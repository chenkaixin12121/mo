package ink.ckx.mo.gateway.filter;

import ink.ckx.mo.common.core.constant.CoreConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @author chenkaixin
 * @description
 * @since 2023/11/12
 */

@Component
public class RequestGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // 移除请求头参数 from
        ServerHttpRequest request = exchange.getRequest().mutate().headers(httpHeaders -> {
            httpHeaders.remove(CoreConstant.FROM);
        }).build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return 1;
    }
}