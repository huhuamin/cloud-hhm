package com.huhuamin.autoconfig.config;

import com.huhuamin.autoconfig.config.bean.User;
import com.huhuamin.autoconfig.config.bean.UserFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName : HhmConfiguration  //类名
 * @Description : 自动装配  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-28 14:04  //时间
 */

@Configuration
public class HhmAutoConfiguration {

    @Bean
    public UserFactories userFactories() {
        return new UserFactories();
    }
}
