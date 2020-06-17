package com.huhuamin.common.oauth2.jwt;


import org.springframework.security.oauth2.common.DefaultExpiringOAuth2RefreshToken;
import org.springframework.security.oauth2.common.DefaultOAuth2RefreshToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2RefreshToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

import java.util.Collection;

/**
 * jwt的加密配置 TokenStore
 */
public class JweTokenStore implements TokenStore {
    private String encodedSigningKey;
    private final TokenStore delegate;
    private final JwtAccessTokenConverter converter;
    private final JweTokenSerializer crypto;

    public JweTokenStore(String encodedSigningKey, TokenStore delegate,
                         JwtAccessTokenConverter converter, JweTokenSerializer crypto) {
        this.encodedSigningKey = encodedSigningKey;
        this.delegate = delegate;
        this.converter = converter;
        this.crypto = crypto;
    }

    @Override
    public OAuth2AccessToken readAccessToken(String tokenValue) {
        return converter.extractAccessToken(
                tokenValue, crypto.decode(encodedSigningKey, tokenValue));
    }

    @Override
    public OAuth2Authentication readAuthentication(OAuth2AccessToken token) {
        return readAuthentication(token.getValue());
    }

    @Override
    public OAuth2Authentication readAuthentication(String token) {
        return converter.extractAuthentication(crypto.decode(encodedSigningKey, token));
    }

    // delegating remaining methods

    @Override
    public void storeAccessToken(OAuth2AccessToken token, OAuth2Authentication authentication) {
        delegate.storeAccessToken(token, authentication);
    }

    @Override
    public void removeAccessToken(OAuth2AccessToken token) {
        delegate.removeAccessToken(token);
    }

    @Override
    public void storeRefreshToken(OAuth2RefreshToken refreshToken, OAuth2Authentication authentication) {
        delegate.storeRefreshToken(refreshToken, authentication);
    }


    private OAuth2RefreshToken createRefreshToken(OAuth2AccessToken encodedRefreshToken) {
//        if (!delegate.isRefreshToken(encodedRefreshToken)) {
//            throw new InvalidTokenException("Encoded token is not a refresh token");
//        }
        if (encodedRefreshToken.getExpiration()!=null) {
            return new DefaultExpiringOAuth2RefreshToken(encodedRefreshToken.getValue(),
                    encodedRefreshToken.getExpiration());
        }
        return new DefaultOAuth2RefreshToken(encodedRefreshToken.getValue());
    }

    @Override
    public OAuth2RefreshToken readRefreshToken(String tokenValue) {
        OAuth2AccessToken encodedRefreshToken=  converter.extractAccessToken(
                tokenValue, crypto.decode(encodedSigningKey, tokenValue));
        OAuth2RefreshToken refreshToken = createRefreshToken(encodedRefreshToken);
//        if (approvalStore != null) {
//            OAuth2Authentication authentication = readAuthentication(tokenValue);
//            if (authentication.getUserAuthentication() != null) {
//                String userId = authentication.getUserAuthentication().getName();
//                String clientId = authentication.getOAuth2Request().getClientId();
//                Collection<Approval> approvals = approvalStore.getApprovals(userId, clientId);
//                Collection<String> approvedScopes = new HashSet<String>();
//                for (Approval approval : approvals) {
//                    if (approval.isApproved()) {
//                        approvedScopes.add(approval.getScope());
//                    }
//                }
//                if (!approvedScopes.containsAll(authentication.getOAuth2Request().getScope())) {
//                    return null;
//                }
//            }
//        }
        return refreshToken;
        //return delegate.readRefreshToken(tokenValue);
    }

    @Override
    public OAuth2Authentication readAuthenticationForRefreshToken(OAuth2RefreshToken token) {

        return converter.extractAuthentication(crypto.decode(encodedSigningKey,token.getValue()));

    }

    @Override
    public void removeRefreshToken(OAuth2RefreshToken token) {
        delegate.removeRefreshToken(token);
    }

    @Override
    public void removeAccessTokenUsingRefreshToken(OAuth2RefreshToken refreshToken) {
        delegate.removeAccessTokenUsingRefreshToken(refreshToken);
    }

    @Override
    public OAuth2AccessToken getAccessToken(OAuth2Authentication authentication) {
        return delegate.getAccessToken(authentication);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientIdAndUserName(String clientId, String userName) {
        return delegate.findTokensByClientIdAndUserName(clientId, userName);
    }

    @Override
    public Collection<OAuth2AccessToken> findTokensByClientId(String clientId) {
        return delegate.findTokensByClientId(clientId);
    }
}
