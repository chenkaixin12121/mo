package ink.ckx.mo.auth.authentication.password

import ink.ckx.mo.auth.authentication.base.BaseAuthenticationProvider
import ink.ckx.mo.auth.exception.MyAuthenticationException
import ink.ckx.mo.auth.userdetails.user.SysUserDetailsService
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.OAuth2Token
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
class PasswordGrantAuthenticationProvider(
    authorizationService: OAuth2AuthorizationService,
    tokenGenerator: OAuth2TokenGenerator<out OAuth2Token>,
    private val sysUserDetailsService: SysUserDetailsService,
    private val passwordEncoder: PasswordEncoder
) : BaseAuthenticationProvider<PasswordGrantAuthenticationToken>(authorizationService, tokenGenerator) {

    override fun buildToken(reqParameters: Map<String, Any>): UsernamePasswordAuthenticationToken {
        // 用户名密码校验
        val username = reqParameters[OAuth2ParameterNames.USERNAME] as String
        val password = reqParameters[OAuth2ParameterNames.PASSWORD] as String
        val userDetails = sysUserDetailsService.loadUserByUsername(username)
        if (!passwordEncoder.matches(password, userDetails.password)) {
            throw MyAuthenticationException(ResultCode.PASSWORD_VERIFY_FAIL)
        }
        return UsernamePasswordAuthenticationToken.authenticated(userDetails, password, userDetails.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return PasswordGrantAuthenticationToken::class.java.isAssignableFrom(authentication)
    }

    override fun checkClient(registeredClient: RegisteredClient) {
        if (AuthorizationGrantType(CoreConstant.GRANT_TYPE_CUSTOM_PASSWORD) !in registeredClient.authorizationGrantTypes) {
            throw MyAuthenticationException(ResultCode.UNSUPPORTED_GRANT_TYPE)
        }
    }
}