package com.star.sdagateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            // 获取实际转发的 URI
            URI requestUrl = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR);

            // Optional: 获取原始请求的 URI
            URI originalUri = exchange.getRequest().getURI();

            // 打印信息
            if (requestUrl != null) {
                System.out.println("Forwarding to URL: " + requestUrl);
            }
            if (originalUri != null) {
                System.out.println("Original Request URI: " + originalUri);
            }
        }));
    }

    @Override
    public int getOrder() {
        // 在路由决策之后执行
        return Ordered.LOWEST_PRECEDENCE;
    }
}
