package com.huhuamin.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * @ClassName : CloudGlobalFilter  //类名
 * @Description : 全局过滤器  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-23 15:11  //时间
 */
@Slf4j
@Component
public class CloudGlobalFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String uuid = UUID.randomUUID().toString();
        MDC.put("CloudTraceId", uuid);
        log.warn("test");
        ServerHttpRequest newRequest = request.mutate().header("CloudTraceId", uuid).build();
        return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());

    }

    @Override
    public int getOrder() {
        return 0;
    }
}
