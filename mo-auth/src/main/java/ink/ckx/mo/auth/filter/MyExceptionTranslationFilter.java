package ink.ckx.mo.auth.filter;

import ink.ckx.mo.auth.util.OAuth2EndpointUtils;
import ink.ckx.mo.common.core.result.ResultCode;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Slf4j
public class MyExceptionTranslationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof AuthenticationException || e instanceof AccessDeniedException) {
                throw e;
            }
            // 非AuthenticationException、AccessDeniedException异常，则直接响应
            log.error("", e);
            OAuth2EndpointUtils.fail(response, ResultCode.AUTH_ERROR, e.getMessage());
        }
    }
}