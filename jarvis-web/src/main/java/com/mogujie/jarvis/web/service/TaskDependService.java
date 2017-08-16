package com.mogujie.jarvis.web.service;

import com.google.gson.reflect.TypeToken;

import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.web.entity.qo.TaskDependQo;
import com.mogujie.jarvis.web.entity.vo.*;
import com.mogujie.jarvis.web.mapper.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * @author muming ,hejian
 */
@Service
public class TaskDependService {
    @Autowired
    TaskDependMapper taskDependMapper;
    @Autowired
    JobMapper jobMapper;
    @Autowired
    JobDependMapper jobDependMapper;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    TaskHistoryMapper taskHistoryMapper;

    Logger logger = Logger.getLogger(TaskDependService.class);

    static Type dependMapType = new TypeToken<Map<String, List<Long>>>() {
    }.getType();

    /**
     * 获取——task依赖
     */
    public TaskDependVo getDepended(TaskDependQo query) {

        TaskDependVo result;
        result = this.getTaskDependVoById(query.getTaskId());
        if (result == null) {
            return null;
        }
        result.setRootFlag(true);
        Map<String, List<Long>> parentTaskIds = JsonHelper.fromJson(result.getDependTaskIds(), dependMapType);
        Map<String, List<Long>> childTaskIds = JsonHelper.fromJson(result.getChildTaskIds(), dependMapType);

        //获取所有taskDepend
        List<Long> taskIds = new ArrayList<Long>();
        for (List<Long> taskIdList : parentTaskIds.values()) {
            taskIds.addAll(taskIdList);
        }
        for (List<Long> taskIdList : childTaskIds.values()) {
            taskIds.addAll(taskIdList);
        }
        if(taskIds.size() > 0){
            List<TaskDependVo> taskDepends = taskDependMapper.getTaskDependByIds(taskIds);
            if(taskDepends != null){
                for(TaskDependVo taskDepend : taskDepends){
                    Long jobId = taskDepend.getJobId();
                    if(parentTaskIds.containsKey(jobId.toString())){
                        taskDepend.setParentFlag(true);
                        result.getParents().add(taskDepend);
                    }else {
                        result.getChildren().add(taskDepend);
                    }
                }
            }
        }


        return result;
    }

    private TaskDependVo getTaskDependVoById(long taskId) {
        List<Long> ids = new ArrayList<Long>();
        ids.add(taskId);
        List<TaskDependVo> data = taskDependMapper.getTaskDependByIds(ids);
        if (data == null) {
            return null;
        }
        return data.get(0);
    }

}
