package ink.ckx.mo.auth.handler;

import ink.ckx.mo.common.core.result.Result;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2ErrorCodes;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

/**
 * @author chenkaixin
 * @description 认证失败处理器
 * @since 2023/11/10
 */
public class MyAuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final HttpMessageConverter<Object> accessTokenHttpResponseConverter = new MappingJackson2HttpMessageConverter();

    @SneakyThrows
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        Result<?> error = this.createError(exception);
        ServletServerHttpResponse httpResponse = new ServletServerHttpResponse(response);
        accessTokenHttpResponseConverter.write(error, null, httpResponse);
    }

    private Result<?> createError(AuthenticationException exception) {
        if (exception instanceof OAuth2AuthenticationException oAuth2AuthenticationException) {
            String errorCode = oAuth2AuthenticationException.getError().getErrorCode();
            String description = oAuth2AuthenticationException.getError().getDescription();
            switch (errorCode) {
                case OAuth2ErrorCodes.INVALID_CLIENT -> {
                    return Result.fail(ResultCode.INVALID_CLIENT);
                }
                case OAuth2ErrorCodes.UNSUPPORTED_GRANT_TYPE -> {
                    return Result.fail(ResultCode.UNSUPPORTED_GRANT_TYPE);
                }
                case OAuth2ErrorCodes.INVALID_REQUEST -> {
                    return Result.fail(ResultCode.INVALID_REQUEST);
                }
                case OAuth2ErrorCodes.INVALID_GRANT -> {
                    return Result.fail(ResultCode.INVALID_GRANT);
                }
                case OAuth2ErrorCodes.INVALID_SCOPE -> {
                    return Result.fail(ResultCode.INVALID_SCOPE);
                }
                case OAuth2ErrorCodes.SERVER_ERROR -> {
                    return Result.fail(ResultCode.SERVER_ERROR);
                }
                default -> {
                    return Result.fail(ResultCode.AUTH_ERROR, description);
                }
            }
        }
        return Result.fail(ResultCode.AUTH_ERROR, exception.getMessage());
    }
}