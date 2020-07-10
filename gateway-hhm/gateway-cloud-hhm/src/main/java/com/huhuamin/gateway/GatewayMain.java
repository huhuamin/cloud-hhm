package com.huhuamin.gateway;

import com.huhuamin.common.oauth2.properties.SecurityAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.config.GatewayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;


/**
 * @ClassName : GatewayMain  //类名
 * @Description : 网关启动类  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-15 19:47  //时间
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GatewayMain {
    public static void main(String[] args) {
        SpringApplication.run(GatewayMain.class, args);
    }

    @Bean
    @RefreshScope
    public SecurityAuthProperties securityAuthProperties() {
        return new SecurityAuthProperties();
    }

    /**
     * 自动更新配置信息
     *
     * @return
     */
    @Bean
    @Primary
    @RefreshScope
    public GatewayProperties gatewayProperties() {
        return new GatewayProperties();
    }

}
