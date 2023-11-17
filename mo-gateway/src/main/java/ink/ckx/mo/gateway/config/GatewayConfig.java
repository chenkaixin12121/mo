package ink.ckx.mo.gateway.config;

import ink.ckx.mo.gateway.handler.CaptchaHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.util.pattern.PathPatternParser;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@RequiredArgsConstructor
@Configuration
public class GatewayConfig {

    private final CaptchaHandler captchaHandler;

    /**
     * 验证码
     *
     * @return
     */
    @Bean
    public RouterFunction<ServerResponse> captchaRouterFunction() {
        return RouterFunctions
                .route(RequestPredicates.GET("/captcha")
                        .and(RequestPredicates.accept(MediaType.TEXT_PLAIN)), captchaHandler);
    }

    /**
     * 跨域
     *
     * @return
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource(new PathPatternParser());
        CorsConfiguration corsConfig = new CorsConfiguration();
        // 允许所有请求方法
        corsConfig.addAllowedMethod("*");
        // 允许所有域，当请求头
        corsConfig.addAllowedOriginPattern("*");
        // 允许全部请求头
        corsConfig.addAllowedHeader("*");
        // 允许携带 Authorization 头
        corsConfig.setAllowCredentials(true);
        // 允许全部请求路径
        source.registerCorsConfiguration("/**", corsConfig);
        return source;
    }
}