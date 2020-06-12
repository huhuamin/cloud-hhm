package com.huhuamin.common.oauth2.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.io.Serializable;

/**
 * @ClassName : SecurityAuthProperties  //类名
 * @Description : SecurityAuthProperties  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-04-26 17:21  //时间
 */

@ConfigurationProperties(prefix = "hhm.auth")
@Data
//@RefreshScope
public class SecurityAuthProperties implements Serializable {

    private static final long serialVersionUID = 8428798474641047929L;

    /**
     * 客户端账号
     */
    private String clientId;

    /**
     * 客户端密码
     */
    private String clientSecret;

    /**
     * 产品类型存储时间
     */
    private int productTypeValiditySeconds;

    /**
     * code存储到redis中的时间
     */
    private int tokenValiditySeconds;

    /**
     * token+ip存储到redis中的时间
     */
    private int jwtValiditySeconds;


    /**
     * 域名
     */
    private String cookieDomain;

    /**
     * cookie的有效期
     */
    private int cookieMaxAge;

    /**
     * 申请jwt令牌路径
     */
    private String accessTokenUri;

    /**
     * 公钥
     */
    private String publicKey;
    /**
     * 长token 加密密钥
     */
    private String symmetricKey;
    /**
     * jks 位置
     */
    private String keyLocation;
    /**
     * 别名
     */
    private String alias;
    /**
     * keystore 密码
     */
    private String keystorePassword;
    /**
     * key密码
     */
    private String keyPassword;


}
