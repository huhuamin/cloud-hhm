package com.clzah.common.db.service;


import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @ClassName : DefaultRoutingDataSource  //类名
 * @Description : 动态数据源  //描述
 * @Author : 胡化敏（175759041@qq.com）
 * @Date: 2020-04-30 15:22  //时间
 */
public class DefaultRoutingDataSource extends AbstractRoutingDataSource {
    /**
     * 使用本地线程变了 ThreadLocal 通一个线程用 一个数据源
     */
    private static final ThreadLocal<String> contextHolder = new ThreadLocal<>();

    public DefaultRoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return getDataSource();
    }

    public static void setDataSource(String dataSource) {
        contextHolder.set(dataSource);
    }

    public static String getDataSource() {
        return contextHolder.get();
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
