package com.huhuamin.gateway.filter;


import com.alibaba.nacos.client.identify.Base64;
import com.huhuamin.common.oauth2.properties.SecurityAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;


/**
 * @ClassName : BasicAuthFilter  //类名
 * @Description : 加入Basic认证信息头，网页端不要传  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-15 20:25  //时间
 */
@Component
public class BasicAuthFilter extends AbstractGatewayFilterFactory<Object> {

    @Autowired
    private SecurityAuthProperties securityAuthProperties;


    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            String clientCredentials=securityAuthProperties.getClientId() + ":" + securityAuthProperties.getClientSecret();
            //basic auth
            String base64ClientCredentials = new String(Base64.encodeBase64(clientCredentials.getBytes()));

            ServerHttpRequest newRequest = request.mutate().header("Authorization", "Basic " + base64ClientCredentials).build();
            return chain.filter(exchange.mutate().request(newRequest.mutate().build()).build());
        };
    }
}
