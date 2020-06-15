package com.huhuamin.common.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName : TokenController  //类名
 * @Description : 退出  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-11 15:08  //时间
 */
@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    private TokenStore tokenStore;
    @DeleteMapping("/logout")
    public String logout(@RequestParam(value = "token", required = false) String token)
    {
        if (StringUtils.isEmpty(token))
        {
            return "true";
        }

        OAuth2AccessToken accessToken = tokenStore.readAccessToken(token);
        if (accessToken == null || StringUtils.isEmpty(accessToken.getValue()))
        {
            return "true";
        }

        // 清空 access token
        tokenStore.removeAccessToken(accessToken);

        // 清空 refresh token
        OAuth2RefreshToken refreshToken = accessToken.getRefreshToken();
        tokenStore.removeRefreshToken(refreshToken);
        Map<String, ?> map = accessToken.getAdditionalInformation();

        return "true";
    }
}
