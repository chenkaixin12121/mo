package ink.ckx.mo.common.feign.config

import feign.codec.Decoder
import feign.optionals.OptionalDecoder
import ink.ckx.mo.common.feign.decoder.FeignDecoder
import org.springframework.beans.factory.ObjectProvider
import org.springframework.boot.autoconfigure.http.HttpMessageConverters
import org.springframework.cloud.openfeign.support.HttpMessageConverterCustomizer
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder
import org.springframework.cloud.openfeign.support.SpringDecoder
import org.springframework.context.annotation.Bean

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/11
 */
class FeignDecoderConfig {

    @Bean
    fun feignDecoder(
        messageConverters: ObjectProvider<HttpMessageConverters>,
        customizers: ObjectProvider<HttpMessageConverterCustomizer>
    ): Decoder {
        return OptionalDecoder(ResponseEntityDecoder(FeignDecoder(SpringDecoder(messageConverters, customizers))))
    }
}