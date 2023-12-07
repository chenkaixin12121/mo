package ink.ckx.mo.auth.authentication.base

import org.springframework.lang.Nullable
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.util.Assert

/**
 * @author chenkaixin
 * @description 身份验证抽象类
 * @since 2023/11/11
 */
abstract class BaseAuthenticationToken(
    authorizationGrantType: AuthorizationGrantType,
    clientPrincipal: Authentication,
    @Nullable scopes: Set<String>,
    @Nullable additionalParameters: Map<String, Any>
) : AbstractAuthenticationToken(emptyList<GrantedAuthority>()) {

    val authorizationGrantType: AuthorizationGrantType
    private val clientPrincipal: Authentication
    val scopes: Set<String>
    val additionalParameters: Map<String, Any>

    init {
        Assert.notNull(authorizationGrantType, "authorizationGrantType cannot be null")
        Assert.notNull(clientPrincipal, "clientPrincipal cannot be null")
        this.authorizationGrantType = authorizationGrantType
        this.clientPrincipal = clientPrincipal
        this.scopes = scopes
        this.additionalParameters = additionalParameters
    }

    override fun getCredentials(): Any {
        return ""
    }

    override fun getPrincipal(): Any {
        return clientPrincipal
    }
}