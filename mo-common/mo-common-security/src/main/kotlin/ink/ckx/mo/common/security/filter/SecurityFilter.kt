package ink.ckx.mo.common.security.filter

import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.common.security.config.IgnoreUrlProperties
import ink.ckx.mo.common.security.util.SecurityUtil
import ink.ckx.mo.common.security.util.getAud
import ink.ckx.mo.common.security.util.getJti
import jakarta.servlet.Filter
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.springframework.core.Ordered
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.util.PathMatcher

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@Component
class SecurityFilter(
    private val ignoreUrlProperties: IgnoreUrlProperties,
    private val redisTemplate: RedisTemplate<String, String>
) : Filter, Ordered {

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val pathMatcher: PathMatcher = AntPathMatcher()
        val uri = httpServletRequest.requestURI
        // 指定资源不拦截
        val ignoreUrlList = CoreConstant.IGNORE_STATIC_LIST.union(ignoreUrlProperties.whitelistPaths)
        for (ignoreUrl in ignoreUrlList) {
            if (pathMatcher.match(ignoreUrl, uri)) {
                chain.doFilter(request, response)
                return
            }
        }
        // 黑名单拦截
        val jti = getJti()
        val result = redisTemplate.hasKey(CoreConstant.TOKEN_BLACK + jti)
        if (result) {
            SecurityUtil.fail(response, ResultCode.INVALID_TOKEN)
            return
        }
        // 不同用户体系登录不允许互相访问
        val clientId = getAud()
        if (listOf(CoreConstant.MO_ADMIN_CLIENT_ID, CoreConstant.MO_KNIFE4J_CLIENT_ID).contains(clientId)
            && pathMatcher.match(CoreConstant.APP_API_PATTERN, uri)
        ) {
            SecurityUtil.fail(response, ResultCode.RESOURCE_VISIT_ERROR)
            return
        }
        if (CoreConstant.MO_PORTAL_CLIENT_ID == clientId && !pathMatcher.match(CoreConstant.APP_API_PATTERN, uri)) {
            SecurityUtil.fail(response, ResultCode.RESOURCE_VISIT_ERROR)
            return
        }
        chain.doFilter(request, response)
    }

    override fun getOrder(): Int {
        return 0
    }
}