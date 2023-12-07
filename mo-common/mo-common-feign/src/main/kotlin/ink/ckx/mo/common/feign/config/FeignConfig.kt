package ink.ckx.mo.common.feign.config

import feign.RequestInterceptor
import org.springframework.boot.web.servlet.ServletRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
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
        return RequestInterceptor {
            val requestAttributes = RequestContextHolder.getRequestAttributes()
            if (requestAttributes != null) {
                val attributes = requestAttributes as ServletRequestAttributes
                val request = attributes.request
                // 获取请求头
                val headerNames = request.headerNames
                if (headerNames != null) {
                    while (headerNames.hasMoreElements()) {
                        val name = headerNames.nextElement()
                        val values = request.getHeader(name)
                        // 将请求头保存到模板中
                        it.header(name, values)
                    }
                }
            }
        }
    }
}