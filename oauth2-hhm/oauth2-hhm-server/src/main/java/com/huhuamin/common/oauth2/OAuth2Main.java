package com.huhuamin.common.oauth2;

import com.huhuamin.common.oauth2.properties.SecurityAuthProperties;
import com.huhuamin.common.oauth2.service.UserDetailsServiceImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName : AuthMain  //类名
 * @Description : 认证中心启动类  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-10 14:32  //时间
 */
@EnableDiscoveryClient
@SpringBootApplication
public class OAuth2Main {
    public static void main(String[] args) {
        SpringApplication.run(OAuth2Main.class, args);
    }

    @Bean
    public SecurityAuthProperties securityAuthProperties() {
        return new SecurityAuthProperties();
    }

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return new UserDetailsServiceImpl();
    }


}
