package com.huhuamin.common.oauth2;


import com.huhuamin.common.oauth2.convert.ResourceConvertUser;
import com.huhuamin.common.oauth2.jwt.JweTokenStore;
import com.huhuamin.common.oauth2.jwt.JweTokenSerializer;
import com.huhuamin.common.oauth2.properties.SecurityAuthProperties;
import com.huhuamin.common.oauth2.service.UserTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {


    /**
     * UserTokenServices 使用
     * <p>
     * 服务器自己解析accessToken
     *
     * @return
     */
    @Autowired
    private DefaultAccessTokenConverter defaultAccessTokenConverter;



    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated().and()
                .requestMatchers().antMatchers("/api/**");
    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        /**
         * 每个用户都有一个自己的ResourceServerCommonConvertUser 转换器
         */
        UserAuthenticationConverter userTokenConverter = new ResourceConvertUser();
        defaultAccessTokenConverter.setUserTokenConverter(userTokenConverter);
        UserTokenServices userTokenServices = new UserTokenServices(tokenStore(), defaultAccessTokenConverter, jwtAccessTokenConverter());
        resources.tokenStore(tokenStore()).tokenServices(userTokenServices);
    }

    //autowired 数据 初始化 bean start

    @Bean
    public SecurityAuthProperties securityAuthProperties(){
        return new SecurityAuthProperties();
    }

    /**
     * jwt+加强=jwe
     *
     * @return
     */
    @Bean
    public JweTokenStore tokenStore() {
        return new JweTokenStore(securityAuthProperties().getSymmetricKey(),
                new JwtTokenStore(jwtAccessTokenConverter()), jwtAccessTokenConverter(), tokenSerializer());
    }

    /**
     * jwt json 加密 解密
     *
     * @return
     */
    @Bean
    public JweTokenSerializer tokenSerializer() {
        return new JweTokenSerializer(securityAuthProperties().getSymmetricKey());
    }

    /**
     * jwe +jwt +秘钥 存储
     *
     * @param jwtAccessTokenConverter
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(securityAuthProperties().getSymmetricKey());
        return converter;
    }

    @Bean
    DefaultAccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }
    //autowired 数据 初始化 bean end
}
