package ink.ckx.mo.common.apidoc.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.OAuthFlow;
import io.swagger.v3.oas.models.security.OAuthFlows;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(ApiDocInfoProperties.class)
public class OpenApiConfig {

    /**
     * API 文档信息属性
     */
    private final ApiDocInfoProperties apiDocInfoProperties;

    /**
     * OAuth2 认证 endpoint
     */
    @Value("${spring.security.oauth2.authorizationserver.token-uri}")
    private String tokenUrl;

    /**
     * OpenAPI 配置（元信息、安全协议）
     */
    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes(HttpHeaders.AUTHORIZATION,
                                new SecurityScheme()
                                        // OAuth2 授权模式
                                        .type(SecurityScheme.Type.OAUTH2)
                                        .name(HttpHeaders.AUTHORIZATION)
                                        .flows(new OAuthFlows()
                                                .password(
                                                        new OAuthFlow()
                                                                .tokenUrl(tokenUrl)
                                                                .refreshUrl(tokenUrl)
                                                )
                                        )
                                        // 安全模式使用Bearer令牌（即JWT）
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("Bearer")
                                        .bearerFormat("JWT")

                        )
                )
                // 接口全局添加 Authorization 参数
                .addSecurityItem(new SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
                // 接口文档信息
                .info(new Info()
                        .title(apiDocInfoProperties.getTitle())
                        .version(apiDocInfoProperties.getVersion())
                        .description(apiDocInfoProperties.getDescription())
                        .contact(new Contact()
                                .name(apiDocInfoProperties.getContact().getName())
                                .url(apiDocInfoProperties.getContact().getUrl())
                                .email(apiDocInfoProperties.getContact().getEmail())
                        )
                        .license(new License().name(apiDocInfoProperties.getLicense().getName())
                                .url(apiDocInfoProperties.getLicense().getUrl())
                        ));
    }
}