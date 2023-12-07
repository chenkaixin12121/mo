package ink.ckx.mo.auth.authentication.base

import cn.hutool.core.collection.CollUtil
import ink.ckx.mo.auth.exception.MyAuthenticationException
import ink.ckx.mo.common.core.result.ResultCode
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.*
import org.springframework.security.oauth2.core.oidc.OidcIdToken
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientAuthenticationToken
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.context.AuthorizationServerContextHolder
import org.springframework.security.oauth2.server.authorization.token.DefaultOAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator
import org.springframework.util.Assert
import java.security.Principal

/**
 * @author chenkaixin
 * @description 身份验证提供者抽象类
 * @since 2023/11/11
 */
abstract class BaseAuthenticationProvider<T : BaseAuthenticationToken>(
    authorizationService: OAuth2AuthorizationService,
    tokenGenerator: OAuth2TokenGenerator<out OAuth2Token>
) : AuthenticationProvider {

    private val authorizationService: OAuth2AuthorizationService
    private val tokenGenerator: OAuth2TokenGenerator<out OAuth2Token>

    init {
        Assert.notNull(authorizationService, "authorizationService cannot be null")
        Assert.notNull(tokenGenerator, "tokenGenerator cannot be null")
        this.authorizationService = authorizationService
        this.tokenGenerator = tokenGenerator
    }

    /**
     * 构建具体类型的 token
     *
     * @param reqParameters
     * @return
     */
    abstract fun buildToken(reqParameters: Map<String, Any>): UsernamePasswordAuthenticationToken

    /**
     * 当前 provider 是否支持此令牌类型
     *
     * @param authentication
     * @return
     */
    abstract override fun supports(authentication: Class<*>): Boolean

    /**
     * 客户端是否支持此授权类型
     *
     * @param registeredClient
     */
    abstract fun checkClient(registeredClient: RegisteredClient)

    override fun authenticate(authentication: Authentication): Authentication {
        val resourceOwnerBaseAuthentication = authentication as T
        val authorizationGrantType = resourceOwnerBaseAuthentication.authorizationGrantType

        // 客户端是否支持此授权类型
        val clientPrincipal = getAuthenticatedClientElseThrowInvalidClient(resourceOwnerBaseAuthentication)
        val registeredClient =
            clientPrincipal.registeredClient ?: throw MyAuthenticationException(ResultCode.SERVER_ERROR)
        checkClient(registeredClient)

        // 校验身份
        var additionalParameters = resourceOwnerBaseAuthentication.additionalParameters
        val usernamePasswordAuthenticationToken = buildToken(additionalParameters)

        // 验证申请访问范围
        var authorizedScopes = registeredClient.scopes
        val requestedScopes = resourceOwnerBaseAuthentication.scopes
        if (CollUtil.isNotEmpty(requestedScopes)) {
            if (requestedScopes.any { it !in authorizedScopes }) {
                throw MyAuthenticationException(ResultCode.INVALID_SCOPE)
            }
            authorizedScopes = LinkedHashSet(requestedScopes)
        }

        // 初始化 DefaultOAuth2TokenContext
        val tokenContextBuilder = DefaultOAuth2TokenContext.builder()
            .registeredClient(registeredClient)
            .principal(usernamePasswordAuthenticationToken)
            .authorizationServerContext(AuthorizationServerContextHolder.getContext())
            .authorizationGrantType(authorizationGrantType)
            .authorizedScopes(authorizedScopes)
            .authorizationGrant(resourceOwnerBaseAuthentication)

        // 初始化 OAuth2Authorization
        val authorizationBuilder = OAuth2Authorization
            .withRegisteredClient(registeredClient)
            .principalName(clientPrincipal.name)
            .authorizedScopes(authorizedScopes)
            .attribute(Principal::class.java.name, usernamePasswordAuthenticationToken)
            .authorizationGrantType(authorizationGrantType)

        // ----- Access token -----
        var tokenContext: OAuth2TokenContext = tokenContextBuilder.tokenType(OAuth2TokenType.ACCESS_TOKEN).build()
        val generatedAccessToken = tokenGenerator.generate(tokenContext)
            ?: throw MyAuthenticationException(
                ResultCode.SERVER_ERROR,
                "The token generator failed to generate the access token."
            )
        val accessToken = OAuth2AccessToken(
            OAuth2AccessToken.TokenType.BEARER,
            generatedAccessToken.tokenValue,
            generatedAccessToken.issuedAt,
            generatedAccessToken.expiresAt,
            tokenContext.authorizedScopes
        )
        if (generatedAccessToken is ClaimAccessor) {
            authorizationBuilder.token(accessToken) { metadata ->
                metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] =
                    (generatedAccessToken as ClaimAccessor).claims
            }
        } else {
            authorizationBuilder.accessToken(accessToken)
        }

        // ----- Refresh token -----
        var refreshToken: OAuth2RefreshToken? = null
        if (AuthorizationGrantType.REFRESH_TOKEN in registeredClient.authorizationGrantTypes &&
            // Do not issue refresh token to public client
            clientPrincipal.clientAuthenticationMethod != ClientAuthenticationMethod.NONE
        ) {
            tokenContext = tokenContextBuilder
                .tokenType(OAuth2TokenType.REFRESH_TOKEN)
                .build()
            val generatedRefreshToken = tokenGenerator.generate(tokenContext) as? OAuth2RefreshToken
                ?: throw MyAuthenticationException(
                    ResultCode.SERVER_ERROR,
                    "The token generator failed to generate the refresh token."
                )
            refreshToken = generatedRefreshToken
            authorizationBuilder.refreshToken(refreshToken)
        }

        // ----- ID token -----
        val scopes = getIntersectionSet(registeredClient.scopes, requestedScopes)
        val idToken: OidcIdToken?
        if (OidcScopes.OPENID in scopes) {
            // @formatter:off
            tokenContext = tokenContextBuilder.tokenType(ID_TOKEN_TOKEN_TYPE)
                // ID token customizer may need access to the access token and/or refresh token
                .authorization(authorizationBuilder.build())
                .build()
            // @formatter:on
            val generatedIdToken = tokenGenerator.generate(tokenContext) as? Jwt
                ?: throw MyAuthenticationException(
                    ResultCode.SERVER_ERROR,
                    "The token generator failed to generate the ID token."
                )
            idToken = OidcIdToken(
                generatedIdToken.tokenValue,
                generatedIdToken.issuedAt,
                generatedIdToken.expiresAt,
                generatedIdToken.claims
            )
            authorizationBuilder.token(idToken) { metadata ->
                metadata[OAuth2Authorization.Token.CLAIMS_METADATA_NAME] = idToken.claims
            }
        } else {
            idToken = null
        }

        // 保存认证信息
        val authorization = authorizationBuilder.build()
        authorizationService.save(authorization)
        if (idToken != null) {
            additionalParameters = HashMap()
            additionalParameters[OidcParameterNames.ID_TOKEN] = idToken.tokenValue
        }
        return OAuth2AccessTokenAuthenticationToken(
            registeredClient,
            clientPrincipal,
            accessToken,
            refreshToken,
            additionalParameters
        )
    }

    private fun getAuthenticatedClientElseThrowInvalidClient(authentication: T): OAuth2ClientAuthenticationToken {
        return (authentication.principal as? OAuth2ClientAuthenticationToken)
            ?.takeIf { it.isAuthenticated }
            ?: throw MyAuthenticationException(ResultCode.INVALID_CLIENT)
    }

    private fun getIntersectionSet(set1: Set<String>, set2: Set<String>): Set<String> {
        val resultSet = set1.intersect(set2)
        return resultSet.takeIf { it.isNotEmpty() } ?: mutableSetOf()
    }

    companion object {
        private val ID_TOKEN_TOKEN_TYPE = OAuth2TokenType(OidcParameterNames.ID_TOKEN)
    }
}