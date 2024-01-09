package ink.ckx.mo.common.redis.config

import cn.hutool.core.util.StrUtil
import org.redisson.Redisson
import org.redisson.api.RedissonClient
import org.redisson.config.Config
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/10
 */
@ConditionalOnProperty(prefix = "redisson", name = ["address"])
@Configuration
class RedissonConfig {

    @Bean
    @ConfigurationProperties(prefix = "redisson")
    fun redissonConfigProperties(): RedissonConfigProperties {
        return RedissonConfigProperties()
    }

    @Bean
    fun redissonClient(properties: RedissonConfigProperties): RedissonClient {
        val config = Config()
        val singleServerConfig = config.useSingleServer()
        singleServerConfig.address = properties.address
        singleServerConfig.database = properties.database
        singleServerConfig.connectionMinimumIdleSize = properties.minIdle
        if (StrUtil.isNotBlank(properties.password)) {
            singleServerConfig.password = properties.password
        }
        return Redisson.create(config)
    }
}

data class RedissonConfigProperties(
    var address: String = "redis://127.0.0.1:6379",
    var password: String = "123456",
    var database: Int = 0,
    var minIdle: Int = 1,
)