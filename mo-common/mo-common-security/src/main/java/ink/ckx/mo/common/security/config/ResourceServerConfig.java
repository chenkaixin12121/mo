package ink.ckx.mo.common.security.config;

import cn.hutool.core.collection.CollUtil;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.security.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import java.util.List;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
public class ResourceServerConfig {

    private final IgnoreUrlProperties ignoreUrlProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector) throws Exception {
        MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);

        http.authorizeHttpRequests((requests) -> {
            List<String> whitelistPaths = ignoreUrlProperties.getWhitelistPaths();
            if (CollUtil.isNotEmpty(whitelistPaths)) {
                for (String whitelistPath : whitelistPaths) {
                    requests.requestMatchers(mvcMatcherBuilder.pattern(whitelistPath)).permitAll();
                }
            }
            requests.anyRequest().authenticated();
        }).csrf(AbstractHttpConfigurer::disable);
        http.oauth2ResourceServer(resourceServerConfigurer ->
                resourceServerConfigurer.jwt(jwtConfigurer -> jwtAuthenticationConverter())
                        .authenticationEntryPoint(((request, response, authException) -> SecurityUtil.fail(response, ResultCode.INVALID_TOKEN)))
                        .accessDeniedHandler((request, response, accessDeniedException) -> SecurityUtil.fail(response, ResultCode.ACCESS_DENIED)));
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(CoreConstant.IGNORE_STATIC_LIST.stream().map(AntPathRequestMatcher::new).toArray(AntPathRequestMatcher[]::new));
    }

    @Bean
    public Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(Strings.EMPTY);
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(CoreConstant.AUTHORITIES_CLAIM_NAME_KEY);

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;
    }
}