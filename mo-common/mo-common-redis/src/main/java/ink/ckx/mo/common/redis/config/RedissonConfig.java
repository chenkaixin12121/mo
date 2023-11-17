package ink.ckx.mo.common.redis.config;

import cn.hutool.core.text.CharSequenceUtil;
import lombok.Setter;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/10
 */
@ConditionalOnProperty(prefix = "redisson", name = "address")
@ConfigurationProperties(prefix = "redisson")
@Configuration
@Setter
public class RedissonConfig {

    private String address;

    private String password;

    private Integer database;

    private Integer minIdle;

    @Bean
    public RedissonClient redissonClient() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress(address);
        singleServerConfig.setDatabase(database);
        singleServerConfig.setConnectionMinimumIdleSize(minIdle);
        if (CharSequenceUtil.isNotBlank(password)) {
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }
}