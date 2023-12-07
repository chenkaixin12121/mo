package ink.ckx.mo.auth.handler

import ink.ckx.mo.auth.exception.MyAuthenticationException
import ink.ckx.mo.auth.util.OAuth2EndpointUtils.fail
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException
import org.springframework.security.web.AuthenticationEntryPoint

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
class MyAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException
    ) {
        when (authException) {
            is InsufficientAuthenticationException -> {
                fail(response, ResultCode.AUTH_INSUFFICIENT_EXCEPTION)
            }

            is InvalidBearerTokenException -> {
                fail(response, ResultCode.INVALID_TOKEN)
            }

            is MyAuthenticationException -> {
                fail(response, authException.resultCode)
            }

            else -> {
                fail(response, ResultCode.AUTH_ERROR, authException.message)
            }
        }
    }
}