package ink.ckx.mo.common.oss.config

import com.aliyun.oss.OSS
import com.aliyun.oss.OSSClientBuilder
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Configuration
class AliyunOssConfig {

    @Bean
    @ConfigurationProperties(prefix = "oss.aliyun")
    fun aliyunOssConfigProperties(): AliyunOssConfigProperties {
        return AliyunOssConfigProperties()
    }

    @Bean
    fun oSS(properties: AliyunOssConfigProperties): OSS {
        return OSSClientBuilder().build(properties.endpoint, properties.accessKeyId, properties.accessKeySecret)
    }
}

class AliyunOssConfigProperties {

    /**
     * 访问凭据
     */
    var accessKeyId: String? = null

    /**
     * 凭据密钥
     */
    var accessKeySecret: String? = null

    /**
     * 服务 Endpoint
     */
    var endpoint: String? = null

    /**
     * 存储桶名称
     */
    var bucketName: String? = null
}