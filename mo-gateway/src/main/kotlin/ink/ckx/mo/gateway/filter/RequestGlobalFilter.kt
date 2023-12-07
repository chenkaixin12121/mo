package ink.ckx.mo.gateway.filter

import ink.ckx.mo.common.core.constant.CoreConstant
import org.springframework.cloud.gateway.filter.GatewayFilterChain
import org.springframework.cloud.gateway.filter.GlobalFilter
import org.springframework.core.Ordered
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */
@Component
class RequestGlobalFilter : GlobalFilter, Ordered {

    override fun filter(exchange: ServerWebExchange, chain: GatewayFilterChain): Mono<Void> {
        // 移除请求头参数 from
        val request = exchange.request.mutate().headers { it.remove(CoreConstant.FROM) }.build()
        return chain.filter(exchange.mutate().request(request).build())
    }

    override fun getOrder(): Int {
        return 1
    }
}