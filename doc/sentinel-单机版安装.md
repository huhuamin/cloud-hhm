# sentinel-单机版安装

## [1下载最新版sentinel](https://github.com/alibaba/Sentinel/releases)
ps:迅雷下载会很快哦

## 启动sentinel（需要有java环境）

```sh
#server.port 是启动端口 csp.sentinel.dashboard.server 是控制面板
java -Dserver.port=8858 -Dcsp.sentinel.dashboard.server=localhost:8858 -jar sentinel-dashboard-1.7.2.jar
```



