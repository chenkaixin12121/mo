package ink.ckx.mo.common.oss.config;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@ConfigurationProperties(prefix = "oss.aliyun")
@Component
@Data
public class AliyunOssConfig {

    /**
     * 访问凭据
     */
    private String accessKeyId;

    /**
     * 凭据密钥
     */
    private String accessKeySecret;

    /**
     * 服务 Endpoint
     */
    private String endpoint;

    /**
     * 存储桶名称
     */
    private String bucketName;

    @Bean
    public OSS oSS() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }
}
