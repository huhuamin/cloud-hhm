# feign-ok 集成

## 引入pom

```xml
            <dependency>
                <groupId>io.github.openfeign</groupId>
                <artifactId>feign-okhttp</artifactId>
<!--                <feign-okhttp>11.0</feign-okhttp>-->
                <version>${feign-okhttp}</version>
            </dependency>
```



## 调用方

```xml
<dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.openfeign</groupId>
            <artifactId>feign-okhttp</artifactId>
</dependency>
```



## 配置文件

### 1.propereties方式

```properties
feign.okhttp.enabled=true
feign.httpclient.enabled=true
```

2.yml方式

```yml
# feign 配置
feign:
  okhttp:
    enabled: false
  httpclient:
    enabled: true
```

