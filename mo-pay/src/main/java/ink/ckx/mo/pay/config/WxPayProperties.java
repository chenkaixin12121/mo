package ink.ckx.mo.pay.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@ConfigurationProperties(prefix = "pay.wx")
@Component
@Data
public class WxPayProperties {

    private String qrcodeKey;

    private long qrcodeExpire;

    private String appId;

    private String merchantId;

    private String secrectKey;

    private String spbillCreateIp;

    private String notifyUrl;

    private String tradeType;

    private String placeOrderUrl;
}
