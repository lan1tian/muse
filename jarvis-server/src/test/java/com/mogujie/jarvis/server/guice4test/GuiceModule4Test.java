/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月15日 下午4:39:48
 */

package com.mogujie.jarvis.server.guice4test;

import java.util.Properties;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Scopes;
import com.google.inject.Singleton;
import com.google.inject.name.Names;
import com.mogujie.jarvis.server.ServerConigKeys;
import com.mogujie.jarvis.server.dispatcher.TaskManager;
import com.mogujie.jarvis.server.dispatcher.workerselector.RandomWorkerSelector;
import com.mogujie.jarvis.server.dispatcher.workerselector.RoundRobinWorkerSelector;
import com.mogujie.jarvis.server.dispatcher.workerselector.WorkerSelector;

public class GuiceModule4Test extends AbstractModule {

    private Properties properties;

    public GuiceModule4Test(Properties properties) {
        this.properties = properties;
    }

    @Provides
    @Singleton
    private WorkerSelector provideWorkerSelector() {
        String workerSelectorName = properties.getProperty(ServerConigKeys.WORKER_SELECTOR, "RoundRobinWorkerSelector");
        if (workerSelectorName.equals("RoundRobinWorkerSelector")) {
            return new RoundRobinWorkerSelector();
        }

        return new RandomWorkerSelector();
    }

    @Override
    protected void configure() {
        Names.bindProperties(binder(), properties);
        bind(TaskManager.class).in(Scopes.SINGLETON);
    }

}
