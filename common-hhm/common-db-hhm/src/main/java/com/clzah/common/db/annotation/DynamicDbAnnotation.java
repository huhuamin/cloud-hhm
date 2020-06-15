package com.clzah.common.db.annotation;

import org.springframework.stereotype.Indexed;

import java.lang.annotation.*;

/**
 * @ClassName : 动态数据源注解  //类名
 * @Description : 动态数据源注解  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-04-30 15:33  //时间
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Indexed
public @interface DynamicDbAnnotation {
    String value() default "";
}
