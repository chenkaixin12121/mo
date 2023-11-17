package ink.ckx.mo.common.sms.service.impl;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.exception.BusinessException;
import ink.ckx.mo.common.core.result.ResultCode;
import ink.ckx.mo.common.sms.config.AliyunSmsConfig;
import ink.ckx.mo.common.sms.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AliyunSmsServiceImpl implements SmsService {

    private final AliyunSmsConfig aliyunSmsConfig;

    private final StringRedisTemplate stringRedisTemplate;

    private final AsyncClient asyncClient;

    @Override
    public void sendSmsCode(String phoneNumber) {
        // 配置请求参数
        String code = RandomUtil.randomNumbers(6);

        Map<String, Object> params = new HashMap<>(1);
        params.put("code", code);
        SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
                .phoneNumbers(phoneNumber)
                .signName(aliyunSmsConfig.getSignName())
                .templateCode(aliyunSmsConfig.getTemplateCode())
                .templateParam(JSONUtil.toJsonStr(params))
                .build();
        // 发送请求
        CompletableFuture<SendSmsResponse> response = asyncClient.sendSms(sendSmsRequest);
        try {
            // 同步获取API请求的返回值
            SendSmsResponse resp = response.get();
            log.info("手机号：{}，发送短信成功，result : {}", phoneNumber, JSONUtil.toJsonStr(resp));
            // 短信验证码缓存
            stringRedisTemplate.opsForValue().set(CoreConstant.SMS_CODE_PREFIX + phoneNumber, code, CoreConstant.SMS_CODE_CACHE_EXPIRE, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException e) {
            log.error("手机号：{}，发送短信失败", phoneNumber, e);
            throw new BusinessException(ResultCode.SEND_SMS_CODE_ERROR);
        }
    }
}