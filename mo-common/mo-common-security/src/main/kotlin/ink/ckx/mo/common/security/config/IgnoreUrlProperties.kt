package ink.ckx.mo.common.security.config

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@ConfigurationProperties(prefix = "security")
@Component
class IgnoreUrlProperties(val whitelistPaths: List<String>)