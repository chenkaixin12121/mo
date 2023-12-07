package ink.ckx.mo.auth.config

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.ImmutableJWKSet
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import ink.ckx.mo.auth.authentication.captcha.CaptchaGrantAuthenticationConverter
import ink.ckx.mo.auth.authentication.captcha.CaptchaGrantAuthenticationProvider
import ink.ckx.mo.auth.authentication.mobile.MobileGrantAuthenticationConverter
import ink.ckx.mo.auth.authentication.mobile.MobileGrantAuthenticationProvider
import ink.ckx.mo.auth.authentication.password.PasswordGrantAuthenticationConverter
import ink.ckx.mo.auth.authentication.password.PasswordGrantAuthenticationProvider
import ink.ckx.mo.auth.filter.MyExceptionTranslationFilter
import ink.ckx.mo.auth.handler.MyAccessDeniedHandler
import ink.ckx.mo.auth.handler.MyAuthenticationEntryPoint
import ink.ckx.mo.auth.handler.MyAuthenticationFailureHandler
import ink.ckx.mo.auth.handler.MyAuthenticationSuccessHandler
import ink.ckx.mo.auth.userdetails.member.MemberUserDetails
import ink.ckx.mo.auth.userdetails.member.MemberUserDetailsService
import ink.ckx.mo.auth.userdetails.user.SysUserDetails
import ink.ckx.mo.auth.userdetails.user.SysUserDetailsService
import ink.ckx.mo.auth.util.KeyStoreKeyFactory
import ink.ckx.mo.common.core.constant.CoreConstant
import org.apache.catalina.util.StandardSessionIdGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.core.io.ClassPathResource
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames
import org.springframework.security.oauth2.jwt.JwtClaimNames
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import org.springframework.security.oauth2.server.authorization.*
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2TokenEndpointConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.token.*
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.access.ExceptionTranslationFilter
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Instant
import java.util.*

@Configuration
class AuthorizationServerConfig(
    private val memberUserDetailsService: MemberUserDetailsService,
    private val sysUserDetailsService: SysUserDetailsService,
    private val redisTemplate: RedisTemplate<String, String>,
) {

    @Bean
    @Order(1)
    fun authorizationServerSecurityFilterChain(
        http: HttpSecurity, authorizationService: OAuth2AuthorizationService, tokenGenerator: OAuth2TokenGenerator<*>
    ): SecurityFilterChain {
        val authorizationServerConfigurer = OAuth2AuthorizationServerConfigurer()
        authorizationServerConfigurer
            // 设置自定义密码模式
            .tokenEndpoint { tokenEndpoint: OAuth2TokenEndpointConfigurer ->
                tokenEndpoint
                    // 自定义授权模式转换器
                    .accessTokenRequestConverters {
                        it.addAll(
                            listOf(
                                PasswordGrantAuthenticationConverter(),
                                MobileGrantAuthenticationConverter(),
                                CaptchaGrantAuthenticationConverter()
                            )
                        )
                    }
                    // 自定义授权模式提供者
                    .authenticationProviders {
                        it.addAll(
                            listOf(
                                PasswordGrantAuthenticationProvider(
                                    authorizationService, tokenGenerator, sysUserDetailsService, passwordEncoder()
                                ), MobileGrantAuthenticationProvider(
                                    authorizationService, tokenGenerator, memberUserDetailsService, redisTemplate
                                ), CaptchaGrantAuthenticationProvider(
                                    authorizationService,
                                    tokenGenerator,
                                    sysUserDetailsService,
                                    redisTemplate,
                                    passwordEncoder()
                                )
                            )
                        )
                    }
                    // 自定义成功与失败响应
                    .errorResponseHandler(MyAuthenticationFailureHandler())
                    .accessTokenResponseHandler(MyAuthenticationSuccessHandler())
            }
            // 配置客户端认证处理器
            .clientAuthentication { it.errorResponseHandler(MyAuthenticationFailureHandler()) }
            // 开启 OpenID Connect
            .oidc(Customizer.withDefaults())
        http.addFilterBefore(MyExceptionTranslationFilter(), ExceptionTranslationFilter::class.java)
            .authorizeHttpRequests { it.anyRequest().authenticated() }
            .csrf { it.disable() }
            .exceptionHandling {
                it.authenticationEntryPoint(MyAuthenticationEntryPoint()).accessDeniedHandler(MyAccessDeniedHandler())
            }
            .apply(authorizationServerConfigurer)
        return http.build()
    }

    /**
     * 客户端信息
     * 对应表：oauth2_registered_client
     */
    @Bean
    fun registeredClientRepository(jdbcTemplate: JdbcTemplate): RegisteredClientRepository {
        return JdbcRegisteredClientRepository(jdbcTemplate)
    }

    /**
     * 授权信息
     * 对应表：oauth2_authorization
     */
    @Bean
    fun authorizationService(
        jdbcTemplate: JdbcTemplate,
        registeredClientRepository: RegisteredClientRepository
    ): OAuth2AuthorizationService {
        return JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository)
    }

    /**
     * 授权确认
     * 对应表：oauth2_authorization_consent
     */
    @Bean
    fun authorizationConsentService(
        jdbcTemplate: JdbcTemplate,
        registeredClientRepository: RegisteredClientRepository
    ): OAuth2AuthorizationConsentService {
        return JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository)
    }

    /**
     * 密码加密
     *
     * @return
     */
    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    /**
     * jwt 加密密钥
     */
    @Bean
    fun jwkSource(): JWKSource<SecurityContext> {
        val keyStoreKeyFactory = KeyStoreKeyFactory(ClassPathResource("jwt.jks"), "admin123456".toCharArray())
        val keyPair = keyStoreKeyFactory.getKeyPair("jwt")
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey
        val rsaKey = RSAKey.Builder(publicKey)
            .privateKey(privateKey)
//            .keyID(UUID.randomUUID().toString())
            .build()
        val jwkSet = JWKSet(rsaKey)
        return ImmutableJWKSet(jwkSet)
    }

    /**
     * 配置jwt解析器
     */
    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)
    }

    /**
     * 配置认证服务器请求地址
     */
    @Bean
    fun authorizationServerSettings(): AuthorizationServerSettings {
        // 什么都不配置，则使用默认地址
        return AuthorizationServerSettings.builder().build()
    }

    /**
     * 配置token生成器
     */
    @Bean
    fun tokenGenerator(jwkSource: JWKSource<SecurityContext>): OAuth2TokenGenerator<*> {
        val jwtGenerator = JwtGenerator(NimbusJwtEncoder(jwkSource))
        jwtGenerator.setJwtCustomizer(jwtCustomizer())
        val accessTokenGenerator = OAuth2AccessTokenGenerator()
        val refreshTokenGenerator = OAuth2RefreshTokenGenerator()
        return DelegatingOAuth2TokenGenerator(
            jwtGenerator, accessTokenGenerator, refreshTokenGenerator
        )
    }

    @Bean
    fun jwtCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext> {
        return OAuth2TokenCustomizer<JwtEncodingContext> { context ->
            val claims = context.claims
            if (context.tokenType == OAuth2TokenType.ACCESS_TOKEN) {
                claims.claim(JwtClaimNames.JTI, UUID.randomUUID())
                // Customize headers/claims for access_token
                val principal = context.getPrincipal<Authentication>().principal
                if (principal is SysUserDetails) {
                    // 系统用户添加自定义字段
                    claims.claim(CoreConstant.USER_ID, principal.userInfoDTO.userId)
                    val roleSet = principal.authorities?.map(GrantedAuthority::getAuthority)?.toSet()
                    claims.claim(CoreConstant.AUTHORITIES_CLAIM_NAME_KEY, roleSet)
                    claims.claim(CoreConstant.DEPT_ID, principal.userInfoDTO.deptId)
                    claims.claim(CoreConstant.DATA_SCOPE, principal.userInfoDTO.dataScope?.value)
                } else if (principal is MemberUserDetails) {
                    // 会员用户添加自定义字段
                    claims.claim(CoreConstant.USER_ID, principal.userInfoDTO.userId)
                }
            } else if (context.tokenType.value == OidcParameterNames.ID_TOKEN) {
                // Customize headers/claims for id_token
                claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()))
                val standardSessionIdGenerator = StandardSessionIdGenerator()
                claims.claim("sid", standardSessionIdGenerator.generateSessionId())
            }
        }
    }
}