package ink.ckx.mo.common.sms.service.impl

import cn.hutool.core.util.RandomUtil
import cn.hutool.json.JSONUtil
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.exception.BusinessException
import ink.ckx.mo.common.core.result.ResultCode
import ink.ckx.mo.common.sms.config.AliyunSmsConfigProperties
import ink.ckx.mo.common.sms.service.SmsService
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Service
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/14
 */
@Service
class AliyunSmsServiceImpl(
    private val aliyunSmsConfigProperties: AliyunSmsConfigProperties,
    private val stringRedisTemplate: StringRedisTemplate,
    private val asyncClient: AsyncClient,
) : SmsService {

    private val log = KotlinLogging.logger {}

    override fun sendSmsCode(phoneNumber: String) {
        // 配置请求参数
        val code = RandomUtil.randomNumbers(6)
        val params: MutableMap<String, Any> = HashMap(1)
        params["code"] = code
        val sendSmsRequest = SendSmsRequest.builder()
            .phoneNumbers(phoneNumber)
            .signName(aliyunSmsConfigProperties.signName)
            .templateCode(aliyunSmsConfigProperties.templateCode)
            .templateParam(JSONUtil.toJsonStr(params))
            .build()
        // 发送请求
        val response = asyncClient.sendSms(sendSmsRequest)
        try {
            // 同步获取API请求的返回值
            val resp = response.get()
            log.info { "手机号：$phoneNumber，发送短信成功，result : ${JSONUtil.toJsonStr(resp)}" }
            // 短信验证码缓存
            stringRedisTemplate.opsForValue()[CoreConstant.SMS_CODE_PREFIX + phoneNumber, code, CoreConstant.SMS_CODE_CACHE_EXPIRE.toLong()] =
                TimeUnit.SECONDS
        } catch (e: InterruptedException) {
            log.error(e) { "手机号：$phoneNumber，发送短信失败" }
            throw BusinessException(ResultCode.SEND_SMS_CODE_ERROR)
        } catch (e: ExecutionException) {
            log.error(e) { "手机号：$phoneNumber，发送短信失败" }
            throw BusinessException(ResultCode.SEND_SMS_CODE_ERROR)
        }
    }
}