package com.huhuamin.oauth2;

import com.huhuamin.oauth2.properties.SecurityAuthProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName : Oauth2ResoucesMain  //类名
 * @Description : 资源服务器  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-11 16:29  //时间
 */
@SpringBootApplication
public class Oauth2ResourceMain {
    public static void main(String[] args) {
        SpringApplication.run(Oauth2ResourceMain.class, args);
    }
    @Bean
    public SecurityAuthProperties securityAuthProperties(){
        return new SecurityAuthProperties();
    }

}
