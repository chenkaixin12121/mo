package ink.ckx.mo.pay.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
public interface WxPayService {

    /**
     * 获取微信扫码图片地址
     *
     * @param orderNo 订单号
     * @param amount  金额
     * @return
     */
    String getWxPayQRCode(String orderNo, BigDecimal amount);

    /**
     * 支付成功后的微信异步通知
     *
     * @param request
     * @param response
     */
    void notice(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
