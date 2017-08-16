/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年8月31日 下午2:30:51
 */
package com.mogujie.jarvis.core.util;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.configuration.Configuration;

/**
 * @author guangming
 *
 */
public class ReflectionUtils {
    private static final Class<?>[] EMPTY_ARRAY = new Class[] {};
    private static final Map<Class<?>, Constructor<?>> CONSTRUCTOR_CACHE = new ConcurrentHashMap<Class<?>, Constructor<?>>();
    private static final Map<String, Class<?>> CLASS_CACHE = new ConcurrentHashMap<String, Class<?>>();

    @SuppressWarnings("unchecked")
    private static <T> T newInstance(Class<T> clazz) {
        T result;
        try {
            Constructor<T> meth = (Constructor<T>) CONSTRUCTOR_CACHE.get(clazz);
            if (meth == null) {
                meth = clazz.getDeclaredConstructor(EMPTY_ARRAY);
                meth.setAccessible(true);
                CONSTRUCTOR_CACHE.put(clazz, meth);
            }
            result = meth.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstanceByClassName(String className) throws ClassNotFoundException {
        Class<?> clazz = CLASS_CACHE.get(className);
        if (clazz == null) {
            clazz = Class.forName(className);
            CLASS_CACHE.put(className, clazz);
        }
        return (T) newInstance(clazz);
    }

    public static <T> List<T> getInstancesByConf(Configuration conf, String key)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        String classString = conf.getString(key);
        List<T> instances = new ArrayList<T>();
        if (classString == null) {
            return instances;
        }

        classString = classString.trim();
        if (classString.equals("")) {
            return instances;
        }

        String[] classes = classString.split(",");
        for (String className : classes) {
            T theClass = getInstanceByClassName(className);
            instances.add(theClass);
        }

        return instances;
    }
}
