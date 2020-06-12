package com.huhuamin.oauth2.service;


import com.huhuamin.core.exception.HCommonException;
import com.huhuamin.core.exception.LoginException;
import com.huhuamin.core.model.HResult;
import com.huhuamin.oauth2.model.DbTempUser;
import com.huhuamin.oauth2.model.UserJwt;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

/**
 * @ClassName : UserDetailsServiceImpl  //类名
 * @Description : 认证用户实现类  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-12 09:26  //时间
 */
@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    public Map<String, DbTempUser> user = new HashMap<>();

    @PostConstruct
    public void init() {
        user.put("huhuamin", new DbTempUser(1L, "huhuamin", passwordEncoder.encode("123456")));
        user.put("chenyong", new DbTempUser(2L, "chenyong", passwordEncoder.encode("654321")));
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HResult<DbTempUser> dbTempUserHResult = null;
        if (user.containsKey(username)) {
            dbTempUserHResult = HResult.ok(user.get(username));
        } else {
            dbTempUserHResult = HResult.failed("账户密码不匹配");
        }
        checkUser(dbTempUserHResult, username);
        return getUserDetails(dbTempUserHResult.getData());
    }

    public void checkUser(HResult<DbTempUser> userResult, String username) {
        if (null == userResult) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (userResult.isFail()) {
            log.info(userResult.getMsg());
            throw new LoginException(userResult.getMsg());
        } else if (null == userResult.getData()) {
            throw new LoginException("登录用户：" + username + " 不存在");
        }
//        else if (UserStatus.DELETED.getCode().equals(userResult.getData().getSysUser().getDelFlag())) {
//            log.info("登录用户：{} 已被删除.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
//        } else if (UserStatus.DISABLE.getCode().equals(userResult.getData().getSysUser().getStatus())) {
//            log.info("登录用户：{} 已被停用.", username);
//            throw new BaseException("对不起，您的账号：" + username + " 已停用");
//        }
    }

    private UserDetails getUserDetails(DbTempUser dbUser) {

        Set<String> dbAuthsSet = new HashSet<String>();
        // 获取角色
        dbAuthsSet.add("admin");
        // 获取权限
        dbAuthsSet.add("system:user");
        Collection<? extends GrantedAuthority> authorities = AuthorityUtils
                .createAuthorityList(dbAuthsSet.toArray(new String[0]));
        return new UserJwt(dbUser.getId(), dbUser.getUserName(), dbUser.getPassword(), true, true, true, true,
                authorities);
    }
}
