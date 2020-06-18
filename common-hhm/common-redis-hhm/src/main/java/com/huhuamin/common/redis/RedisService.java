package com.huhuamin.common.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;


import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


/**
 * @ClassName : RedisService  //类名
 * @Description : RedisService  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-04-26 11:35  //时间
 */
@Service
public class RedisService {
    /*防止公用一个系统,数据库备注*/
    @Value("${spring.redis.sysName}")
    private String DB_PREFIX;
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 默认7天
     */
    private final static int EXPIRE_SECOND = 60 * 60 * 24 * 7;

    /**
     * 锁，默认等待时间7s，锁25
     *
     * @param lockName
     * @return
     */
    public String acquireLock(String lockName) {
        return acquireLock(lockName, 4000, 25000);
    }

    /**
     * @param lockName       锁名称
     * @param acquierTimeout 获得或超时时间 毫秒
     * @param lockTimeout    锁的过期时间  毫秒
     * @return
     */
    public String acquireLock(String lockName, long acquierTimeout, long lockTimeout) {

        String result = null;
        try {
            String identifier = UUID.randomUUID().toString();//释放锁的时候持有者校验
            String key = DB_PREFIX + "lock:" + lockName;


            int lockExpire = (int) lockTimeout / 1000;//锁秒数
            long end = System.currentTimeMillis() + acquierTimeout;
            while (System.currentTimeMillis() < end) {//阻塞
//                if (jedis.setnx(key, identifier) == 1) {//设置超时时间
                if (redisTemplate.opsForValue().setIfAbsent(key, identifier)) {
//                    jedis.expire(key, lockExpire);
                    redisTemplate.expire(key, lockExpire, TimeUnit.SECONDS);
                    //锁设置成功，redis操作成功
                    result = identifier;
                    break;
                }

//                if (jedis.ttl(key) == -1) { //检测过期时间
                if (redisTemplate.getExpire(key) == -1) {
                    redisTemplate.expire(key, lockExpire, TimeUnit.SECONDS);
                }
                Thread.sleep(300);//睡眠时间
            }
        } catch (
                Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 释放锁
     *
     * @param lockName
     * @param identifier
     * @return
     */
    public boolean releaseLock(String lockName, String identifier) {
        Boolean isRelease = false;
        try {
            String key = DB_PREFIX + "lock:" + lockName;


            isRelease = redisTemplate.execute(new SessionCallback<Boolean>() {
                @Override
                @SuppressWarnings({"unchecked", "rawtypes"})
                public Boolean execute(RedisOperations operations) throws DataAccessException {
                    while (true) {
                        operations.watch(key);
                        if (identifier.equals(operations.opsForValue().get(key))) { //判断获得锁的线程和当前redis中存的锁是同一个
                            operations.multi(); //开始事务
                            operations.delete(key);
                            List<Object> list = operations.exec();
                            if (list == null || list.isEmpty()) {
                                continue;
                            }
                            return true;
                        } else {
                            return false;
                        }
                    } //如果失败则重试

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
        return isRelease;
    }


    public void setKeyValue(String key, String value, Integer sec) {
        setKeyValue(DB_PREFIX, key, value, sec);

    }

    public void setKeyValue(String dbPrefix, String key, String value, Integer sec) {
        try {
            key = dbPrefix + key;
            if (null == sec) {
                redisTemplate.opsForValue().set(key, value, EXPIRE_SECOND, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value, sec, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getKeyValue(String dbPrefix, String key, String value, Integer sec) {
        try {
            key = dbPrefix + key;
            if (null == sec) {
                redisTemplate.opsForValue().set(key, value, EXPIRE_SECOND, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(key, value, sec, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getValueByKey(String key) {
        return getValueByKey(DB_PREFIX, key);
    }

    public String getValueByKey(String dbPrefix, String key) {
        try {
            key = dbPrefix + key;

            return redisTemplate.opsForValue().get(key);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public void delByKey(String key) {
        delByKey(DB_PREFIX, key);

    }

    public void delByKey(String dbPrefix, String key) {
        try {
            key = dbPrefix + key;
            redisTemplate.delete(key);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 今天24点后过期
     *
     * @param key
     * @param value
     */
    public void setKeyValueToDay(String key, String value) {
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(now);
        Long dif = calendar.getTimeInMillis() - calendar2.getTimeInMillis();
        int sec = (int) (dif / 1000);
        setKeyValue(key, value, sec);
    }


}
