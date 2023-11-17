package ink.ckx.mo.pay.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.security.util.SecurityUtil;
import ink.ckx.mo.pay.config.WxPayProperties;
import ink.ckx.mo.pay.result.PayResult;
import ink.ckx.mo.pay.result.PreOrder;
import ink.ckx.mo.pay.result.PreOrderResult;
import ink.ckx.mo.pay.service.WxPayService;
import ink.ckx.mo.pay.util.Sign;
import ink.ckx.mo.pay.util.XmlUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/16
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class WxPayServiceImpl implements WxPayService {

    private final WxPayProperties wxPayProperties;

    private final RedisTemplate<String, String> redisTemplate;

    @Override
    public String getWxPayQRCode(String orderNo, BigDecimal amount) {
        Long userId = SecurityUtil.getUserId();
        // 商品描述
        String body = "mo-付款用户[" + userId + "]";
        // 商户订单号 从 redis 中去获得这笔订单的微信支付二维码，如果订单状态没有支付没有就放入，这样的做法防止用户频繁刷新而调用微信接口
        String qrCodeUrl = redisTemplate.opsForValue().get(wxPayProperties.getQrcodeKey() + orderNo);
        if (StrUtil.isBlank(qrCodeUrl)) {
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                throw new BusinessException(ResultCode.PAY_ERROR, "金额不能小于等于 0");
            }
            // 订单总金额，单位为分
            int total_fee = amount.setScale(2, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).intValue();
            PreOrderResult preOrderResult = this.placeOrder(body, orderNo, total_fee);
            if (StrUtil.equals(preOrderResult.getReturn_code(), "FAIL")) {
                throw new BusinessException(ResultCode.PAY_ERROR, preOrderResult.getReturn_msg());
            }
            qrCodeUrl = preOrderResult.getCode_url();
        }
        redisTemplate.opsForValue().set(wxPayProperties.getQrcodeKey() + orderNo, qrCodeUrl, wxPayProperties.getQrcodeExpire());
        return qrCodeUrl;
    }

    @Override
    public void notice(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 获取微信支付结果
        PayResult payResult = this.getWxPayResult(request.getInputStream());

        boolean isPaid = payResult.getReturn_code().equals("SUCCESS");
        // 查询该笔订单在微信那边是否成功支付
        // 支付成功，商户处理后同步返回给微信参数
        PrintWriter writer = response.getWriter();
        if (isPaid) {

            String merchantOrderId = payResult.getOut_trade_no();            // 商户订单号
            String wxFlowId = payResult.getTransaction_id();
            Integer paidAmount = payResult.getTotal_fee();

            log.info("************* 支付成功(微信支付异步通知) - 时间: {} *************", DateUtil.now());
            log.info("* 商户订单号: {}", merchantOrderId);
            log.info("* 微信订单号: {}", wxFlowId);
            log.info("* 实际支付金额: {}", paidAmount);
            log.info("*****************************************************************************");

            // TODO 通知订单服务已支付

            // 通知微信已经收到消息，不要再给我发消息了，否则微信会10连击调用本接口
            String noticeStr = this.setXML("SUCCESS", "");
            writer.write(noticeStr);
        } else {
            log.info("================================= 支付失败 =================================");
            String noticeStr = this.setXML("FAIL", "");
            writer.write(noticeStr);
        }
        writer.flush();
    }

    /**
     * 统一下单
     * ==========================================
     * 微信预付单：指的是在自己的平台需要和微信进行支付交易生成的一个微信订单，称之为“预付单”
     * 订单：指的是自己的网站平台与用户之间交易生成的订单
     * <p>
     * 1. 用户购买商品 --> 生成网站订单
     * 2. 用户支付 --> 网站在微信平台生成预付单
     * 3. 最终实际根据预付单的信息进行支付
     * ==========================================
     */
    public PreOrderResult placeOrder(String body, String out_trade_no, Integer total_fee) {
        // 生成预付单对象
        PreOrder preOrder = new PreOrder();
        preOrder.setAppid(wxPayProperties.getAppId());
        preOrder.setBody(body);
        preOrder.setMch_id(wxPayProperties.getMerchantId());
        preOrder.setNotify_url(wxPayProperties.getNotifyUrl());
        preOrder.setOut_trade_no(out_trade_no);
        preOrder.setTotal_fee(total_fee);
        String nonce_str = IdUtil.fastSimpleUUID();
        preOrder.setNonce_str(nonce_str);
        preOrder.setTrade_type(wxPayProperties.getTradeType());
        preOrder.setSpbill_create_ip(wxPayProperties.getSpbillCreateIp());

        SortedMap<Object, Object> parameters = new TreeMap<>();
        parameters.put("appid", wxPayProperties.getAppId());
        parameters.put("mch_id", wxPayProperties.getMerchantId());
        parameters.put("body", body);
        parameters.put("nonce_str", nonce_str);
        parameters.put("out_trade_no", out_trade_no);
        parameters.put("total_fee", total_fee);
        parameters.put("spbill_create_ip", wxPayProperties.getSpbillCreateIp());
        parameters.put("notify_url", wxPayProperties.getNotifyUrl());
        parameters.put("trade_type", wxPayProperties.getTradeType());
        // 获得签名
        String sign = Sign.createSign("utf-8", parameters, wxPayProperties.getSecrectKey());
        preOrder.setSign(sign);
        // Object 转换为 XML
        String xml = XmlUtil.object2Xml(preOrder, PreOrder.class);
        // 统一下单地址
        String url = wxPayProperties.getPlaceOrderUrl();
        // 调用微信统一下单地址
        String returnXml = HttpUtil.post(url, xml);
        // XML 转换为 Object
        return (PreOrderResult) XmlUtil.xml2Object(returnXml, PreOrderResult.class);
    }

    /**
     * 获取支付结果
     *
     * @param inStream
     * @return
     */
    public PayResult getWxPayResult(InputStream inStream) throws IOException {
        StringBuilder result = new StringBuilder();
        BufferedReader in = new BufferedReader(new InputStreamReader(inStream));
        String line;
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        return (PayResult) XmlUtil.xml2Object(result.toString(), PayResult.class);
    }

    private String setXML(String return_code, String return_msg) {
        return "<xml><return_code><![CDATA[" + return_code + "]]></return_code><return_msg><![CDATA[" + return_msg + "]]></return_msg></xml>";
    }
}