package ink.ckx.mo.gateway.config

import ink.ckx.mo.gateway.handler.CaptchaHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.reactive.CorsConfigurationSource
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.util.pattern.PathPatternParser

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@Configuration
class GatewayConfig(
    private val captchaHandler: CaptchaHandler
) {

    /**
     * 验证码
     *
     * @return
     */
    @Bean
    fun captchaRouterFunction(): RouterFunction<ServerResponse> {
        return RouterFunctions
            .route(
                RequestPredicates.GET("/captcha")
                    .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), captchaHandler
            )
    }

    /**
     * 跨域
     *
     * @return
     */
    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource(PathPatternParser())
        val corsConfig = CorsConfiguration()
        // 允许所有请求方法
        corsConfig.addAllowedMethod("*")
        // 允许所有域，当请求头
        corsConfig.addAllowedOriginPattern("*")
        // 允许全部请求头
        corsConfig.addAllowedHeader("*")
        // 允许携带 Authorization 头
        corsConfig.allowCredentials = true
        // 允许全部请求路径
        source.registerCorsConfiguration("/**", corsConfig)
        return source
    }
}