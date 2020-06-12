package com.huhuamin.oauth2.service;

import com.huhuamin.oauth2.convert.ResourceServerCommonConvertUser;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidTokenException;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.ResourceServerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

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

    private final JwtAccessTokenConverter jwtAccessTokenConverter;

    public UserTokenServices(TokenStore tokenStore, DefaultAccessTokenConverter defaultAccessTokenConverter, JwtAccessTokenConverter jwtAccessTokenConverter) {
        this.tokenStore = tokenStore;
        this.defaultAccessTokenConverter = defaultAccessTokenConverter;
        this.jwtAccessTokenConverter = jwtAccessTokenConverter;
    }

    @Override
    public OAuth2Authentication loadAuthentication(String accessToken) throws AuthenticationException, InvalidTokenException {
        OAuth2Authentication oAuth2Authentication = tokenStore.readAuthentication(accessToken);
        UserAuthenticationConverter userTokenConverter = new ResourceServerCommonConvertUser();
        defaultAccessTokenConverter.setUserTokenConverter(userTokenConverter);
        Map<String, ?> map = jwtAccessTokenConverter.convertAccessToken(readAccessToken(accessToken), oAuth2Authentication);
        return defaultAccessTokenConverter.extractAuthentication(map);
    }

    @Override
    public OAuth2AccessToken readAccessToken(String accessToken) {
        return tokenStore.readAccessToken(accessToken);
    }
}
