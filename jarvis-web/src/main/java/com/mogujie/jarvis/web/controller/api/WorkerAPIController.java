package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.core.domain.WorkerStatus;
import com.mogujie.jarvis.web.entity.qo.WorkerQo;
import com.mogujie.jarvis.web.service.WorkerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/28.
 */
@Controller
@RequestMapping(value = "/api/worker")
public class WorkerAPIController {
    @Autowired
    WorkerService workerService;

    @RequestMapping(value = "/getWorkers")
    @ResponseBody
    public Object getWorkers(WorkerQo workerSearchVo) {
        Map<String, Object> result;
        result = workerService.getWorkers(workerSearchVo);
        return result;
    }

    @RequestMapping(value = "getWorkerStatus")
    @ResponseBody
    public Object getWorkerStatus() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        WorkerStatus[] workerStatuses = WorkerStatus.values();
        for (WorkerStatus workerStatus : workerStatuses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", workerStatus.getValue());
            map.put("text", workerStatus.getDescription());
            list.add(map);
        }

        return list;
    }

}
