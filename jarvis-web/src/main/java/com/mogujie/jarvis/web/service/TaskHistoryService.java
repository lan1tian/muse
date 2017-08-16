package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.web.entity.vo.TaskHistoryVo;
import com.mogujie.jarvis.web.mapper.TaskHistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hejian on 16/1/13.
 */
@Service
public class TaskHistoryService {
    @Autowired
    TaskHistoryMapper taskHistoryMapper;
    public List<TaskHistoryVo> getByTaskId(Long taskId){
        return taskHistoryMapper.getByTaskId(taskId);
    }
}
