package ink.ckx.mo.common.feign.config

import feign.RequestInterceptor
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.DispatcherServlet

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Configuration
class FeignConfig {

    /**
     * 让DispatcherServlet向子线程传递RequestContext
     *
     * @param servlet servlet
     * @return 注册bean
     */
    @Bean
    fun dispatcherRegistration(servlet: DispatcherServlet): ServletRegistrationBean<DispatcherServlet> {
        servlet.setThreadContextInheritable(true)
        return ServletRegistrationBean(servlet, "/**")
    }

    /**
     * 覆写拦截器，在feign发送请求前取出原来的header并转发
     *
     * @return 拦截器
     */
    @Bean
    fun requestInterceptor(): RequestInterceptor {
        return RequestInterceptor { template ->
            val requestAttributes = RequestContextHolder.getRequestAttributes()
            if (requestAttributes is ServletRequestAttributes) {
                val request = requestAttributes.request
                // 只透传必要的请求头，避免敏感头(Cookie)及 hop-by-hop 头(host/content-length)被转发
                for (headerName in FORWARDED_HEADERS) {
                    val value = request.getHeader(headerName)
                    if (value != null) {
                        template.header(headerName, value)
                    }
                }
            }
        }
    }

    companion object {
        // 需要向下游透传的请求头白名单
        private val FORWARDED_HEADERS = setOf(HttpHeaders.AUTHORIZATION)
    }
}