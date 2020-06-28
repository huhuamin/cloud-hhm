package com.huhuamin.common.oauth2.service;

import com.huhuamin.common.oauth2.convert.ResourceConvertUser;
import com.huhuamin.common.oauth2.jwt.JweAccessTokenConverter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;

import java.util.Map;

/**
 * @ClassName : UserTokenServices  //类名
 * @Description : 用户转换服务  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 16:44  //时间
 */
public class UserTokenServices implements ResourceServerTokenServices {

    private final TokenStore tokenStore;

    private final DefaultAccessTokenConverter defaultAccessTokenConverter;

    private final JweAccessTokenConverter jweAccessTokenConverter;

    public UserTokenServices(TokenStore tokenStore, DefaultAccessTokenConverter defaultAccessTokenConverter, JweAccessTokenConverter jweAccessTokenConverter) {
        this.tokenStore = tokenStore;
        this.defaultAccessTokenConverter = defaultAccessTokenConverter;
        this.jweAccessTokenConverter = jweAccessTokenConverter;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
//        UserAuthenticationConverter userTokenConverter = new ResourceConvertUser();
//        defaultAccessTokenConverter.setUserTokenConverter(userTokenConverter);
//        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
//        Map<String, ?> map = jweAccessTokenConverter.convertAccessToken(readAccessToken(accessToken), oAuth2Authentication);
//        return jweAccessTokenConverter.extractAuthentication(map);


        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        UserAuthenticationConverter userTokenConverter = new ResourceConvertUser();
        defaultAccessTokenConverter.setUserTokenConverter(userTokenConverter);
        Map<String, ?> map = jweAccessTokenConverter.convertAccessToken(readAccessToken(accessToken), oAuth2Authentication);
        return defaultAccessTokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }
}
