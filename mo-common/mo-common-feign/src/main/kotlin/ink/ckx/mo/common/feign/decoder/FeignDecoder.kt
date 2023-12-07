package ink.ckx.mo.common.feign.decoder

import cn.hutool.http.HttpStatus
import feign.Response
import feign.codec.DecodeException
import feign.codec.Decoder
import ink.ckx.mo.common.core.result.Result
import ink.ckx.mo.common.core.result.Result.Companion.isSuccess
import org.springframework.cloud.openfeign.support.SpringDecoder
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
class FeignDecoder(private val decoder: SpringDecoder) : Decoder {

    override fun decode(response: Response, type: Type): Any? {

        val method = response.request().requestTemplate().methodMetadata().method()
        val notTheSame = method.returnType != Result::class.java
        if (notTheSame) {
            val newType: Type = object : ParameterizedType {
                override fun getActualTypeArguments(): Array<Type> {
                    return arrayOf(type)
                }

                override fun getRawType(): Type {
                    return Result::class.java
                }

                override fun getOwnerType(): Type? {
                    return null
                }
            }
            val result = decoder.decode(response, newType) as Result<*>
            return if (isSuccess(result)) {
                result.data
            } else {
                throw DecodeException(HttpStatus.HTTP_INTERNAL_ERROR, result.msg, response.request())
            }
        }
        return decoder.decode(response, type)
    }
}