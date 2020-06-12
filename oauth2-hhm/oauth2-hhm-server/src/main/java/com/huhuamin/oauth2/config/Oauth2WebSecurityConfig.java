package com.huhuamin.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.builders.WebSecurity;

/**
 * @ClassName : Oauth2WebSecurityConfig  //类名
 * @Description : web安全配置  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-10 14:57  //时间
 */
@Configuration
@EnableWebSecurity
class Oauth2WebSecurityConfig extends WebSecurityConfigurerAdapter {

//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        //用户登录认证、用户退出、用户获取jwt令牌
//        web.ignoring().antMatchers("/token","check_token", "/logout");
//    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        AuthenticationManager manager = super.authenticationManagerBean();
        return manager;
    }

    /**
     * 自定义账户系统
     * @return
     */
//    @Override
//    protected UserDetailsService userDetailsService() {
//        return userDetailsService;
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
//    }

    //采用bcrypt对密码进行编码 client
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(
                        "/actuator/**",
                        "/oauth/*",
                        "/token/**").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();

    }

}
