package com.huhuamin.common.oauth2.common.core.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @ClassName : LoginException  //类名
 * @Description : 公共异常封装  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 10:03  //时间
 */

@Data
@EqualsAndHashCode(callSuper = false)
@edu.umd.cs.findbugs.annotations.SuppressFBWarnings("EI_EXPOSE_REP")
public class HCommonException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    /**
     * 所属模块
     */
    private String module;

    /**
     * 错误码
     */
    private Integer code;

    /**
     * 错误码对应的参数
     */

    private Object[] args;

    /**
     * 错误消息
     */
    private String defaultMessage;

    public HCommonException(String module, Integer code, Object[] args, String defaultMessage) {
        this.module = module;
        this.code = code;
        this.args = args;
        this.defaultMessage = defaultMessage;
    }


}
