package ink.ckx.mo.admin.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * 安全相关配置
 *
 * @author chenkaixin
 */
@Component
@ConfigurationProperties(prefix = "mo.security")
class MoSecurityProperties {

    /**
     * 新增用户默认密码（建议生产环境通过配置覆盖）
     */
    var defaultPassword: String = "123456"
}
