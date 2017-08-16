package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.web.entity.qo.TaskHistoryQo;
import com.mogujie.jarvis.web.entity.vo.TaskHistoryVo;
import com.mogujie.jarvis.web.service.TaskHistoryService;
import com.mogujie.jarvis.web.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 16/1/13.
 */
@Controller
@RequestMapping(value = "/api/taskHistory")
public class TaskHistoryAPIController {
    @Autowired
    TaskHistoryService taskHistoryService;

    @RequestMapping(value = "getByTaskId")
    @ResponseBody
    public Object getByTaskId(TaskHistoryQo taskHistoryQo) {
        if (null == taskHistoryQo.getTaskId()) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("code", 1001);
            map.put("msg", "未传入taskId");
            map.put("supportFields", Tools.getObjectField(TaskHistoryQo.class));
            return map;
        }
        List<TaskHistoryVo> taskHistoryVoList = taskHistoryService.getByTaskId(taskHistoryQo.getTaskId());
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("rows",taskHistoryVoList);

        return map;
    }
}
