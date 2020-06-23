package com.clzah.common.db.aop;

import com.clzah.common.db.annotation.DynamicDbAnnotation;
import com.clzah.common.db.constants.DbConstants;
import com.clzah.common.db.service.DefaultRoutingDataSource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * 这个要下沉系统
 *  增加下面2个注解
 * @Component
 * @Aspect
 * @ClassName : DataSourceAspect  //类名
 * @Description : 数据源切面  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-06-15 18:45  //时间
 */
public class DbAspect {
    @Around("@annotation(com.clzah.common.db.annotation.DynamicDbAnnotation)")
    public Object around(ProceedingJoinPoint point) throws Throwable {
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        DynamicDbAnnotation dataSource = method.getAnnotation(DynamicDbAnnotation.class);
        if (null != dataSource) {
            //兼容现有的系统，save，update 操作，也可以作为约定大于配置
            if (method.getName().contains("save") || method.getName().contains("update")) {
                DefaultRoutingDataSource.setDataSource(DbConstants.CLOUD_HHM_MASTER);
            } else {
                DefaultRoutingDataSource.setDataSource(dataSource.value());
            }
        }else{
            DefaultRoutingDataSource.setDataSource(DbConstants.CLOUD_HHM_REPLIC);
        }
        /**
         *  如果没有注解{@link cn.com.uflyvision.metadata.config.DynamicDruidConfiguration#dataSource} 配置了默认的数据源
         */

        try {
            return point.proceed();
        } finally {
            DefaultRoutingDataSource.clearDataSource();
        }
    }
}
