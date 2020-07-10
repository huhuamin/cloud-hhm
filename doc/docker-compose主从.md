# docker-compose mysql5.7.30 主从

## 一、安装mysql5.7.30主库

### 1.准备docker和docker-compose环境

docker官网安装教程：https://docs.docker.com/engine/install/

docker-compose 官网安装教程: https://docs.docker.com/compose/install/
curl -L https://get.daocloud.io/docker/compose/releases/download/1.26.0/docker-compose-`uname -s`-`uname -m` > /usr/local/bin/docker-compose


### 2.创建yml目录，相关数据挂载

```sh
#yml存放的目录
mkdir -p /root/docker-compse/mysql
#主库的/var/lib/mysql 数据的挂载目录
mkdir -p /data/mysql5matser
#不用slave 了，用replication的缩写replic , slave 是奴隶的意思，在美国有种族歧视的之嫌
#从库的/var/lib/mysql 数据的挂载目录
mkdir -p /data/mysql5replic

```

### 3.创建网络空间，配置时区

```sh
docker network create  mysql_replic
echo 'Asia/Shanghai' > /etc/timezone
```

### 4.提前准备好所需镜像

```sh
docker pull mysql:5.7.30
```

### 5.主库/root/docker-compse/mysql/master.yml 

```yml
version: '3.7'
services:
  #主库服务名称
  mysql-master:
    #容器名称
    container_name: mysql-master
    #镜像名称 提前 docker pull mysql:5.7.30 不然会很慢
    image: mysql:5.7.30
    #docker 重启后容器重启
    restart: always
    #选择自建bridge网络
    networks:
      - mysql_replic
    #mysql 参数配置 my.cnf里面的配置，这里简单配置下，可以配置volumes  /etc/my.cnf:/etc/my.cnf
    command: --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci --server-id=1 --log-bin=mysql-bin
    environment:
      - TZ=Asia/Shanghai
      #默认密码配置 123456 修改成您的密码
      - MYSQL_ROOT_PASSWORD=123456
    ports:
      - 3307:3306
     
    volumes:
      - /etc/localtime:/etc/localtime
      - /etc/timezone:/etc/timezone
       #挂载目录 /var/lib/mysql 是mysql的数据
      - /data/mysql5matser:/var/lib/mysql
#配置网络
networks:
  mysql_replic:
    name: mysql_replic
    #强制使用已经创建好的网络，不然报错
    external: true
```

### 6.启动主库

```sh
docker-compose -f master.yml up -d
```



### 7.进入容器，登录主库

```sh
#进入docker
docker exec -it mysql-master bash
#进入docker的mysql
mysql -uroot -p
#输入密码
```



### 8.创建同步账号replic,授权同步权限,查看主库状态信息

```sql
-- 创建同步账号
CREATE USER 'replic'@'%' IDENTIFIED BY '123456';
-- 授权权限
GRANT REPLICATION SLAVE, REPLICATION CLIENT ON *.* TO 'replic'@'%';
-- 查看主节点状态
show master status;
-- 数据的结果

-- +------------------+----------+--------------+------------------+-------------------+
-- | File             | Position | Binlog_Do_DB | Binlog_Ignore_DB | Executed_Gtid_Set |
-- +------------------+----------+--------------+------------------+-------------------+
-- | mysql-bin.000003 |      619 |              |                  |                   |
-- +------------------+----------+--------------+------------------+-------------------+
```



## 二、安装mysql5.7从库，同步主库数据

### 1.从库/root/docker-compse/mysqlreplic.yml

```yml
version: '3.7'
services:
  #从库服务名称
  mysql-replic:
    #容器名称
    container_name: mysql-replic
    #镜像名称 提前 docker pull mysql:5.7.30 不然会很慢
    image: mysql:5.7.30
    #docker 重启后容器重启
    restart: always
    #选择自建bridge网络
    networks:
      - mysql_replic
    command: --character-set-server=utf8mb4 --collation-server=utf8mb4_general_ci --server-id=2 --log-bin=mysql-slave-bin --relay_log=ep-mysql-relay-bin  --read_only=1
    environment:
      #时区
      - TZ=Asia/Shanghai
      #默认密码配置
      - MYSQL_ROOT_PASSWORD=123456
    volumes:
      - /etc/localtime:/etc/localtime
      - /etc/timezone:/etc/timezone
       #挂载目录 /var/lib/mysql 是数据盘
      - /data/mysql5replic:/var/lib/mysql
    ports:
      - 3308:3306
#配置网络
networks:
  mysql_replic:
    name: mysql_replic
    #强制使用已经创建好的网络，不然报错
    external: true
```

### 2.启动从库

```sh
docker-compose -f replic.yml up -d
#不用管 提醒
#WARNING: Found orphan containers (mysql-master) for this project. If you removed or renamed this service in your compose file, you can run this command with the --remove-orphans flag to clean it up.
```

### 3.进入容器，登录从库

```sh
#进入容器
docker exec -it mysql-replic  bash
#登录mysql
mysql -uroot -p
#输入密码
```



### 4.同步脚本

```mysql
-- 查看从库状态
show slave status ;
-- 第一次安装执行会返回
-- Empty set (0.00 sec)
-- 同步设置
change master to master_host='mysql-master', master_user='replic', master_password='123456', master_port=3306, master_log_file='mysql-bin.000003', master_log_pos=619;
-- 启动同步
start slave;
-- 查看同步结果
show slave status \G;
-- *************************** 1. row ***************************
--                Slave_IO_State: Waiting for master to send event
--                   Master_Host: mysql-master
--                   Master_User: replic
--                   Master_Port: 3306
--                 Connect_Retry: 60
--               Master_Log_File: mysql-bin.000003
--           Read_Master_Log_Pos: 619
--                Relay_Log_File: ep-mysql-relay-bin .000002
--                 Relay_Log_Pos: 320
--         Relay_Master_Log_File: mysql-bin.000003
--              Slave_IO_Running: Yes
--             Slave_SQL_Running: Yes
--               Replicate_Do_DB:
--           Replicate_Ignore_DB:
--            Replicate_Do_Table:
--        Replicate_Ignore_Table:
--       Replicate_Wild_Do_Table:
--   Replicate_Wild_Ignore_Table:
--                    Last_Errno: 0
--                    Last_Error:
--                  Skip_Counter: 0
--           Exec_Master_Log_Pos: 619
--               Relay_Log_Space: 532
--               Until_Condition: None
--                Until_Log_File:
--                 Until_Log_Pos: 0
--            Master_SSL_Allowed: No
--            Master_SSL_CA_File:
--            Master_SSL_CA_Path:
--               Master_SSL_Cert:
--             Master_SSL_Cipher:
--                Master_SSL_Key:
--         Seconds_Behind_Master: 0
-- Master_SSL_Verify_Server_Cert: No
--                 Last_IO_Errno: 0
--                 Last_IO_Error:
--                Last_SQL_Errno: 0
--                Last_SQL_Error:
--   Replicate_Ignore_Server_Ids:
--              Master_Server_Id: 1
--                   Master_UUID: b2353e18-8d00-11ea-9de4-0242ac170002
--              Master_Info_File: /var/lib/mysql/master.info
--                     SQL_Delay: 0
--           SQL_Remaining_Delay: NULL
--       Slave_SQL_Running_State: Slave has read all relay log; waiting for more updates
--            Master_Retry_Count: 86400
--                   Master_Bind:
--       Last_IO_Error_Timestamp:
--      Last_SQL_Error_Timestamp:
--                Master_SSL_Crl:
--            Master_SSL_Crlpath:
--            Retrieved_Gtid_Set:
--             Executed_Gtid_Set:
--                 Auto_Position: 0
--          Replicate_Rewrite_DB:
--                  Channel_Name:
--            Master_TLS_Version:


```

### 5.成功标志

```sql
-- 表示同步成功
--              Slave_IO_Running: Yes
--             Slave_SQL_Running: Yes
```



## 三、检查主从同步

### 1.进入主库创建一个数据库

```sql
create database test;
show databases;
-- 输出
-- +--------------------+
-- | Database           |
-- +--------------------+
-- | information_schema |
-- | mysql              |
-- | performance_schema |
-- | sys                |
-- | test               |
```



### .主库创建只读账号，供从库只读使用

```sql
grant select on *.* TO 'view'@'%' identified by '123456';
-- 从库修改数据会
--1290 - The MySQL server is running with the --read-only option so it cannot execute this statement
```



### 3.进入从库查看

```sql
show databases;
-- 输出
-- +--------------------+
-- | Database           |
-- +--------------------+
-- | information_schema |
-- | mysql              |
-- | performance_schema |
-- | sys                |
-- | test               |

```

## 四、卸载mysql主从

### 1.删除容器

```sh
#删除从库容器
docker-compose -f replic.yml down
#删除主库容器
docker-compose -f master.yml down
```

### 2.删除数据(谨慎使用此命令)

```sh
rm -rf  /data/mysql5matser
rm -rf  /data/mysql5replic
```



## PS:如果疑问留言，如果对您有用，请点赞支持

### 上一篇单机版安装[Centos7.x 安装/卸载 mysql5.7.x rpm 方式](https://mp.weixin.qq.com/s/byyhMECenFpyNIf94BNXIA)
### 下期预告 springboot mybatis druid 实现读写分离

## 参考文档
> dockerhub -mysql  https://hub.docker.com/_/mysql
> dockerhub-mysql-gitlub https://github.com/docker-library/mysql/blob/master/5.7
> docker文档 https://docs.docker.com/engine/install/
> docker-compose文档  https://docs.docker.com/compose/install/

