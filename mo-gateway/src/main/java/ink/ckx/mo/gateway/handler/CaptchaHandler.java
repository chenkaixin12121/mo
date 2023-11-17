package ink.ckx.mo.gateway.handler;

import cn.hutool.captcha.CircleCaptcha;
import cn.hutool.captcha.GifCaptcha;
import cn.hutool.captcha.generator.MathGenerator;
import cn.hutool.captcha.generator.RandomGenerator;
import cn.hutool.core.util.IdUtil;
import ink.ckx.mo.common.core.constant.CoreConstant;
import ink.ckx.mo.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/13
 */
@Component
@RequiredArgsConstructor
public class CaptchaHandler implements HandlerFunction<ServerResponse> {

    private final StringRedisTemplate redisTemplate;

    @Override
    public Mono<ServerResponse> handle(ServerRequest request) {
        MathGenerator mathGenerator = new MathGenerator(1);
        GifCaptcha gifCaptcha = new GifCaptcha(110, 35, 3);
        gifCaptcha.setGenerator(mathGenerator);
        // 验证码
        String captchaCode = gifCaptcha.getCode();
        // 验证码图片Base64
        String captchaBase64 = gifCaptcha.getImageBase64Data();

        // 验证码文本缓存至Redis，用于登录校验
        String verifyCodeKey = IdUtil.fastSimpleUUID();
        redisTemplate.opsForValue().set(CoreConstant.VERIFY_CODE_CACHE_KEY_PREFIX + verifyCodeKey, captchaCode,
                CoreConstant.VERIFY_CODE_CACHE_EXPIRE, TimeUnit.SECONDS);

        Map<String, String> result = new HashMap<>(2);
        result.put("verifyCodeKey", verifyCodeKey);
        result.put("verifyCodeBase64", captchaBase64);

        return ServerResponse.ok().body(BodyInserters.fromValue(Result.success(result)));
    }
}