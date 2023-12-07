package ink.ckx.mo.auth.authentication.mobile

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationConverter
import ink.ckx.mo.auth.authentication.base.checkNotBlank
import ink.ckx.mo.auth.util.OAuth2EndpointUtils
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
class MobileGrantAuthenticationConverter : BaseAuthenticationConverter<MobileGrantAuthenticationToken>() {

    override fun support(grantType: String): Boolean {
        return CoreConstant.GRANT_TYPE_CUSTOM_MOBILE == grantType
    }

    override fun checkParams(request: HttpServletRequest) {
        val parameters = OAuth2EndpointUtils.getParameters(request)
        checkNotBlank(parameters.getFirst(CoreConstant.MOBILE), ResultCode.MOBILE_NOT_EMPTY)
        checkNotBlank(parameters.getFirst(CoreConstant.SMS_CODE), ResultCode.SMS_CODE_NOT_EMPTY)
    }

    override fun buildToken(
        clientPrincipal: Authentication,
        requestedScopes: Set<String>,
        additionalParameters: Map<String, Any>
    ): MobileGrantAuthenticationToken {
        return MobileGrantAuthenticationToken(
            AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_MOBILE),
            clientPrincipal,
            requestedScopes,
            additionalParameters
        )
    }
}