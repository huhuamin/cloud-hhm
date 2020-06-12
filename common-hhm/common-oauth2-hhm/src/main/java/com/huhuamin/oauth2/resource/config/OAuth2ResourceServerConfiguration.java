package com.huhuamin.oauth2.resource.config;


import com.huhuamin.oauth2.convert.ResourceServerCommonConvertUser;
import com.huhuamin.oauth2.jwt.JweTokenSerializer;
import com.huhuamin.oauth2.jwt.JweTokenStore;
import com.huhuamin.oauth2.properties.SecurityAuthProperties;
import com.huhuamin.oauth2.service.UserTokenServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

@Configuration
@EnableResourceServer
public class OAuth2ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
    /**
     * 安全配置信息
     */
    @Autowired
    private SecurityAuthProperties securityAuthProperties;

    /**
     * UserTokenServices 使用
     * <p>
     * 服务器自己解析accessToken
     *
     * @return
     */
    @Autowired
    private DefaultAccessTokenConverter defaultAccessTokenConverter;

    /**
     * JwtAccessTokenConverter
     */
    @Autowired
    private JwtAccessTokenConverter jwtAccessTokenConverter;
    /**
     * 配置jwe算法，jwt的信息加密，解密
     */
    @Autowired
    private TokenStore tokenStore;

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
        UserAuthenticationConverter userTokenConverter = new ResourceServerCommonConvertUser();
        defaultAccessTokenConverter.setUserTokenConverter(userTokenConverter);
        UserTokenServices userTokenServices = new UserTokenServices(tokenStore, defaultAccessTokenConverter, jwtAccessTokenConverter);
        resources.tokenStore(tokenStore()).tokenServices(userTokenServices);
    }

    //autowired 数据 初始化 bean start

    @Bean
    public JweTokenStore tokenStore() {
        return new JweTokenStore(securityAuthProperties.getSymmetricKey(),
                new JwtTokenStore(jwtTokenConverter()), jwtTokenConverter(), tokenSerializer());
    }

    @Bean
    public JweTokenSerializer tokenSerializer() {
        return new JweTokenSerializer(securityAuthProperties.getSymmetricKey());
    }

    @Bean
    public JwtAccessTokenConverter jwtTokenConverter() {
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setSigningKey(securityAuthProperties.getSymmetricKey());
        return converter;
    }

    @Bean
    DefaultAccessTokenConverter accessTokenConverter() {
        return new DefaultAccessTokenConverter();
    }
    //autowired 数据 初始化 bean end
}
