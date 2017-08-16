/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: muming
 * Create Date: 2015年12月1日 下午7:15:05
 */

package com.mogujie.jarvis.server.service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.gson.reflect.TypeToken;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.dao.generate.TaskDependMapper;
import com.mogujie.jarvis.dto.generate.TaskDepend;

/**
 * @author muming
 */
@Singleton
public class TaskDependService {

    @Inject
    private TaskDependMapper mapper;
    private static final Logger LOGGER = LogManager.getLogger();

    Type dataType = new TypeToken<Map<Long, List<Long>>>() {
    }.getType();

    public void storeParent(long taskId, Map<Long, List<Long>> dependTaskIdMap) {
        String dependJson = JsonHelper.toJson(dependTaskIdMap, dataType);
        TaskDepend newData = new TaskDepend();
        newData.setTaskId(taskId);
        newData.setDependTaskIds(dependJson);
        newData.setCreateTime(DateTime.now().toDate());

        TaskDepend oldData = mapper.selectByPrimaryKey(taskId);
        if (oldData == null) {
            newData.setChildTaskIds("{}");
            mapper.insertSelective(newData);
            LOGGER.info("insert parent task dependecy, taskId={}, parents is {}", taskId, dependJson);
        } else {
            mapper.updateByPrimaryKey(newData);
            LOGGER.info("update parent task dependecy, taskId={}, parents is {}", taskId, dependJson);
        }
    }

    public Map<Long, List<Long>> loadParent(long taskId) {
        Map<Long, List<Long>> parentTaskIdMap = Maps.newHashMap();
        TaskDepend data = mapper.selectByPrimaryKey(taskId);
        if (data != null) {
            Map<Long, List<Long>> tmpMap = JsonHelper.fromJson(data.getDependTaskIds(), dataType);
            if (tmpMap != null) {
                parentTaskIdMap = tmpMap;
            }
        }
        return parentTaskIdMap;
    }

    public void storeChild(long taskId, Map<Long, List<Long>> childTaskIdMap) {
        String childJson = JsonHelper.toJson(childTaskIdMap, dataType);

        TaskDepend oldData = mapper.selectByPrimaryKey(taskId);
        if (oldData != null) {
            oldData.setChildTaskIds(childJson);
            mapper.updateByPrimaryKey(oldData);
            LOGGER.info("update child task dependecy, taskId={}, parents is {}", taskId, childJson);
        } else {
            TaskDepend newData = new TaskDepend();
            newData.setTaskId(taskId);
            newData.setDependTaskIds("{}");
            newData.setChildTaskIds(childJson);
            newData.setCreateTime(DateTime.now().toDate());
            mapper.insertSelective(newData);
            LOGGER.info("insert child task dependecy, taskId={}, parents is {}", taskId, childJson);
        }
    }

    public Map<Long, List<Long>> loadChild(long taskId) {
        Map<Long, List<Long>> childTaskIdMap = Maps.newHashMap();
        TaskDepend data = mapper.selectByPrimaryKey(taskId);
        if (data != null) {
            Map<Long, List<Long>> tmpMap = JsonHelper.fromJson(data.getChildTaskIds(), dataType);
            if (tmpMap != null) {
                childTaskIdMap = tmpMap;
            }
        }
        return childTaskIdMap;
    }

    public void remove(long taskId) {
        mapper.deleteByPrimaryKey(taskId);
    }

    public void addChildDependency(long parentTaskId, long childJobId, long childTaskId) {
        // load old
        Map<Long, List<Long>> childTaskIdMap = loadChild(parentTaskId);
        if (childTaskIdMap.containsKey(childJobId)) {
            List<Long> childTaskIds = childTaskIdMap.get(childJobId);
            if (!childTaskIds.contains(childTaskId)) {
                childTaskIds.add(childTaskId);
            }
            childTaskIdMap.put(childJobId, childTaskIds);
        } else {
            List<Long> childTaskIds = Lists.newArrayList(childTaskId);
            childTaskIdMap.put(childJobId, childTaskIds);
        }
        // store new
        storeChild(parentTaskId, childTaskIdMap);
    }

}
