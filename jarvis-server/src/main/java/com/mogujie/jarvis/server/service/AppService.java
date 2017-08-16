/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年10月15日 下午2:57:15
 */

package com.mogujie.jarvis.server.service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.exception.NotFoundException;
import com.mogujie.jarvis.dao.generate.AppMapper;
import com.mogujie.jarvis.dto.generate.App;
import com.mogujie.jarvis.dto.generate.AppExample;
import com.mogujie.jarvis.dto.generate.AppWorkerGroup;

/**
 * @author guangming,muming
 */
@Singleton
public class AppService {

    @Inject
    private AppMapper appMapper;

    @Inject
    private AppWorkerGroupService appWorkerGroupService;

    private Map<Integer, App> appMetastore = Maps.newConcurrentMap();

    @Inject
    private void init() {
        AppExample example = new AppExample();
        List<App> apps = appMapper.selectByExample(example);
        for (App app : apps) {
            appMetastore.put(app.getAppId(), app);
        }
    }

    public App getAppById(Integer appId) throws  NotFoundException {
        App app =  appMetastore.get(appId);
        if(app == null){
            throw new NotFoundException("App not found. appId:" + appId);
        }
        return app;
    }

    public App getAppByName(String appName) throws NotFoundException{
        for (Entry<Integer, App> entry : appMetastore.entrySet()) {
            App app = entry.getValue();
            if (app.getAppName().equals(appName)) {
                return app;
            }
        }
        throw new NotFoundException("App not found. appName:" + appName);
    }


    public int getAppIdByName(String appName) throws NotFoundException{
        return getAppByName(appName).getAppId();
    }

    public String getAppNameByAppId(Integer appId) throws  NotFoundException{
        return getAppById(appId).getAppName();
    }

    public List<App> getAppList() {
        List<App> list = Lists.newArrayList(appMetastore.values());
        return list;
    }

    public void insert(App app) {
        appMapper.insertSelective(app);
        app = appMapper.selectByPrimaryKey(app.getAppId());
        appMetastore.put(app.getAppId(), app);
    }

    public void update(App app) {
        int appId = app.getAppId();
        appMapper.updateByPrimaryKeySelective(app);
        app = appMapper.selectByPrimaryKey(appId);

        App srcApp = appMetastore.get(appId);
        if (srcApp == null) {
            appMetastore.put(appId, app);
        } else {
            Field[] fields = app.getClass().getDeclaredFields();
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {
                    continue;
                }

                field.setAccessible(true);
                try {
                    Object value = field.get(app);
                    if (value != null) {
                        field.set(srcApp, value);
                    }
                } catch (IllegalArgumentException | IllegalAccessException e) {
                    Throwables.propagate(e);
                }
            }
        }
    }

    public void delete(int appId) {
        appMapper.deleteByPrimaryKey(appId);
        appWorkerGroupService.deleteByAppId(appId);
    }

    /**
     * 检查——名字重复
     *
     * @param name
     * @return
     */
    public void checkDuplicateName(String name,Integer ignoreId) throws IllegalArgumentException {
        AppExample example = new AppExample();
        example.createCriteria().andAppNameEqualTo(name);
        List<App> list = appMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            if(ignoreId !=null && list.size() == 1 && list.get(0).getAppId().equals(ignoreId)){
                return;
            }
            throw new IllegalArgumentException("appName名称重复了. name:" + name);
        }
    }

    /**
     * app能否访问workerGroup
     *
     * @param appId         ：appId
     * @param workerGroupId ：workerGroupId
     * @return ：
     */
    public boolean canAccessWorkerGroup(int appId, int workerGroupId) {
        AppWorkerGroup appWorkerGroup = appWorkerGroupService.query(appId, workerGroupId);
        if (appWorkerGroup == null) {
            return false;
        } else {
            return true;
        }
    }

}
