package ink.ckx.mo.common.security.config

import cn.hutool.core.collection.CollUtil
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.common.security.util.SecurityUtil
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.logging.log4j.util.Strings
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.convert.converter.Converter
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.core.AuthenticationException
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import org.springframework.web.servlet.handler.HandlerMappingIntrospector

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(jsr250Enabled = true, securedEnabled = true)
class ResourceServerConfig(private val ignoreUrlProperties: IgnoreUrlProperties) {

    @Bean
    fun securityFilterChain(http: HttpSecurity, introspector: HandlerMappingIntrospector): SecurityFilterChain {
        val mvcMatcherBuilder = MvcRequestMatcher.Builder(introspector)

        http.authorizeHttpRequests {
            val whitelistPaths = ignoreUrlProperties.whitelistPaths
            if (CollUtil.isNotEmpty(whitelistPaths)) {
                for (whitelistPath in whitelistPaths) {
                    it.requestMatchers(mvcMatcherBuilder.pattern(whitelistPath)).permitAll()
                }
            }
            it.anyRequest().authenticated()
        }.csrf { it.disable() }

        http.oauth2ResourceServer {
            it.jwt { jwtAuthenticationConverter() }
                .authenticationEntryPoint { _: HttpServletRequest, response: HttpServletResponse, _: AuthenticationException ->
                    SecurityUtil.fail(response, ResultCode.INVALID_TOKEN)
                }
                .accessDeniedHandler { _: HttpServletRequest, response: HttpServletResponse, _: AccessDeniedException ->
                    SecurityUtil.fail(response, ResultCode.ACCESS_DENIED)
                }
        }
        return http.build()
    }

    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer {
            it.ignoring()
                .requestMatchers(*CoreConstant.IGNORE_STATIC_LIST.map { e -> AntPathRequestMatcher(e) }.toTypedArray())
        }
    }

    @Bean
    fun jwtAuthenticationConverter(): Converter<Jwt, out AbstractAuthenticationToken> {
        val jwtGrantedAuthoritiesConverter = JwtGrantedAuthoritiesConverter()
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix(Strings.EMPTY)
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName(CoreConstant.AUTHORITIES_CLAIM_NAME_KEY)
        val jwtAuthenticationConverter = JwtAuthenticationConverter()
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter)
        return jwtAuthenticationConverter
    }
}