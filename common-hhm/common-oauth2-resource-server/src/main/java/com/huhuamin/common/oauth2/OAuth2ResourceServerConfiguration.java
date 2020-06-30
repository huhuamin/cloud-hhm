package com.huhuamin.common.oauth2;


import com.huhuamin.common.oauth2.convert.ResourceConvertUser;
import com.huhuamin.common.oauth2.jwe.JweAccessTokenConverter;
import com.huhuamin.common.oauth2.jwe.JweTokenStore;
import com.huhuamin.common.oauth2.properties.SecurityAuthProperties;
import com.huhuamin.common.oauth2.service.UserTokenServices;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;


@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .requestMatchers().antMatchers("/api/**").antMatchers("/customer/**");
    }

    @Bean
    DefaultAccessTokenConverter defaultAccessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        /**
         * 每个用户都有一个自己的ResourceServerCommonConvertUser 转换器
         */
        DefaultAccessTokenConverter accessTokenConverter = defaultAccessTokenConverter();
        UserAuthenticationConverter userTokenConverter = new ResourceConvertUser();
        accessTokenConverter.setUserTokenConverter(userTokenConverter);
        UserTokenServices userTokenServices = new UserTokenServices(tokenStore(), accessTokenConverter,accessTokenConverter(securityAuthProperties()));
        resources.tokenStore(tokenStore()).tokenServices(userTokenServices);
    }

    //autowired 数据 初始化 bean start

    @Bean
    public SecurityAuthProperties securityAuthProperties() {
        return new SecurityAuthProperties();
    }

    /**
     * jwt+加强=jwe
     *
     * @return
     */
    @Bean
    public JweTokenStore tokenStore() {
        return new JweTokenStore(accessTokenConverter(securityAuthProperties()));
    }


    @Bean
    public JweAccessTokenConverter accessTokenConverter(SecurityAuthProperties securityAuthProperties) {
        JweAccessTokenConverter converter = new JweAccessTokenConverter();
        converter.setSigningKey(securityAuthProperties().getSymmetricKey());
        converter.setBase64EncodedKey(securityAuthProperties.getSymmetricKey());
        return converter;
    }

//    @Bean
//    DefaultAccessTokenConverter accessTokenConverter() {
//        return new DefaultAccessTokenConverter();
//    }

}
