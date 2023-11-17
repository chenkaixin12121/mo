package ink.ckx.mo.auth.config;

import cn.hutool.core.lang.UUID;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import ink.ckx.mo.auth.authentication.captcha.CaptchaGrantAuthenticationConverter;
import ink.ckx.mo.auth.authentication.captcha.CaptchaGrantAuthenticationProvider;
import ink.ckx.mo.auth.authentication.mobile.MobileGrantAuthenticationConverter;
import ink.ckx.mo.auth.authentication.mobile.MobileGrantAuthenticationProvider;
import ink.ckx.mo.auth.authentication.password.PasswordGrantAuthenticationConverter;
import ink.ckx.mo.auth.authentication.password.PasswordGrantAuthenticationProvider;
import ink.ckx.mo.auth.filter.MyExceptionTranslationFilter;
import ink.ckx.mo.auth.handler.MyAccessDeniedHandler;
import ink.ckx.mo.auth.handler.MyAuthenticationEntryPoint;
import ink.ckx.mo.auth.handler.MyAuthenticationFailureHandler;
import ink.ckx.mo.auth.handler.MyAuthenticationSuccessHandler;
import ink.ckx.mo.auth.userdetails.member.MemberUserDetails;
import ink.ckx.mo.auth.userdetails.member.MemberUserDetailsService;
import ink.ckx.mo.auth.userdetails.user.SysUserDetails;
import ink.ckx.mo.auth.userdetails.user.SysUserDetailsService;
import ink.ckx.mo.auth.util.KeyStoreKeyFactory;
import ink.ckx.mo.common.core.constant.CoreConstant;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.IdTokenClaimNames;
import org.springframework.security.oauth2.core.oidc.endpoint.OidcParameterNames;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.*;
import org.springframework.security.oauth2.server.authorization.client.JdbcRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/02
 */
@RequiredArgsConstructor
@Configuration
public class AuthorizationServerConfig {

    private final MemberUserDetailsService memberUserDetailsService;

    private final SysUserDetailsService sysUserDetailsService;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * Spring Authorization Server 相关配置
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      OAuth2TokenGenerator<?> tokenGenerator) throws Exception {
        OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();

        authorizationServerConfigurer
                // 设置自定义密码模式
                .tokenEndpoint(tokenEndpoint ->
                        tokenEndpoint
                                // 自定义授权模式转换器
                                .accessTokenRequestConverters(
                                        authenticationConverters -> authenticationConverters.addAll(
                                                List.of(new PasswordGrantAuthenticationConverter(),
                                                        new MobileGrantAuthenticationConverter(),
                                                        new CaptchaGrantAuthenticationConverter())))
                                // 自定义授权模式提供者
                                .authenticationProviders(
                                        authenticationProviders ->
                                                authenticationProviders.addAll(
                                                        List.of(new PasswordGrantAuthenticationProvider(authorizationService, tokenGenerator, sysUserDetailsService, passwordEncoder()),
                                                                new MobileGrantAuthenticationProvider(authorizationService, tokenGenerator, memberUserDetailsService, redisTemplate),
                                                                new CaptchaGrantAuthenticationProvider(authorizationService, tokenGenerator, sysUserDetailsService, redisTemplate, passwordEncoder())))
                                )
                                // 自定义成功与失败响应
                                .errorResponseHandler(new MyAuthenticationFailureHandler())
                                .accessTokenResponseHandler(new MyAuthenticationSuccessHandler())
                )
                // 配置客户端认证处理器
                .clientAuthentication(clientAuthentication -> {
                    clientAuthentication.errorResponseHandler(new MyAuthenticationFailureHandler());
                })
                // 开启 OpenID Connect
                .oidc(Customizer.withDefaults());

        http.addFilterBefore(new MyExceptionTranslationFilter(), ExceptionTranslationFilter.class)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .csrf(CsrfConfigurer::disable)
                .exceptionHandling((exceptionHand -> {
                    exceptionHand
                            .authenticationEntryPoint(new MyAuthenticationEntryPoint())
                            .accessDeniedHandler(new MyAccessDeniedHandler());
                }))
                .apply(authorizationServerConfigurer);

        return http.build();
    }

    /**
     * 客户端信息
     * 对应表：oauth2_registered_client
     */
    @Bean
    public RegisteredClientRepository registeredClientRepository(JdbcTemplate jdbcTemplate) {
        return new JdbcRegisteredClientRepository(jdbcTemplate);
    }

    /**
     * 授权信息
     * 对应表：oauth2_authorization
     */
    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 授权确认
     * 对应表：oauth2_authorization_consent
     */
    @Bean
    public OAuth2AuthorizationConsentService authorizationConsentService(JdbcTemplate jdbcTemplate, RegisteredClientRepository registeredClientRepository) {
        return new JdbcOAuth2AuthorizationConsentService(jdbcTemplate, registeredClientRepository);
    }

    /**
     * 密码加密
     *
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * jwt 加密密钥
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("jwt.jks"), "admin123456".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("jwt");
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
//                .keyID(UUID.randomUUID().toString())
                .build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * 配置jwt解析器
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * 配置认证服务器请求地址
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        // 什么都不配置，则使用默认地址
        return AuthorizationServerSettings.builder().build();
    }

    /**
     * 配置token生成器
     */
    @Bean
    public OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
        JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
        jwtGenerator.setJwtCustomizer(jwtCustomizer());
        OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
        OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
        return new DelegatingOAuth2TokenGenerator(
                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            JwtClaimsSet.Builder claims = context.getClaims();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                claims.claim(JwtClaimNames.JTI, UUID.randomUUID());
                // Customize headers/claims for access_token
                Optional.ofNullable(context.getPrincipal().getPrincipal()).ifPresent(principal -> {
                    if (principal instanceof SysUserDetails userDetails) {
                        // 系统用户添加自定义字段
                        claims.claim(CoreConstant.USER_ID, userDetails.getUserId());
                        Set<String> roleSet = userDetails.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority).collect(Collectors.toSet());
                        claims.claim(CoreConstant.AUTHORITIES_CLAIM_NAME_KEY, roleSet);
                        claims.claim(CoreConstant.DEPT_ID, userDetails.getDeptId());
                        claims.claim(CoreConstant.DATA_SCOPE, userDetails.getDataScope());
                    } else if (principal instanceof MemberUserDetails userDetails) {
                        // 会员用户添加自定义字段
                        claims.claim(CoreConstant.USER_ID, userDetails.getUserId());
                    }
                });
            } else if (context.getTokenType().getValue().equals(OidcParameterNames.ID_TOKEN)) {
                // Customize headers/claims for id_token
                claims.claim(IdTokenClaimNames.AUTH_TIME, Date.from(Instant.now()));
                StandardSessionIdGenerator standardSessionIdGenerator = new StandardSessionIdGenerator();
                claims.claim("sid", standardSessionIdGenerator.generateSessionId());
            }
        };
    }
}