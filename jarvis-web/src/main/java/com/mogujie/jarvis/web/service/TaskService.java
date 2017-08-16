package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.web.entity.qo.TaskQo;
import com.mogujie.jarvis.web.entity.vo.TaskVo;
import com.mogujie.jarvis.web.mapper.TaskMapper;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/17.
 */
@Service
public class TaskService {
    @Autowired
    private TaskMapper taskMapper;

    public TaskVo getTaskById(Long taskId) {
        TaskVo taskVo = taskMapper.getTaskById(taskId);
        return taskVo;
    }

    public Map<String, Object> getTasks(TaskQo taskQo) {
        Map<String, Object> result = new HashMap<String, Object>();

        if (StringUtils.isNotBlank(taskQo.getTaskStatusArrStr())) {
            List<String> statusList = JsonHelper.fromJson(taskQo.getTaskStatusArrStr(), List.class);

            if (statusList.size() > 0) {
                List<Integer> taskStatus = new ArrayList<Integer>();
                for (int i = 0; i < statusList.size(); i++) {
                    Integer status = Integer.parseInt(statusList.get(i));
                    taskStatus.add(status);
                }
                taskQo.setTaskStatus(taskStatus);
            }
        }

        Integer count = taskMapper.getCountByCondition(taskQo);
        count = count == null ? 0 : count;
        List<TaskVo> taskVoList = taskMapper.getTasksByCondition(taskQo);


        result.put("total", count);
        result.put("rows", taskVoList);

        return result;
    }

    public List<String> getAllExecuteUser() {
        return taskMapper.getAllExecuteUser();
    }

    public List<TaskVo> getTaskByJobIdBetweenTime(List<Long> jobIdList, int startTime, int endTime){

        DateTime now = DateTime.now();
        if(startTime == 0 && endTime ==0){
            startTime = (int) (now.withTime(0, 0, 0, 0).getMillis() / 1000);
            endTime = (int) (now.withTime(23, 59, 59, 999).getMillis() / 1000);
        }

        return taskMapper.getTaskByJobIdBetweenTime(jobIdList,startTime,endTime);
    }



}
