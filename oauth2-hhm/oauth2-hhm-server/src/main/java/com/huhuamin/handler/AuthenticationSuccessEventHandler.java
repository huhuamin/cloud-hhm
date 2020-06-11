package com.huhuamin.handler;


import io.micrometer.core.instrument.util.StringUtils;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;


/**
 * 认证成功处理
 *
 * @author ruoyi
 */
@Component
public class AuthenticationSuccessEventHandler implements ApplicationListener<AuthenticationSuccessEvent> {
//    @Autowired
//    private RemoteLogService remoteLogService;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication authentication = (Authentication) event.getSource();
        System.out.println(authentication.toString());
//        if (StringUtils.isNotEmpty(authentication.getAuthorities())
//                && authentication.getPrincipal()
//        {


        // 记录用户登录日志
//            remoteLogService.saveLogininfor(username, Constants.LOGIN_SUCCESS, "登录成功");
//        }
    }
}
