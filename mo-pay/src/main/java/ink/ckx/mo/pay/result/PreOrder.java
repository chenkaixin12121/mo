package ink.ckx.mo.pay.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Schema(description = "预付单对象")
@Data
public class PreOrder {

    @Schema(description = "公众账号 id")
    private String appid;

    @Schema(description = "商户号")
    private String mch_id;

    @Schema(description = "随机字符串")
    private String nonce_str;

    @Schema(description = "签名")
    private String sign;

    @Schema(description = "商品描述")
    private String body;

    @Schema(description = "商户订单号")
    private String out_trade_no;

    @Schema(description = "订单总金额，单位为分")
    private int total_fee;

    @Schema(description = "APP 和网页支付提交用户端 ip，Native 支付填调用微信支付 API 的机器 ip")
    private String spbill_create_ip;

    @Schema(description = "接收微信支付异步通知回调地址，通知 url 必须为直接可访问的 url，不能携带参数")
    private String notify_url;

    @Schema(description = "取值如下：JSAPI，NATIVE，APP")
    private String trade_type;
}