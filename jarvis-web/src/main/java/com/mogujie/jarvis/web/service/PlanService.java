package com.mogujie.jarvis.web.service;

import com.alibaba.fastjson.JSONObject;
import com.mogujie.jarvis.web.entity.qo.PlanQo;
import com.mogujie.jarvis.web.entity.vo.PlanVo;
import com.mogujie.jarvis.web.entity.vo.TaskVo;
import com.mogujie.jarvis.web.mapper.PlanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian,muming on 15/10/21.
 */
@Service
public class PlanService {
    @Autowired
    PlanMapper planMapper;

    @Autowired
    TaskService taskService;

    /**
     * @param planQo
     * @func 根据条件获取执行计划
     * @author hejian
     */
    public Map<String, Object> getPlans(PlanQo planQo) {
        Map<String, Object> result = new HashMap<String, Object>();


//        List<String> taskStatusList = planQo.getTaskStatusList();
//        //包含未初始化的job
//        for (String status : taskStatusList) {
//            if (status.equals("0")) {
//                planQo.setUnInitial(true);
//                break;
//            }
//        }

        Integer total = planMapper.getPlanCountByCondition(planQo);
        List<PlanVo> planVoList = planMapper.getPlansByCondition(planQo);

        List<Long> jobIdList = new ArrayList<>();
        for (PlanVo planVo : planVoList) {
            if (null != planVo.getJobId()) {
                jobIdList.add(planVo.getJobId());
            }
        }
        List<TaskVo> taskVoList = new ArrayList<>();
        if (jobIdList.size() > 0) {
            taskVoList = planMapper.getRecentTasks(jobIdList);
        }

        Map<Long, List<Long>> avgTime = new HashMap<>();
        for (TaskVo taskVo : taskVoList) {
            List<Long> item = avgTime.get(taskVo.getJobId());
            if (null == item) {
                item = new ArrayList<>();
                Long intervalSecond = 0l;
                if (null != taskVo.getExecuteEndTime() && null != taskVo.getExecuteStartTime()) {
                    intervalSecond = (taskVo.getExecuteEndTime().getTime() - taskVo.getExecuteStartTime().getTime()) / 1000;
                }
                item.add(intervalSecond);
                avgTime.put(taskVo.getJobId(), item);
            } else {
                Long intervalSecond = 0l;
                if (null != taskVo.getExecuteEndTime() && null != taskVo.getExecuteStartTime()) {
                    intervalSecond = (taskVo.getExecuteEndTime().getTime() - taskVo.getExecuteStartTime().getTime()) / 1000;
                }
                item.add(intervalSecond);
            }
        }

        for (PlanVo planVo : planVoList) {
            List<Long> item = avgTime.get(planVo.getJobId());
            Long avgSecond = null;
            if (item != null && item.size() > 0) {
                Long totalSecond = 0l;
                for (Long second : item) {
                    totalSecond += second;
                }
                avgSecond = totalSecond / item.size();
            }

            planVo.setAverageExecuteTime(avgSecond);
        }


        result.put("total", total);
        result.put("rows", planVoList);

        return result;
    }
}
