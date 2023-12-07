package ink.ckx.mo.auth.filter

import ink.ckx.mo.auth.util.OAuth2EndpointUtils
import ink.ckx.mo.common.core.result.ResultCode
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.web.filter.OncePerRequestFilter
import java.nio.file.AccessDeniedException

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
class MyExceptionTranslationFilter : OncePerRequestFilter() {

    private val log = KotlinLogging.logger {}

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            filterChain.doFilter(request, response)
        } catch (e: Exception) {
            if (e is AuthenticationException || e is AccessDeniedException) {
                throw e
            }
            // 非AuthenticationException、AccessDeniedException异常，则直接响应
            log.error(e) {}
            OAuth2EndpointUtils.fail(response, ResultCode.AUTH_ERROR, e.message)
        }
    }
}