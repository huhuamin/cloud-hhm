package com.huhuamin.common.oauth2.common.core.exception;

import com.huhuamin.common.oauth2.common.core.constants.HConstants;

/**
 * @ClassName : LoginException  //类名
 * @Description : 登陆异常  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 13:44  //时间
 */
public class LoginException extends HCommonException {
    public LoginException(String defaultMessage) {
        this(null, defaultMessage);
    }

    public LoginException(Object[] args, String defaultMessage) {
        super(HConstants.MODEL_LOGIN_NAME, 403, args, defaultMessage);
    }
}
