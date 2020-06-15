# nacos 单机版+mysql 安装	

## [1下载最新版nacos](https://github.com/alibaba/nacos/releases)

## 2解压nacos

## 3找到conf 下的application,修改mysql配置，到哪都能localhost 使用

```properties
### If user MySQL as datasource:
spring.datasource.platform=mysql

### Count of DB:
db.num=1

### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user=nacos
db.password=nacos
```

## 4启动,到bin目录下

### window

```sh
startup.bat -m standalone
```

### mac/linux

```sh
./startup.sh -m standalone
```



