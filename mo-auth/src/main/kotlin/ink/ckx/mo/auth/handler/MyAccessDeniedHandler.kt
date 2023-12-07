package ink.ckx.mo.auth.handler

import ink.ckx.mo.auth.util.OAuth2EndpointUtils.fail
import ink.ckx.mo.common.core.result.ResultCode
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
class MyAccessDeniedHandler : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException
    ) {
        fail(response, ResultCode.ACCESS_DENIED)
    }
}