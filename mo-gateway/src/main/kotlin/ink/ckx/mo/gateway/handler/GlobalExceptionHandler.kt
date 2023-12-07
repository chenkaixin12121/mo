package ink.ckx.mo.gateway.handler

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import ink.ckx.mo.common.core.result.Result.Companion.fail
import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * @author chenkaixin
 * @description
 * @since 2023/12/07
 */
@Order(-1)
@Configuration
class GlobalExceptionHandler(
    private val objectMapper: ObjectMapper
) : ErrorWebExceptionHandler {

    private val log = KotlinLogging.logger {}

    override fun handle(exchange: ServerWebExchange, ex: Throwable): Mono<Void> {
        val response = exchange.response
        if (response.isCommitted) {
            return Mono.error(ex)
        }
        response.headers.contentType = MediaType.APPLICATION_JSON
        response.statusCode = HttpStatus.OK
        return response.writeWith(Mono.fromSupplier {
            val bufferFactory = response.bufferFactory()
            try {
                log.warn { "Error Spring Cloud Gateway : ${exchange.request.path} ${ex.message}" }
                return@fromSupplier bufferFactory.wrap(
                    objectMapper.writeValueAsBytes(fail<Any>(ex.message!!))
                )
            } catch (e: JsonProcessingException) {
                log.error(e) { "Error writing response" }
                return@fromSupplier bufferFactory.wrap(ByteArray(0))
            }
        })
    }
}