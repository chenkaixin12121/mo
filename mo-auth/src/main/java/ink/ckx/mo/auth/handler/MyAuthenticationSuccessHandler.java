package ink.ckx.mo.auth.handler;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.OAuth2RefreshToken;
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.time.temporal.ChronoUnit;
import java.util.Map;

/**
 * @author chenkaixin
 * @description 认证成功处理器
 * @since 2023/11/10
 */
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    private final Converter<OAuth2AccessTokenResponse, Map<String, Object>> accessTokenResponseParametersConverter =
            new DefaultOAuth2AccessTokenResponseMapConverter();

    @SneakyThrows
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) {
        OAuth2AccessTokenAuthenticationToken accessTokenAuthentication = (OAuth2AccessTokenAuthenticationToken) authentication;

        OAuth2AccessToken accessToken = accessTokenAuthentication.getAccessToken();
        OAuth2RefreshToken refreshToken = accessTokenAuthentication.getRefreshToken();
        Map<String, Object> additionalParameters = accessTokenAuthentication.getAdditionalParameters();

        OAuth2AccessTokenResponse.Builder builder = OAuth2AccessTokenResponse.withToken(accessToken.getTokenValue())
                .tokenType(accessToken.getTokenType()).scopes(accessToken.getScopes());
        if (accessToken.getIssuedAt() != null && accessToken.getExpiresAt() != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.getIssuedAt(), accessToken.getExpiresAt()));
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.getTokenValue());
        }
        if (CollUtil.isNotEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters);
        }
        OAuth2AccessTokenResponse accessTokenResponse = builder.build();

        Map<String, Object> tokenResponseParameters = this.accessTokenResponseParametersConverter.convert(accessTokenResponse);
        MapUtil.removeAny(tokenResponseParameters, CoreConstant.MOBILE, CoreConstant.SMS_CODE,
                OAuth2ParameterNames.USERNAME, OAuth2ParameterNames.PASSWORD);
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);

        String clientId = accessTokenAuthentication.getRegisteredClient().getClientId();
        if (CoreConstant.MO_KNIFE4J_CLIENT_ID.equals(clientId)) {
            this.accessTokenHttpResponseConverter.write(tokenResponseParameters, null, httpResponse);
        } else {
            this.accessTokenHttpResponseConverter.write(Result.success(tokenResponseParameters), null, httpResponse);
        }
    }
}