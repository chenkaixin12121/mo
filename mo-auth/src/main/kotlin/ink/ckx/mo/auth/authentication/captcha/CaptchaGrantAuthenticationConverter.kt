package ink.ckx.mo.auth.authentication.captcha

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationConverter
import ink.ckx.mo.auth.authentication.base.checkNotBlank
import ink.ckx.mo.auth.util.OAuth2EndpointUtils
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames

/**
 * @author chenkaixin
 * @description
 * @since 2023/12/04
 */
class CaptchaGrantAuthenticationConverter : BaseAuthenticationConverter<CaptchaGrantAuthenticationToken>() {

    override fun support(grantType: String): Boolean {
        return CoreConstant.GRANT_TYPE_CUSTOM_CAPTCHA == grantType
    }

    override fun checkParams(request: HttpServletRequest) {
        val parameters = OAuth2EndpointUtils.getParameters(request)
        checkNotBlank(parameters.getFirst(CoreConstant.VERIFY_CODE), ResultCode.VERIFY_CODE_NOT_EMPTY)
        checkNotBlank(parameters.getFirst(CoreConstant.VERIFY_CODE_KEY), ResultCode.VERIFY_CODE_KEY_NOT_EMPTY)
        checkNotBlank(parameters.getFirst(OAuth2ParameterNames.USERNAME), ResultCode.USERNAME_NOT_EMPTY)
        checkNotBlank(parameters.getFirst(OAuth2ParameterNames.PASSWORD), ResultCode.PASSWORD_NOT_EMTPY)
    }

    override fun buildToken(
        clientPrincipal: Authentication,
        requestedScopes: Set<String>,
        additionalParameters: Map<String, Any>
    ): CaptchaGrantAuthenticationToken {
        return CaptchaGrantAuthenticationToken(
            AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_CAPTCHA),
            clientPrincipal,
            requestedScopes,
            additionalParameters
        )
    }
}