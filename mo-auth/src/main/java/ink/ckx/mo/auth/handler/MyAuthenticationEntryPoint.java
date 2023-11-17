package ink.ckx.mo.auth.handler;

import ink.ckx.mo.auth.exception.MyAuthenticationException;
import ink.ckx.mo.auth.util.OAuth2EndpointUtils;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) {
        if (authException instanceof InsufficientAuthenticationException) {
            OAuth2EndpointUtils.fail(response, ResultCode.AUTH_INSUFFICIENT_EXCEPTION);
        } else if (authException instanceof InvalidBearerTokenException) {
            OAuth2EndpointUtils.fail(response, ResultCode.INVALID_TOKEN);
        } else if (authException instanceof MyAuthenticationException) {
            OAuth2EndpointUtils.fail(response, ((MyAuthenticationException) authException).getResultCode());
        } else {
            OAuth2EndpointUtils.fail(response, ResultCode.AUTH_ERROR, authException.getMessage());
        }
    }
}