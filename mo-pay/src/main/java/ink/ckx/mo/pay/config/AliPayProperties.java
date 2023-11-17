package ink.ckx.mo.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@ConfigurationProperties(prefix = "pay.ali")
@Component
@Data
public class AliPayProperties {

    private String appId;

    private String merchantPrivateKey;

    private String alipayPublicKey;

    private String notifyUrl;

    private String returnUrl;

    private String signType;

    private String charset;

    private String gatewayUrl;
}