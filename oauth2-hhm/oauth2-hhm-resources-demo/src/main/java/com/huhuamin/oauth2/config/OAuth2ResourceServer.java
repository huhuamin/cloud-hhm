package com.huhuamin.oauth2.config;


import com.huhuamin.oauth2.jwt.JweTokenSerializer;
import com.huhuamin.oauth2.jwt.JweTokenStore;
import com.huhuamin.oauth2.properties.SecurityAuthProperties;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

@Configuration

@EnableResourceServer
public class OAuth2ResourceServer extends ResourceServerConfigurerAdapter {
    @Autowired
    private SecurityAuthProperties securityAuthProperties;
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .anyRequest().authenticated().and()
            .requestMatchers().antMatchers("/api/**");
    }


    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.tokenStore(tokenStore());
    }
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

}
