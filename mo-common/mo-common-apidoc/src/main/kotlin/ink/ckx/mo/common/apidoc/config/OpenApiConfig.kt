package ink.ckx.mo.common.apidoc.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import io.swagger.v3.oas.models.security.OAuthFlow
import io.swagger.v3.oas.models.security.OAuthFlows
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
@Configuration
class OpenApiConfig {

    /**
     * OAuth2 认证 endpoint
     */
    @Value("\${spring.security.oauth2.authorizationserver.token-uri}")
    private val tokenUrl: String? = null

    @Bean
    @ConfigurationProperties(prefix = "springdoc.info")
    fun apiDocInfoProperties(): ApiDocInfoProperties {
        return ApiDocInfoProperties()
    }

    /**
     * OpenAPI 配置（元信息、安全协议）
     */
    @Bean
    fun apiInfo(properties: ApiDocInfoProperties): OpenAPI {
        return OpenAPI()
            .components(
                Components()
                    .addSecuritySchemes(
                        HttpHeaders.AUTHORIZATION,
                        SecurityScheme()
                            // OAuth2 授权模式
                            .type(SecurityScheme.Type.OAUTH2)
                            .name(HttpHeaders.AUTHORIZATION)
                            .flows(
                                OAuthFlows()
                                    .password(
                                        OAuthFlow()
                                            .tokenUrl(tokenUrl)
                                            .refreshUrl(tokenUrl)
                                    )
                            )
                            // 安全模式使用Bearer令牌（即JWT）
                            .`in`(SecurityScheme.In.HEADER)
                            .scheme("Bearer")
                            .bearerFormat("JWT")
                    )
            )
            // 接口全局添加 Authorization 参数
            .addSecurityItem(SecurityRequirement().addList(HttpHeaders.AUTHORIZATION))
            // 接口文档信息
            .info(
                Info()
                    .title(properties.title)
                    .version(properties.version)
                    .description(properties.description)
                    .contact(
                        Contact()
                            .name("chenkaixin12121")
                            .url("https://blog.ckx.ink")
                            .email("chenkaixin12121@163.com")
                    )
                    .license(
                        License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0.html")
                    )
            )
    }
}

class ApiDocInfoProperties {

    /**
     * API文档标题
     */
    var title: String? = null

    /**
     * API文档版本
     */
    var version: String? = null

    /**
     * API文档描述
     */
    var description: String? = null
}