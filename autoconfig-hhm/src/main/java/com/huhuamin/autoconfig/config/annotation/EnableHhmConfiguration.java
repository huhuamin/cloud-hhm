package com.huhuamin.autoconfig.config.annotation;

import com.huhuamin.autoconfig.config.HhmDefineImportSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @ClassName : EnableHhmConfiguration  //类名
 * @Description : 启动  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-28 14:07  //时间
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(HhmDefineImportSelector.class)
public @interface EnableHhmConfiguration {
}
