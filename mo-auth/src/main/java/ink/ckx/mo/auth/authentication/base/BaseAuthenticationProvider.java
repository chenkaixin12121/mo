package ink.ckx.mo.auth.authentication.base;

import cn.hutool.core.collection.CollUtil;
import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.common.core.result.ResultCode;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.*;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder;
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenkaixin
 * @description 身份验证提供者抽象类
 * @since 2023/11/11
 */
public abstract class BaseAuthenticationProvider<T extends BaseAuthenticationToken> implements AuthenticationProvider {

    private static final OAuth2TokenType ID_TOKEN_TOKEN_TYPE = new OAuth2TokenType(OidcParameterNames.ID_TOKEN);

    private final OAuth2AuthorizationService authorizationService;

    private final OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator;

    public BaseAuthenticationProvider(OAuth2AuthorizationService authorizationService, OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator) {
        Assert.notNull(authorizationService, "authorizationService cannot be null");
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null");
        this.authorizationService = authorizationService;
        this.tokenGenerator = tokenGenerator;
    }

    /**
     * 构建具体类型的 token
     *
     * @param reqParameters
     * @return
     */
    public abstract UsernamePasswordAuthenticationToken buildToken(Map<String, Object> reqParameters);

    /**
     * 当前 provider 是否支持此令牌类型
     *
     * @param authentication
     * @return
     */
    @Override
    public abstract boolean supports(Class<?> authentication);

    /**
     * 客户端是否支持此授权类型
     *
     * @param registeredClient
     */
    public abstract void checkClient(RegisteredClient registeredClient);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        T resourceOwnerBaseAuthentication = (T) authentication;
        AuthorizationGrantType authorizationGrantType = resourceOwnerBaseAuthentication.getAuthorizationGrantType();

        // 客户端是否支持此授权类型
        OAuth2ClientAuthenticationToken clientPrincipal = this.getAuthenticatedClientElseThrowInvalidClient(resourceOwnerBaseAuthentication);
        RegisteredClient registeredClient = clientPrincipal.getRegisteredClient();
        this.checkClient(registeredClient);

        // 校验身份
        Map<String, Object> additionalParameters = resourceOwnerBaseAuthentication.getAdditionalParameters();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = this.buildToken(additionalParameters);

        // 验证申请访问范围
        Set<String> authorizedScopes = registeredClient.getScopes();
        Set<String> requestedScopes = resourceOwnerBaseAuthentication.getScopes();
        if (CollUtil.isNotEmpty(requestedScopes)) {
            for (String requestedScope : requestedScopes) {
                if (!authorizedScopes.contains(requestedScope)) {
                    throw new MyAuthenticationException(ResultCode.INVALID_SCOPE);
                }
            }
            authorizedScopes = new LinkedHashSet<>(requestedScopes);
        }

        // 初始化 DefaultOAuth2TokenContext
        DefaultOAuth2TokenContext.Builder tokenContextBuilder = DefaultOAuth2TokenContext.builder()
                .registeredClient(registeredClient)
                .principal(usernamePasswordAuthenticationToken)
                .authorizationServerContext(AuthorizationServerContextHolder.getContext())
                .authorizationGrantType(authorizationGrantType)
                .authorizedScopes(authorizedScopes)
                .authorizationGrant(resourceOwnerBaseAuthentication);

        // 初始化 OAuth2Authorization
        OAuth2Authorization.Builder authorizationBuilder = OAuth2Authorization
                .withRegisteredClient(registeredClient)
                .principalName(clientPrincipal.getName())
                .authorizedScopes(authorizedScopes)
                .attribute(Principal.class.getName(), usernamePasswordAuthenticationToken)
                .authorizationGrantType(authorizationGrantType);

        // ----- Access token -----
        OAuth2TokenContext tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build();
        OAuth2Token generatedAccessToken = this.tokenGenerator.generate(tokenContext);
        if (generatedAccessToken == null) {
            throw new MyAuthenticationException(ResultCode.SERVER_ERROR, "The token generator failed to generate the access token.");
        }

        OAuth2AccessToken accessToken = new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, generatedAccessToken.getTokenValue(),
                generatedAccessToken.getIssuedAt(), generatedAccessToken.getExpiresAt(), tokenContext.getAuthorizedScopes());
        if (generatedAccessToken instanceof ClaimAccessor) {
            authorizationBuilder.token(accessToken, (metadata) -> metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, ((ClaimAccessor) generatedAccessToken).getClaims()));
        } else {
            authorizationBuilder.accessToken(accessToken);
        }

        // ----- Refresh token -----
        OAuth2RefreshToken refreshToken = null;
        if (registeredClient.getAuthorizationGrantTypes().contains(AuthorizationGrantType.REFRESH_TOKEN) &&
                // Do not issue refresh token to public client
                !clientPrincipal.getClientAuthenticationMethod().equals(ClientAuthenticationMethod.NONE)) {

            tokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.REFRESH_TOKEN).build();
            OAuth2Token generatedRefreshToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedRefreshToken instanceof OAuth2RefreshToken)) {
                throw new MyAuthenticationException(ResultCode.SERVER_ERROR, "The token generator failed to generate the refresh token.");
            }

            refreshToken = (OAuth2RefreshToken) generatedRefreshToken;
            authorizationBuilder.refreshToken(refreshToken);
        }

        // ----- ID token -----
        Set<String> scopes = this.getInterseSet(registeredClient.getScopes(), requestedScopes);
        OidcIdToken idToken;
        if (scopes.contains(OidcScopes.OPENID)) {
            // @formatter:off
            tokenContext = tokenContextBuilder.tokenType(ID_TOKEN_TOKEN_TYPE).authorization(authorizationBuilder.build())    // ID token customizer may need access to the access token and/or refresh token
                    .build();
            // @formatter:on
            OAuth2Token generatedIdToken = this.tokenGenerator.generate(tokenContext);
            if (!(generatedIdToken instanceof Jwt)) {
                throw new MyAuthenticationException(ResultCode.SERVER_ERROR, "The token generator failed to generate the ID token.");
            }

            idToken = new OidcIdToken(generatedIdToken.getTokenValue(), generatedIdToken.getIssuedAt(), generatedIdToken.getExpiresAt(), ((Jwt) generatedIdToken).getClaims());
            authorizationBuilder.token(idToken, (metadata) -> {
                metadata.put(OAuth2Authorization.Token.CLAIMS_METADATA_NAME, idToken.getClaims());
            });
        } else {
            idToken = null;
        }

        // 保存认证信息
        OAuth2Authorization authorization = authorizationBuilder.build();
        this.authorizationService.save(authorization);

        if (idToken != null) {
            additionalParameters = new HashMap<>();
            additionalParameters.put(OidcParameterNames.ID_TOKEN, idToken.getTokenValue());
        }

        return new OAuth2AccessTokenAuthenticationToken(registeredClient, clientPrincipal, accessToken, refreshToken, additionalParameters);
    }

    private OAuth2ClientAuthenticationToken getAuthenticatedClientElseThrowInvalidClient(Authentication authentication) {
        OAuth2ClientAuthenticationToken clientPrincipal = null;
        if (OAuth2ClientAuthenticationToken.class.isAssignableFrom(authentication.getPrincipal().getClass())) {
            clientPrincipal = (OAuth2ClientAuthenticationToken) authentication.getPrincipal();
        }
        if (clientPrincipal != null && clientPrincipal.isAuthenticated()) {
            return clientPrincipal;
        }
        throw new MyAuthenticationException(ResultCode.INVALID_CLIENT);
    }

    private Set<String> getInterseSet(Set<String> set1, Set<String> set2) {
        if (CollectionUtils.isEmpty(set1) || CollectionUtils.isEmpty(set2)) {
            return Set.of();
        }
        Set<String> set = set1.stream().filter(set2::contains).collect(Collectors.toSet());
        if (CollectionUtils.isEmpty(set)) {
            set = Set.of();
        }
        return set;
    }
}