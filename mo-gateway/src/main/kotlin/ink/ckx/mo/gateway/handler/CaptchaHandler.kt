package ink.ckx.mo.gateway.handler

import cn.hutool.captcha.GifCaptcha
import cn.hutool.captcha.generator.MathGenerator
import cn.hutool.core.util.IdUtil
import ink.ckx.mo.common.core.constant.CoreConstant
import ink.ckx.mo.common.core.result.Result.Companion.success
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.concurrent.TimeUnit

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/13
 */
@Component
class CaptchaHandler(
    private val redisTemplate: StringRedisTemplate
) : HandlerFunction<ServerResponse> {

    override fun handle(request: ServerRequest): Mono<ServerResponse> {
        val mathGenerator = MathGenerator(1)
        val gifCaptcha = GifCaptcha(110, 35, 3)
        gifCaptcha.generator = mathGenerator
        // 验证码
        val captchaCode = gifCaptcha.code
        // 验证码图片Base64
        val captchaBase64 = gifCaptcha.imageBase64Data

        // 验证码文本缓存至Redis，用于登录校验
        val verifyCodeKey = IdUtil.fastSimpleUUID()
        redisTemplate.opsForValue()[CoreConstant.VERIFY_CODE_CACHE_KEY_PREFIX + verifyCodeKey, captchaCode, CoreConstant.VERIFY_CODE_CACHE_EXPIRE.toLong()] =
            TimeUnit.SECONDS
        val result: MutableMap<String, String> = HashMap(2)
        result["verifyCodeKey"] = verifyCodeKey
        result["captchaImgBase64"] = captchaBase64
        return ServerResponse.ok().body(BodyInserters.fromValue(success<Map<String, String>>(result)))
    }
}