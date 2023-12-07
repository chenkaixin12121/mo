package ink.ckx.mo.auth.authentication.captcha

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType

/**
 * @author chenkaixin
 * @description
 * @since 2023/12/04
 */
class CaptchaGrantAuthenticationToken(
    authorizationGrantType: AuthorizationGrantType,
    clientPrincipal: Authentication,
    scopes: Set<String>,
    additionalParameters: Map<String, Any>
) : BaseAuthenticationToken(authorizationGrantType, clientPrincipal, scopes, additionalParameters)