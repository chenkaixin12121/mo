package ink.ckx.mo.common.sms.config

import com.aliyun.auth.credentials.Credential
import com.aliyun.auth.credentials.provider.StaticCredentialProvider
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient
import darabonba.core.client.ClientOverrideConfiguration
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Configuration
class AliyunSmsConfig {

    @Bean
    @ConfigurationProperties(prefix = "sms.aliyun")
    fun aliyunSmsConfigProperties(): AliyunSmsConfigProperties {
        return AliyunSmsConfigProperties()
    }

    @Bean
    fun asyncClient(properties: AliyunSmsConfigProperties): AsyncClient {
        // 配置认证信息
        val provider = StaticCredentialProvider.create(
            Credential.builder()
                .accessKeyId(properties.accessKeyId)
                .accessKeySecret(properties.accessKeySecret)
                .build()
        )
        // 配置客户端
        return AsyncClient.builder()
            .region(properties.regionId)
            .credentialsProvider(provider)
            .overrideConfiguration(
                ClientOverrideConfiguration.create()
                    // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
                    .setEndpointOverride(properties.endpoint)
            )
            .build()
    }
}

data class AliyunSmsConfigProperties(

    /**
     * 访问凭据
     */
    var accessKeyId: String? = null,

    /**
     * 访问密钥
     */
    var accessKeySecret: String? = null,

    /**
     * 服务 Endpoint
     */
    var endpoint: String? = null,

    /**
     * 地区
     */
    var regionId: String? = null,

    /**
     * 模板代码
     */
    var templateCode: String? = null,

    /**
     * 签名名称
     */
    var signName: String? = null,
)