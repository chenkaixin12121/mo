package ink.ckx.mo.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.pay.config.AliPayProperties;
import ink.ckx.mo.pay.service.AliPayService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class AliPayServiceImpl implements AliPayService {

    private final AliPayProperties aliPayProperties;

    @Override
    public String getAliPayForm(String orderNo, BigDecimal amount) {
        // 获得初始化的 AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                aliPayProperties.getGatewayUrl(),
                aliPayProperties.getAppId(),
                aliPayProperties.getMerchantPrivateKey(),
                "json",
                aliPayProperties.getCharset(),
                aliPayProperties.getAlipayPublicKey(),
                aliPayProperties.getSignType()
        );

        // 设置请求参数
        AlipayTradePagePayRequest aliPayRequest = new AlipayTradePagePayRequest();
        aliPayRequest.setReturnUrl(aliPayProperties.getReturnUrl());
        aliPayRequest.setNotifyUrl(aliPayProperties.getNotifyUrl());

        // 订单名称, 必填
        String subject = "mo-订单[" + orderNo + "]";
        // 该笔订单允许的最晚付款时间，逾期将关闭交易。取值范围：1m～15d。m-分钟，h-小时，d-天，1c-当天（1c-当天的情况下，无论交易何时创建，都在0点关闭）。 该参数数值不接受小数点， 如 1.5h，可转换为 90m。
//        String timeout_express = "1d";

        Map<String, String> bizContent = new HashMap<>();
        bizContent.put("out_trade_no", orderNo); // 商户订单号, 商户网站订单系统中唯一订单号, 必填
        bizContent.put("total_amount", amount.toString());
        bizContent.put("subject", subject);
        bizContent.put("body", subject); // 商品描述, 可空, 目前先用订单名称
//        bizContent.put("timeout_express", timeout_express);
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");

        aliPayRequest.setBizContent(JSONUtil.toJsonStr(bizContent));

        // 若想给BizContent增加其他可选请求参数, 以增加自定义超时时间参数timeout_express来举例说明
//        aliPayRequest.setBizContent("{\"out_trade_no\":\"" + merchantOrderId + "\","
//                + "\"total_amount\":\"" + total_amount + "\","
//                + "\"subject\":\"" + subject + "\","
//                + "\"body\":\"" + body + "\","
//                + "\"timeout_express\":\"10m\","
//                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");

        // 请求
        String aliPayForm = null;
        try {
            aliPayForm = alipayClient.pageExecute(aliPayRequest).getBody();
        } catch (AlipayApiException e) {
            log.error("请求支付宝支付出错：", e);
            throw new BusinessException(ResultCode.PAY_ERROR);
        }
        log.info("支付宝支付 - 前往支付页面, alipayForm: {}", aliPayForm);
        return aliPayForm;
    }

    @Override
    public String notice(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (String name : requestParams.keySet()) {
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
            }
            // 乱码解决，这段代码在出现乱码时使用
//			valueStr = new String(valueStr.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            params.put(name, valueStr);
        }

        boolean signVerified; // 调用 SDK 验证签名
        try {
            signVerified = AlipaySignature.rsaCheckV1(params,
                    aliPayProperties.getAlipayPublicKey(),
                    aliPayProperties.getCharset(),
                    aliPayProperties.getSignType());
        } catch (AlipayApiException e) {
            log.info("支付宝回调验签异常, 参数：{}", params);
            return "fail";
        }

        // 验签成功
        if (signVerified) {
            // 商户订单号
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 支付宝交易号
            String trade_no = new String(request.getParameter("trade_no").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 交易状态
            String trade_status = new String(request.getParameter("trade_status").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
            // 付款金额
            String total_amount = new String(request.getParameter("total_amount").getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

            if (trade_status.equals("TRADE_SUCCESS")) {
                // TODO 通知订单服务修改状态
            }

            log.info("************* 支付成功(支付宝异步通知) - 时间: {} *************", DateUtil.now());
            log.info("* 订单号: {}", out_trade_no);
            log.info("* 支付宝交易号: {}", trade_no);
            log.info("* 实付金额: {}", total_amount);
            log.info("* 交易状态: {}", trade_status);
            log.info("*****************************************************************************");

            return "success";
        }
        log.info("支付宝回调验签失败, 参数：{}", params);
        return "fail";
    }
}