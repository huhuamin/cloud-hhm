package com.huhuamin.autoconfig.config;

import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @ClassName : HhmDefineImportSelector  //类名
 * @Description : 自定义bean 装载  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-28 14:05  //时间
 */
public class HhmDefineImportSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{HhmConfiguration.class.getName()};
    }
}


