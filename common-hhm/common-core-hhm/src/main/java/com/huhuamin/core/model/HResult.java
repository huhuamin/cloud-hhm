package com.huhuamin.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.beans.Transient;
import java.io.Serializable;

/**
 * @ClassName : H  //类名
 * @Description : 公共JSON 返回处理 HResult(huhuaminResult for short )  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 09:29  //时间
 */
public class HResult<T> implements Serializable {
    /**
     * 成功返回code
     */
    public static final int SUCCESS_CODE = 200;
    /**
     * 失败返回code
     */
    public static final int FAIL_CODE = 700;
    /**
     * 成功
     */
    public static final boolean TRUE = true;
    /**
     * 失败
     */
    public static final boolean FALSE = false;
    private static final long serialVersionUID = 1L;
    /**
     * true-成功 false-失败
     */
    private boolean success;
    /**
     * 错误码
     */
    private int code;
    /**
     * 错误消息
     */
    private String msg;

    private T data;
    /**
     * 模块名称
     */
    private String module;


    public static <T> HResult<T> ok() {
        return restResult(null, SUCCESS_CODE, TRUE, null);
    }

    public static <T> HResult<T> ok(T data) {
        return restResult(data, SUCCESS_CODE, TRUE, null);
    }

    public static <T> HResult<T> ok(T data, String msg) {
        return restResult(data, SUCCESS_CODE, TRUE, msg);
    }

    public static <T> HResult<T> failed() {
        return restResult(null, FAIL_CODE, FALSE, null);
    }

    public static <T> HResult<T> failed(String msg, String module) {
        return restResult(null, FAIL_CODE, FALSE, msg, null);
    }

    public static <T> HResult<T> failed(String msg) {
        return restResult(null, FAIL_CODE, FALSE, msg);
    }

    public static <T> HResult<T> failed(T data) {
        return restResult(data, FAIL_CODE, FALSE, null);
    }

    public static <T> HResult<T> failed(T data, String msg) {
        return restResult(data, FAIL_CODE, FALSE, msg);
    }

    public static <T> HResult<T> failed(int code, String msg) {
        return restResult(null, code, FALSE, msg);
    }

    public static <T> HResult<T> failed(int code, String msg, String module) {
        return restResult(null, code, FALSE, msg, module);
    }

    private static <T> HResult<T> restResult(T data, int code, boolean success, String msg) {
        return restResult(data, code, success, msg, null);
    }

    private static <T> HResult<T> restResult(T data, int code, boolean success, String msg, String module) {
        HResult<T> apiResult = new HResult<>();
        apiResult.setCode(code);
        apiResult.setSuccess(success);
        apiResult.setData(data);
        apiResult.setMsg(msg);
        apiResult.setModule(module);
        return apiResult;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    @JsonIgnore
    public boolean isFail() {
        return !success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }
}
