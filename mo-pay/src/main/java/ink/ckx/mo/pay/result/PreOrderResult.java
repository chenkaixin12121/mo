package ink.ckx.mo.pay.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Data
@Schema(description = "预付单返回对象")
public class PreOrderResult {

    @Schema(description = "返回状态码")
    private String return_code;

    @Schema(description = "返回信息")
    private String return_msg;

    @Schema(description = "公众账号ID")
    private String appid;

    @Schema(description = "商户号")
    private String mch_id;

    @Schema(description = "设备号")
    private String device_info;

    @Schema(description = "随机字符串")
    private String nonce_str;

    @Schema(description = "签名")
    private String sign;

    @Schema(description = "业务结果")
    private String result_code;

    @Schema(description = "错误代码")
    private String err_code;

    @Schema(description = "错误代码描述")
    private String err_code_des;

    @Schema(description = "交易类型")
    private String trade_type;

    @Schema(description = "预支付交易会话标识")
    private String prepay_id;

    @Schema(description = "二维码链接")
    private String code_url;
}