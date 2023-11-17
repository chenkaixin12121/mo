package ink.ckx.mo.pay.service;

import jakarta.servlet.http.HttpServletRequest;

import java.math.BigDecimal;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
public interface AliPayService {

    /**
     * 获取支付宝支付表单
     *
     * @param orderNo 订单号
     * @param amount  金额
     * @return
     */
    String getAliPayForm(String orderNo, BigDecimal amount);

    /**
     * 支付成功后的支付宝异步通知
     *
     * @param request
     */
    String notice(HttpServletRequest request);
}
