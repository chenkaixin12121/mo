package ink.ckx.mo.auth.handler

import cn.hutool.core.collection.CollUtil
import cn.hutool.core.map.MapUtil
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.Result
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.convert.converter.Converter
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.OAuth2AccessToken
import org.springframework.security.oauth2.core.OAuth2RefreshToken
import org.springframework.security.oauth2.core.endpoint.DefaultOAuth2AccessTokenResponseMapConverter
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.authentication.OAuth2AccessTokenAuthenticationToken
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import java.time.temporal.ChronoUnit

/**
 * @author chenkaixin
 * @description 认证成功处理器
 * @since 2023/11/10
 */
class MyAuthenticationSuccessHandler : AuthenticationSuccessHandler {

    private val accessTokenHttpResponseConverter: HttpMessageConverter<Any> = MappingJackson2HttpMessageConverter()
    private val accessTokenResponseParametersConverter: Converter<OAuth2AccessTokenResponse, Map<String, Any>> =
        DefaultOAuth2AccessTokenResponseMapConverter()

    override fun onAuthenticationSuccess(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val accessTokenAuthentication: OAuth2AccessTokenAuthenticationToken =
            authentication as OAuth2AccessTokenAuthenticationToken

        val accessToken: OAuth2AccessToken = accessTokenAuthentication.accessToken
        val refreshToken: OAuth2RefreshToken? = accessTokenAuthentication.refreshToken
        val additionalParameters: Map<String, Any> = accessTokenAuthentication.additionalParameters

        val builder: OAuth2AccessTokenResponse.Builder =
            OAuth2AccessTokenResponse.withToken(accessToken.tokenValue)
                .tokenType(accessToken.tokenType).scopes(accessToken.scopes)
        if (accessToken.issuedAt != null && accessToken.expiresAt != null) {
            builder.expiresIn(ChronoUnit.SECONDS.between(accessToken.issuedAt, accessToken.expiresAt))
        }
        if (refreshToken != null) {
            builder.refreshToken(refreshToken.tokenValue)
        }
        if (CollUtil.isNotEmpty(additionalParameters)) {
            builder.additionalParameters(additionalParameters)
        }
        val accessTokenResponse: OAuth2AccessTokenResponse = builder.build()

        val tokenResponseParameters = accessTokenResponseParametersConverter.convert(accessTokenResponse)!!
        MapUtil.removeAny(
            tokenResponseParameters, CoreConstant.MOBILE, CoreConstant.SMS_CODE,
            OAuth2ParameterNames.USERNAME, OAuth2ParameterNames.PASSWORD
        )
        val httpResponse = ServletServerHttpResponse(response)
        val clientId: String = accessTokenAuthentication.registeredClient.clientId
        if (CoreConstant.MO_KNIFE4J_CLIENT_ID == clientId) {
            accessTokenHttpResponseConverter.write(tokenResponseParameters, null, httpResponse)
        } else {
            accessTokenHttpResponseConverter.write(Result.success(tokenResponseParameters), null, httpResponse)
        }
    }
}