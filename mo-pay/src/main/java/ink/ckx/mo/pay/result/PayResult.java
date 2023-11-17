package ink.ckx.mo.pay.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Data
@Schema(description = "支付结果对象")
public class PayResult {

    @Schema(description = "返回状态码")
    private String return_code;

    @Schema(description = "公众账号 id")
    private String appid;

    @Schema(description = "商户号")
    private String mch_id;

    @Schema(description = "随机字符串")
    private String nonce_str;

    @Schema(description = "签名")
    private String sign;

    @Schema(description = "业务结果")
    private String result_code;

    @Schema(description = "用户标识")
    private String openid;

    @Schema(description = "交易类型")
    private String trade_type;

    @Schema(description = "付款银行")
    private String bank_type;

    @Schema(description = "总金额")
    private int total_fee;

    @Schema(description = "现金支付金额")
    private int cash_fee;

    @Schema(description = "微信支付订单号")
    private String transaction_id;

    @Schema(description = "商户订单号")
    private String out_trade_no;

    @Schema(description = "支付完成时间")
    private String time_end;

    @Schema(description = "返回信息")
    private String return_msg;

    @Schema(description = "设备号")
    private String device_info;

    @Schema(description = "错误代码")
    private String err_code;

    @Schema(description = "错误代码描述")
    private String err_code_des;

    @Schema(description = "是否关注公众账号")
    private String is_subscribe;

    @Schema(description = "货币种类")
    private String fee_type;

    @Schema(description = "现金支付货币类型")
    private String cash_fee_type;

    @Schema(description = "代金券或立减优惠金额")
    private String coupon_fee;

    @Schema(description = "代金券或立减优惠使用数量")
    private String coupon_count;

    @Schema(description = "代金券或立减优惠ID")
    private String coupon_id_$n;

    @Schema(description = "单个代金券或立减优惠支付金额")
    private String coupon_fee_$n;

    @Schema(description = "商家数据包")
    private String attach;
}