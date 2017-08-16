package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.core.domain.TaskStatus;
import com.mogujie.jarvis.web.entity.qo.TaskDependQo;
import com.mogujie.jarvis.web.entity.qo.TaskQo;
import com.mogujie.jarvis.web.service.TaskDependService;
import com.mogujie.jarvis.web.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/17.
 */
@Controller
@RequestMapping(value = "/api/task")
public class TaskAPIController {
    @Autowired
    TaskService taskService;

    @Autowired
    TaskDependService taskDependService;

    @RequestMapping(value = "/getTasks")
    @ResponseBody
    public Object getTasks(TaskQo taskQo) {
        Map<String, Object> result = taskService.getTasks(taskQo);
        return result;
    }


    @RequestMapping(value = "getTaskStatus")
    @ResponseBody
    public Object getJobStatus() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        TaskStatus[] taskStatuses = TaskStatus.values();
        for (TaskStatus taskStatus : taskStatuses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", taskStatus.getValue());
            map.put("text", taskStatus.getDescription());
            list.add(map);
        }

        return list;
    }

    /**
     * 获取任务依赖
     * */
    @RequestMapping(value = "/getDepend")
    @ResponseBody
    public Object getDepend(TaskDependQo taskDependQo) {
        return taskDependService.getDepended(taskDependQo);
    }

}
