/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年10月15日 下午2:57:15
 */

package com.mogujie.jarvis.server.service;

import java.util.ArrayList;
import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.dao.generate.AppWorkerGroupMapper;
import com.mogujie.jarvis.dto.generate.AppWorkerGroup;
import com.mogujie.jarvis.dto.generate.AppWorkerGroupExample;
import com.mogujie.jarvis.dto.generate.AppWorkerGroupKey;


/**
 * @author muming
 */
@Singleton
public class AppWorkerGroupService {

    @Inject
    private AppWorkerGroupMapper appWorkerGroupMapper;

    public AppWorkerGroup query(int appId, int workerGroupId){
        AppWorkerGroupKey key = new AppWorkerGroupKey();
        key.setAppId(appId);
        key.setWorkerGroupId(workerGroupId);
        return appWorkerGroupMapper.selectByPrimaryKey(key);
    }

    public List<AppWorkerGroup> queryByAppId(int appId){
        AppWorkerGroupExample example = new AppWorkerGroupExample();
        example.createCriteria().andAppIdEqualTo(appId);
        List<AppWorkerGroup> appWorkerGroups = appWorkerGroupMapper.selectByExample(example);
        if (appWorkerGroups == null) {
            appWorkerGroups = new ArrayList<AppWorkerGroup>();
        }
        return appWorkerGroups;
    }

    public int insert(AppWorkerGroup appWorkerGroup) {
        return appWorkerGroupMapper.insertSelective(appWorkerGroup);
    }

    public int delete(Integer appId, Integer workerGroupId) {
        AppWorkerGroupKey key = new AppWorkerGroupKey();
        key.setAppId(appId);
        key.setWorkerGroupId(workerGroupId);
        return appWorkerGroupMapper.deleteByPrimaryKey(key);
    }

    public int deleteByAppId(int appId) {
        AppWorkerGroupExample example = new AppWorkerGroupExample();
        example.createCriteria().andAppIdEqualTo(appId);
        return appWorkerGroupMapper.deleteByExample(example);
    }

}
