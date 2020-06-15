# cloud-hhm
## tag0.0.2
    认证中心
        非对称加密
        jwt json 对称加密
        oauthresource demo
        jwt 做服务器无状态设计，可以堆机器扩展性能
        
## tag0.0.3
    认证中心
        oauth2全局异常处理
        模拟从数据库取数据
        抽离公共Resource Config,其他微服务获取用户信息，只需要调用
```java
 UserJwt username = (UserJwt) SecurityContextHolder.getContext()
                        .getAuthentication().getPrincipal();
```
## tag0.0.4
网关第一版，oauth/token,登陆 basic auth 信息服务端生成，不是网页端生成，提高安全性
自动更新配置信息
@RefreshScore GatewayProperties
gateway sentinel 监控

        