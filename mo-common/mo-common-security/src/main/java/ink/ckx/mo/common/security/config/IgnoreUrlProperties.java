package ink.ckx.mo.common.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/23
 */
@Data
@ConfigurationProperties(prefix = "security")
@Component
public class IgnoreUrlProperties {

    private List<String> whitelistPaths;
}