package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.core.domain.WorkerGroupStatus;
import com.mogujie.jarvis.web.entity.qo.WorkerGroupQo;
import com.mogujie.jarvis.web.entity.vo.WorkerGroupVo;
import com.mogujie.jarvis.web.service.WorkerGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 16/1/8.
 */
@Controller
@RequestMapping(value = "/api/workerGroup")
public class WorkerGroupAPIController {
    @Autowired
    WorkerGroupService workerGroupService;


    /*
    * 获取Id对应的workerGroup
    * */
    @RequestMapping(value = "getById")
    @ResponseBody
    public WorkerGroupVo getById(WorkerGroupQo workerGroupQo) {

        Field[] fields=WorkerGroupAPIController.class.getDeclaredFields();

        WorkerGroupVo workerGroupVo = workerGroupService.getWorkerGroupById(workerGroupQo.getId());
        return workerGroupVo;
    }

    /*
    * 获取appId对应的workerGroup
    * */
    @RequestMapping(value = "getByAppId")
    @ResponseBody
    public Object getByAppId(WorkerGroupQo workerGroupQo) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<WorkerGroupVo> list = workerGroupService.getByAppId(workerGroupQo.getAppId());
        map.put("rows", list);
        return map;
    }

    /*
    * 获取所有workerGroup
    * */
    @RequestMapping(value = "getAllWorkerGroup")
    @ResponseBody
    public Object getAllWorkerGroups() {
        List<WorkerGroupVo> list = workerGroupService.getAllWorkerGroup();
        return list;
    }

    /*
    * 分页获取workerGroup
    * */
    @RequestMapping(value = "/getWorkerGroups")
    @ResponseBody
    public Object getWorkerGroups(WorkerGroupQo workerGroupQo) {
        Map<String, Object> result;
        result = workerGroupService.getWorkerGroups(workerGroupQo);
        return result;
    }

    /*
    * 获取workerGroup的所有可选状态
    * */
    @RequestMapping(value = "getWorkerGroupStatus")
    @ResponseBody
    public Object getWorkerGroupStatus() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        WorkerGroupStatus[] workerGroupStatuses = WorkerGroupStatus.values();
        for (WorkerGroupStatus workerGroupStatus : workerGroupStatuses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", workerGroupStatus.getValue());
            map.put("text", workerGroupStatus.getDescription());
            list.add(map);
        }

        return list;
    }
}
