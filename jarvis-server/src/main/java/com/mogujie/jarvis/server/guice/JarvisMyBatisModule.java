/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月15日 下午8:04:03
 */

package com.mogujie.jarvis.server.guice;

import java.util.Properties;

import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.guice.MyBatisModule;

public class JarvisMyBatisModule extends MyBatisModule {

    private Properties properties;

    public JarvisMyBatisModule(Properties properties) {
        this.properties = properties;
    }

    @Override
    protected void initialize() {
        environmentId("development");
        addMapperClasses("com.mogujie.jarvis.dao");
        bindDataSourceProvider(new DataSourceProvider(properties));
        bindTransactionFactoryType(JdbcTransactionFactory.class);
    }

}
