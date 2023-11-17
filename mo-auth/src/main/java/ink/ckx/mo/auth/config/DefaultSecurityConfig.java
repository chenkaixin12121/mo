package ink.ckx.mo.auth.config;

import ink.ckx.mo.auth.handler.MyAccessDeniedHandler;
import ink.ckx.mo.auth.handler.MyAuthenticationEntryPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/06
 */
@Configuration
@EnableWebSecurity
public class DefaultSecurityConfig {

    /**
     * Spring Security 过滤链配置
     */
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authorize ->
                        authorize.anyRequest().authenticated())
                .exceptionHandling(exceptionHand ->
                        exceptionHand
                                .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                                .accessDeniedHandler(new MyAccessDeniedHandler()));
        return http.build();
    }
}