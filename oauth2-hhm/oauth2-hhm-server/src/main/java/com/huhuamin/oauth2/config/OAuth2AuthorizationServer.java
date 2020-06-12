package com.huhuamin.oauth2.config;

import com.huhuamin.oauth2.jwt.JweTokenEnhancer;
import com.huhuamin.oauth2.jwt.JweTokenSerializer;
import com.huhuamin.oauth2.jwt.JweTokenStore;
import com.huhuamin.oauth2.properties.SecurityAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * @ClassName : OAuth2AuthorizationServer  //类名
 * @Description : 认证中心配置  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-10 14:57  //时间
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServer extends AuthorizationServerConfigurerAdapter {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private SecurityAuthProperties securityAuthProperties;
    @Autowired
    private PasswordEncoder passwordEncoder;




    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("jk").password(passwordEncoder.encode("jkjk")).roles("USER")
                .and()
                .withUser("admin").password(passwordEncoder.encode("admin123")).roles("ADMIN");
    }

    /* 授权服务器的安全配置*/
    @Override
    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                /*校验token需要认证通过，可采用http basic认证*/
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    /*设置认证请求相关参数，例如客户端账号和密码、令牌有效时间等等*/
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(securityAuthProperties.getClientId())/*客户端id*/
                .secret(passwordEncoder.encode(securityAuthProperties.getClientSecret()))/*密码，要保密*/
                .accessTokenValiditySeconds(60 * 60 * 4)/*访问令牌有效期4个小时*/
                .refreshTokenValiditySeconds(60 * 60 * 24)/*刷新令牌有效期24个小时*/
                /*授权客户端请求认证服务的类型authorization_code：根据授权码生成令牌，
                 client_credentials:客户端认证，refresh_token：刷新令牌，password：密码方式认证*/
                .authorizedGrantTypes("authorization_code", "client_credentials","refresh_token", "password").scopes("app").redirectUris("http://localhost");
//                .redirectUris("http://localhost")
//                .scopes("hhm");/*客户端范围，名称自定义，必填*/
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints
                .authenticationManager(authenticationManager).allowedTokenEndpointRequestMethods(HttpMethod.GET, HttpMethod.POST)
                .tokenStore(jweTokenStore(jwtAccessTokenConverter(securityAuthProperties)))
                .tokenEnhancer(tokenEnhancer(securityAuthProperties));
//                .accessTokenConverter(jwtAccessTokenConverter());//交给jwe处理
    }

    @Bean
    public TokenEnhancer tokenEnhancer(SecurityAuthProperties securityAuthProperties) {
        return new JweTokenEnhancer(jwtAccessTokenConverter(securityAuthProperties),
                new JweTokenSerializer(securityAuthProperties.getSymmetricKey()));
    }


    @Bean
    public JweTokenSerializer tokenSerializer() {
        return new JweTokenSerializer(securityAuthProperties.getSymmetricKey());
    }


    @Bean
    JweTokenStore jweTokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
        return new JweTokenStore(securityAuthProperties.getSymmetricKey(),
                new JwtTokenStore(jwtAccessTokenConverter), jwtAccessTokenConverter, tokenSerializer());
    }


    /**
     * 非对称密钥
     *
     * @return
     */
    @Bean
    public JwtAccessTokenConverter jwtAccessTokenConverter(SecurityAuthProperties securityAuthProperties) {
        /*访问证书路径*/
        ClassPathResource resource = new ClassPathResource(securityAuthProperties.getKeyLocation());
        /*密钥工厂*/
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource,
                //*密钥库密码*/
                securityAuthProperties.getKeystorePassword().toCharArray());
        /*密钥别名*/
        String alias = securityAuthProperties.getAlias();
        /*密钥库密码*/
        String keypassword = securityAuthProperties.getKeypassword();

        KeyPair keyPair = keyStoreKeyFactory.getKeyPair(alias, keypassword.toCharArray());
        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
        converter.setKeyPair(keyPair);
        return converter;
    }
}
