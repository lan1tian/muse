/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2015年12月15日 下午5:16:03
 */

package com.mogujie.jarvis.server.guice4test;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.mogujie.jarvis.server.guice.JarvisMyBatisModule;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

public class Injectors4Test {

    private static Injector injector;

    static {
        List<Module> modules = Lists.newArrayList();

        Properties properties = new Properties();
        try {
            properties.load(Injectors4Test.class.getClassLoader().getResourceAsStream("server.properties"));
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        modules.add(new JarvisMyBatisModule(properties));
         modules.add(new GuiceModule4Test(properties));
        injector = Guice.createInjector(modules);
    }

    private Injectors4Test() {
    }

    public static Injector getInjector() {
        return injector;
    }
}
