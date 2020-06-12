package com.huhuamin.common.oauth2.jwt;


import com.huhuamin.common.oauth2.common.core.constants.HSecurityConstants;
import com.huhuamin.common.oauth2.model.UserJwt;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.util.JsonParser;
import org.springframework.security.oauth2.common.util.JsonParserFactory;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.AccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 使用jwe 加强 jwt 是jwt的长token  无法被解析
 */
public class JweTokenEnhancer implements TokenEnhancer {
    public static final String TOKEN_ID = AccessTokenConverter.JTI;

    private JsonParser objectMapper = JsonParserFactory.create();
    private AccessTokenConverter tokenConverter;
    private JweTokenSerializer tokenSerializer;

    public JweTokenEnhancer(AccessTokenConverter tokenConverter,
                            JweTokenSerializer tokenSerializer) {
        this.tokenConverter = tokenConverter;
        this.tokenSerializer = tokenSerializer;
    }

    /**
     * jwt json 加密配置，额外的用户信息加入
     *
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken result = new DefaultOAuth2AccessToken(accessToken);
        UserJwt user = (UserJwt) authentication.getUserAuthentication().getPrincipal();
        Map<String, Object> info = new LinkedHashMap<>(accessToken.getAdditionalInformation());
        info.put(HSecurityConstants.DETAILS_USER_ID, user.getUserId());
        info.put(HSecurityConstants.DETAILS_USERNAME, user.getUsername());
        String tokenId = result.getValue();
        if (!info.containsKey(TOKEN_ID)) {
            info.put(TOKEN_ID, tokenId);
        }
        result.setAdditionalInformation(info);
        result.setValue(encode(result, authentication));

        return result;
    }

    /**
     * 加密
     *
     * @param accessToken
     * @param authentication
     * @return
     */
    private String encode(DefaultOAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        String content;
        try {
            content = objectMapper.formatMap(
                    tokenConverter.convertAccessToken(accessToken, authentication));
            return tokenSerializer.encode(content);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot convert access token to JSON", e);
        }
    }

}