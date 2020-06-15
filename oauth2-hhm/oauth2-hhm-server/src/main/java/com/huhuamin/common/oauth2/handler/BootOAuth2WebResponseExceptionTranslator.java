package com.huhuamin.common.oauth2.handler;

import com.huhuamin.common.oauth2.common.core.constants.HConstants;
import com.huhuamin.common.oauth2.common.core.exception.HCommonException;
import com.huhuamin.common.oauth2.common.core.model.HResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @ClassName : BootOAuth2WebResponseExceptionTranslator  //类名
 * @Description : 异常转换  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 13:22  //时间
 */
@Slf4j
@Service
public class BootOAuth2WebResponseExceptionTranslator implements WebResponseExceptionTranslator {
    @Override
    public ResponseEntity translate(Exception e) throws Exception {
        if (null != e.getCause() && e.getCause() instanceof HCommonException) {
            HCommonException hCommonException = (HCommonException) e.getCause();
            return handleOAuth2Exception(HResult.failed(hCommonException.getCode(), hCommonException.getMessage(), HConstants.MODEL_LOGIN_NAME));
        } else {
            return handleOAuth2Exception(HResult.failed(500, e.getMessage()));
        }

    }

    private ResponseEntity<HResult> handleOAuth2Exception(HResult hResult) throws IOException {

        int status = hResult.getCode();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Cache-Control", "no-store");
        headers.set("Pragma", "no-cache");
        ResponseEntity<HResult> response = new ResponseEntity<>(hResult, headers,
                HttpStatus.valueOf(status));
        return response;

    }

}
