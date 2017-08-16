/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年12月16日 下午5:59:26
 */

package com.mogujie.jarvis.server.service;

import java.util.List;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.dao.generate.AlarmMapper;
import com.mogujie.jarvis.dto.generate.Alarm;
import com.mogujie.jarvis.dto.generate.AlarmExample;

/**
 * BizGroupService
 *
 * @author  muming
 */
@Singleton
public class AlarmService {

    @Inject
    private AlarmMapper alarmMapper;

    public Alarm getAlarmByJobId(long jobId) {
        AlarmExample alarmExample = new AlarmExample();
        alarmExample.createCriteria().andJobIdEqualTo(jobId);
        List<Alarm> alarms = alarmMapper.selectByExample(alarmExample);
        if (alarms != null && alarms.size() == 1) {
            return alarms.get(0);
        }

        return null;
    }

    public int insert(Alarm alarm) {
        return alarmMapper.insertSelective(alarm);
    }

    public int updateByJobId(Alarm alarm){
        AlarmExample alarmExample = new AlarmExample();
        alarmExample.createCriteria().andJobIdEqualTo(alarm.getJobId());
        return alarmMapper.updateByExampleSelective(alarm, alarmExample);
    }

    public int deleteByJobId(long jobId) {
        AlarmExample alarmExample = new AlarmExample();
        alarmExample.createCriteria().andJobIdEqualTo(jobId);
        return alarmMapper.deleteByExample(alarmExample);
    }

}
