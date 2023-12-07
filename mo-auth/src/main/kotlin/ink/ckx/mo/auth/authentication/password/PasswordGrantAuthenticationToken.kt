package ink.ckx.mo.auth.authentication.password

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.core.AuthorizationGrantType

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
class PasswordGrantAuthenticationToken(
    authorizationGrantType: AuthorizationGrantType,
    clientPrincipal: Authentication,
    scopes: Set<String>,
    additionalParameters: Map<String, Any>
) : BaseAuthenticationToken(authorizationGrantType, clientPrincipal, scopes, additionalParameters)