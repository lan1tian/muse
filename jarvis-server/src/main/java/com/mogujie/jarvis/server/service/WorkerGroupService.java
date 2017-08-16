/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年11月27日 下午3:45:24
 */

package com.mogujie.jarvis.server.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.domain.WorkerGroupStatus;
import com.mogujie.jarvis.core.exception.NotFoundException;
import com.mogujie.jarvis.dao.generate.WorkerGroupMapper;
import com.mogujie.jarvis.dto.generate.WorkerGroup;
import com.mogujie.jarvis.dto.generate.WorkerGroupExample;

import java.util.List;

/**
 * WorkerGroupService
 *
 * @author muming
 */
@Singleton
public class WorkerGroupService {

    @Inject
    private WorkerGroupMapper workerGroupMapper;

    public WorkerGroupMapper getWorkerGroupMapper() {
        return workerGroupMapper;
    }

    public void setWorkerGroupMapper(WorkerGroupMapper workerGroupMapper) {
        this.workerGroupMapper = workerGroupMapper;
    }

    public int update(WorkerGroup workerGroup) {
        return workerGroupMapper.updateByPrimaryKeySelective(workerGroup);
    }

    public int insert(WorkerGroup workerGroup) {
        return workerGroupMapper.insertSelective(workerGroup);
    }

    public int getGroupIdByAuthKey(String key) {
        WorkerGroupExample example = new WorkerGroupExample();
        example.createCriteria().andAuthKeyEqualTo(key);
        List<WorkerGroup> list = workerGroupMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0).getId();
        }
        return 0;
    }

    public WorkerGroup getGroupByGroupId(int groupId) throws NotFoundException {
        WorkerGroup wg = workerGroupMapper.selectByPrimaryKey(groupId);
        if(wg == null || wg.getStatus() == WorkerGroupStatus.DELETED.getValue()){
            throw new NotFoundException("WorkerGroup not found. groupId:" + groupId);
        }
        return wg;
    }

}
