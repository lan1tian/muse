/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年11月27日 下午3:45:24
 */

package com.mogujie.jarvis.server.service;

import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.domain.WorkerInfo;
import com.mogujie.jarvis.core.domain.WorkerStatus;
import com.mogujie.jarvis.dao.generate.WorkerMapper;
import com.mogujie.jarvis.dto.generate.Worker;
import com.mogujie.jarvis.dto.generate.WorkerExample;

/**
 * @author guangming
 */
@Singleton
public class WorkerService {
    @Inject
    private WorkerMapper workerMapper;

    public int getWorkerId(String ip, int port) {
        int workerId = 0;
        WorkerExample example = new WorkerExample();
        example.createCriteria().andIpEqualTo(ip).andPortEqualTo(port);
        List<Worker> workers = workerMapper.selectByExample(example);
        if (workers != null && !workers.isEmpty()) {
            workerId = workers.get(0).getId();
        }
        return workerId;
    }

    public Worker getWorkerById(int workId) {
        return workerMapper.selectByPrimaryKey(workId);
    }

    public void saveWorker(String ip, int port, int groupId) {
        Date dt = DateTime.now().toDate();
        Worker worker = new Worker();
        worker.setIp(ip);
        worker.setPort(port);
        worker.setWorkerGroupId(groupId);
        worker.setUpdateTime(dt);

        int workerId = getWorkerId(ip, port);
        if (workerId > 0) {
            worker.setId(workerId);
            workerMapper.updateByPrimaryKeySelective(worker);
        } else {
            worker.setCreateTime(dt);
            workerMapper.insertSelective(worker);
        }
    }

    public void updateWorkerStatus(int workerId, int status) {
        Worker worker = new Worker();
        worker.setId(workerId);
        worker.setStatus(status);
        worker.setUpdateTime(DateTime.now().toDate());
        workerMapper.updateByPrimaryKeySelective(worker);
    }

    public WorkerMapper getWorkerMapper() {
        return workerMapper;
    }

    public void setWorkerMapper(WorkerMapper workerMapper) {
        this.workerMapper = workerMapper;
    }

    public List<WorkerInfo> getOffLineWorkers() {
        List<WorkerInfo> list = Lists.newArrayList();
        WorkerExample example = new WorkerExample();
        example.createCriteria().andStatusEqualTo(WorkerStatus.OFFLINE.getValue());
        List<Worker> workers = workerMapper.selectByExample(example);
        for (Worker worker : workers) {
            list.add(new WorkerInfo(worker.getIp(), worker.getPort()));
        }
        return list;
    }
}
