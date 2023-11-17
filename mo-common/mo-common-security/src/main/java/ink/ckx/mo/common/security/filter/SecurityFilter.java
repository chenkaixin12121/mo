package ink.ckx.mo.common.security.filter;

import cn.hutool.core.collection.CollUtil;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.security.config.IgnoreUrlProperties;
import ink.ckx.mo.common.security.util.SecurityUtil;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.PathMatcher;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@RequiredArgsConstructor
@Component
public class SecurityFilter implements Filter, Ordered {

    private final IgnoreUrlProperties ignoreUrlProperties;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        PathMatcher pathMatcher = new AntPathMatcher();
        String uri = httpServletRequest.getRequestURI();
        // 指定资源不拦截
        Collection<String> ignoreUrlList = CollUtil.union(ignoreUrlProperties.getWhitelistPaths(), CoreConstant.IGNORE_STATIC_LIST);
        for (String ignoreUrl : ignoreUrlList) {
            if (pathMatcher.match(ignoreUrl, uri)) {
                chain.doFilter(request, response);
                return;
            }
        }
        // 黑名单拦截
        String jti = SecurityUtil.getJti();
        Boolean result = redisTemplate.hasKey(CoreConstant.TOKEN_BLACK + jti);
        if (result != null && result) {
            SecurityUtil.fail(response, ResultCode.INVALID_TOKEN);
            return;
        }
        // 不同用户体系登录不允许互相访问
        String clientId = SecurityUtil.getAud();
        if (List.of(CoreConstant.MO_ADMIN_CLIENT_ID, CoreConstant.MO_KNIFE4J_CLIENT_ID).contains(clientId)
                && pathMatcher.match(CoreConstant.APP_API_PATTERN, uri)) {
            SecurityUtil.fail(response, ResultCode.RESOURCE_VISIT_ERROR);
            return;
        }
        if (CoreConstant.MO_PORTAL_CLIENT_ID.equals(clientId) && !pathMatcher.match(CoreConstant.APP_API_PATTERN, uri)) {
            SecurityUtil.fail(response, ResultCode.RESOURCE_VISIT_ERROR);
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
