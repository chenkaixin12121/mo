package ink.ckx.mo.auth.handler

import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.fail
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.http.server.ServletServerHttpResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.core.OAuth2AuthenticationException
import org.springframework.security.oauth2.core.OAuth2ErrorCodes
import org.springframework.security.web.authentication.AuthenticationFailureHandler

class MyAuthenticationFailureHandler : AuthenticationFailureHandler {

    private val accessTokenHttpResponseConverter: HttpMessageConverter<Any> = MappingJackson2HttpMessageConverter()

    override fun onAuthenticationFailure(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        val error: Result<Any> = createError(exception)
        val httpResponse = ServletServerHttpResponse(response)
        accessTokenHttpResponseConverter.write(error, null, httpResponse)
    }

    private fun createError(exception: AuthenticationException): Result<Any> {
        if (exception is OAuth2AuthenticationException) {
            val errorCode: String = exception.error.errorCode
            val description: String = exception.error.description
            return when (errorCode) {
                OAuth2ErrorCodes.INVALID_CLIENT -> {
                    fail(ResultCode.INVALID_CLIENT)
                }

                OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE -> {
                    fail(ResultCode.UNSUPPORTED_GRANT_TYPE)
                }

                OAuth2ErrorCodes.INVALID_REQUEST -> {
                    fail(ResultCode.INVALID_REQUEST)
                }

                OAuth2ErrorCodes.INVALID_GRANT -> {
                    fail(ResultCode.INVALID_GRANT)
                }

                OAuth2ErrorCodes.INVALID_SCOPE -> {
                    fail(ResultCode.INVALID_SCOPE)
                }

                OAuth2ErrorCodes.SERVER_ERROR -> {
                    fail(ResultCode.SERVER_ERROR)
                }

                else -> {
                    fail(ResultCode.AUTH_ERROR, description)
                }
            }
        }
        return fail(ResultCode.AUTH_ERROR, exception.message)
    }
}