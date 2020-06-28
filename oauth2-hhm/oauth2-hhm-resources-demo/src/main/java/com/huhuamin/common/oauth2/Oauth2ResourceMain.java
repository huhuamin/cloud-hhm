package com.huhuamin.common.oauth2;


import com.huhuamin.autoconfig.config.annotation.EnableHhmConfiguration;
import com.huhuamin.autoconfig.config.bean.User;
import com.huhuamin.autoconfig.config.bean.UserFactories;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;


/**
 * @ClassName : Oauth2ResoucesMain  //类名
 * @Description : 资源服务器  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-11 16:29  //时间
 */
@SpringBootApplication
@EnableHhmConfiguration
public class Oauth2ResourceMain {
    public static void main(String[] args) {
        ConfigurableApplicationContext ca=SpringApplication.run(Oauth2ResourceMain.class, args);
        System.out.println(ca.getBean(User.class));
        System.out.println(ca.getBean(UserFactories.class));
    }


}
