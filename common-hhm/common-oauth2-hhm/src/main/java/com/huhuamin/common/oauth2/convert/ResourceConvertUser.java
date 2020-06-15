package com.huhuamin.common.oauth2.convert;

import cn.hutool.core.convert.Convert;
import com.huhuamin.common.oauth2.common.core.constants.HSecurityConstants;
import com.huhuamin.common.oauth2.model.UserJwt;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.UserAuthenticationConverter;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @ClassName : ResourceServerCommonConvertUser  //类名
 * @Description : 后台用户自动转换  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 15:22  //时间
 */
public class ResourceConvertUser implements UserAuthenticationConverter {
    private static final String N_A = "N/A";

    @Override
    public Map<String, ?> convertUserAuthentication(Authentication userAuthentication) {
        Map<String, Object> authMap = new LinkedHashMap<>();
        authMap.put(USERNAME, userAuthentication.getName());
        if (userAuthentication.getAuthorities() != null && !userAuthentication.getAuthorities().isEmpty()) {
            authMap.put(AUTHORITIES, AuthorityUtils.authorityListToSet(userAuthentication.getAuthorities()));
        }
        return authMap;
    }

    /**
     * https://my.oschina.net/giegie/blog/3023768 根据checktoken 的结果转化用户信息
     * 获取用户认证信息
     */
    @Override
    public Authentication extractAuthentication(Map<String, ?> map) {
        if (map.containsKey(USERNAME)) {
            Collection<? extends GrantedAuthority> authorities = getAuthorities(map);

            Long userId = Convert.toLong(map.get(HSecurityConstants.DETAILS_USER_ID));
            String username = (String) map.get(HSecurityConstants.DETAILS_USERNAME);
            UserJwt user = new UserJwt(userId, username, N_A, true, true, true, true, authorities);
            return new UsernamePasswordAuthenticationToken(user, N_A, authorities);
        }
        return null;
    }

    /**
     * 获取权限资源信息
     */
    private Collection<? extends GrantedAuthority> getAuthorities(Map<String, ?> map) {
        Object authorities = map.get(AUTHORITIES);
        if (authorities instanceof String) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList((String) authorities);
        }
        if (authorities instanceof Collection) {
            return AuthorityUtils.commaSeparatedStringToAuthorityList(
                    StringUtils.collectionToCommaDelimitedString((Collection<?>) authorities));
        }
        throw new IllegalArgumentException("Authorities must be either a String or a Collection");
    }
}
