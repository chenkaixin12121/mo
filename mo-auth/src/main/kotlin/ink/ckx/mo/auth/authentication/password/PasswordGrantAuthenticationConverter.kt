package ink.ckx.mo.auth.authentication.password

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
 * @since 2023/11/06
 */
class PasswordGrantAuthenticationConverter : BaseAuthenticationConverter<PasswordGrantAuthenticationToken>() {

    override fun support(grantType: String): Boolean {
        return CoreConstant.GRANT_TYPE_CUSTOM_PASSWORD == grantType
    }

    override fun checkParams(request: HttpServletRequest) {
        val parameters = OAuth2EndpointUtils.getParameters(request)
        checkNotBlank(parameters.getFirst(OAuth2ParameterNames.USERNAME), ResultCode.USERNAME_NOT_EMPTY)
        checkNotBlank(parameters.getFirst(OAuth2ParameterNames.PASSWORD), ResultCode.PASSWORD_NOT_EMTPY)
    }

    override fun buildToken(
        clientPrincipal: Authentication,
        requestedScopes: Set<String>,
        additionalParameters: Map<String, Any>
    ): PasswordGrantAuthenticationToken {
        return PasswordGrantAuthenticationToken(
            AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_PASSWORD),
            clientPrincipal,
            requestedScopes,
            additionalParameters
        )
    }
}