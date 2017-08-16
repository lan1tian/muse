/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: wuya
 * Create Date: 2016年1月15日 下午4:29:40
 */

package com.mogujie.jarvis.server.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mogujie.jarvis.dao.IDMapper;

public class TestIDService {

    private IDService idService;

    @Before
    public void setup() {
        Injector injector = Guice.createInjector(new AbstractModule() {

            @Override
            protected void configure() {
                IDMapper idMapper = Mockito.mock(IDMapper.class);
                Mockito.when(idMapper.selectMaxJobId()).thenReturn(99L);
                Mockito.when(idMapper.selectMaxTaskId()).thenReturn(9L);

                bind(IDMapper.class).toInstance(idMapper);
            }
        });

        idService = injector.getInstance(IDService.class);
    }

    @Test
    public void testGetNextJobId() {
        for (long i = 100; i < 1000; i++) {
            Assert.assertEquals(idService.getNextJobId(), i);
        }
    }

    @Test
    public void testGetNextTaskId() {
        for (long i = 10; i < 100; i++) {
            Assert.assertEquals(idService.getNextTaskId(), i);
        }
    }
}
