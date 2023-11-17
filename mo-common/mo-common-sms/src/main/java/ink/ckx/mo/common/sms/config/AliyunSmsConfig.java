package ink.ckx.mo.common.sms.config;

import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import darabonba.core.client.ClientOverrideConfiguration;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@ConfigurationProperties(prefix = "sms.aliyun")
@Component
@Data
public class AliyunSmsConfig {

    /**
     * 访问凭据
     */
    private String accessKeyId;

    /**
     * 访问密钥
     */
    private String accessKeySecret;

    /**
     * 服务 Endpoint
     */
    private String endpoint;

    /**
     * 地区
     */
    private String regionId;

    /**
     * 模板代码
     */
    private String templateCode;

    /**
     * 签名名称
     */
    private String signName;

    @Bean
    public AsyncClient asyncClient() {
        // 配置认证信息
        StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
                .accessKeyId(accessKeyId)
                .accessKeySecret(accessKeySecret)
                .build());
        // 配置客户端
        return AsyncClient.builder()
                .region(regionId)
                .credentialsProvider(provider)
                .overrideConfiguration(
                        ClientOverrideConfiguration.create()
                                // Endpoint 请参考 https://api.aliyun.com/product/Dysmsapi
                                .setEndpointOverride(endpoint)
                )
                .build();
    }
}
