# cloud-hhm
## tag0.0.2
    认证中心
        非对称加密
        jwt json 对称加密
        oauthresource demo
        
## tag0.0.3
    认证中心
        oauth2全局异常处理
        模拟从数据库取数据
        抽离公共Resource Config,其他微服务获取用户信息，只需要调用
```java
 UserJwt username = (UserJwt) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
```

        