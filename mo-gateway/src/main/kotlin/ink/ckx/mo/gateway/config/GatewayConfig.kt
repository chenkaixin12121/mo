package ink.ckx.mo.gateway.config

import ink.ckx.mo.gateway.handler.CaptchaHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerResponse

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
}