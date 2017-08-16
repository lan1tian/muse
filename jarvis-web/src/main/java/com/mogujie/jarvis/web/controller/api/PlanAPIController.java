package com.mogujie.jarvis.web.controller.api;

import com.mogujie.bigdata.base.domain.JSONReturn;
import com.mogujie.jarvis.web.entity.qo.JobDependQo;
import com.mogujie.jarvis.web.entity.qo.PlanQo;
import com.mogujie.jarvis.web.entity.utils.MessageCode;
import com.mogujie.jarvis.web.entity.vo.JobDependVo;
import com.mogujie.jarvis.web.service.*;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian,muming on 15/10/21.
 */
@Controller
@RequestMapping(value = "/api/plan")
public class PlanAPIController {
    @Autowired
    PlanService planService;
    @Autowired
    TaskDependService taskDependService;
    @Autowired
    JobDependService jobDependService;
    @Autowired
    TaskService taskService;
    @Autowired
    JobService jobService;

    DateTimeFormatter dateTimeFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /*
    * 分页获取执行计划
    * */
    @RequestMapping(value = "getPlans")
    @ResponseBody
    public Object getPlans(PlanQo planQo) {
        Map<String, Object> result = new HashMap<>();
        result = planService.getPlans(planQo);

        return result;
    }

    @RequestMapping(value = "planReason")
    @ResponseBody
    public Object planReason(Long jobId, Long scheduleTime) {
        JSONReturn jsonReturn = new JSONReturn();
        if (scheduleTime > System.currentTimeMillis()) {
            jsonReturn.setRespCode(MessageCode.normal.getCode());
            jsonReturn.setRespMsg("成功");
            jsonReturn.setRetCont("调度时间未到,调度时间为 " + dateTimeFormat.print(scheduleTime) + ",当前时间为 " + dateTimeFormat.print(System.currentTimeMillis()));
            return jsonReturn;
        } else {
            jsonReturn.setRespCode(MessageCode.normal.getCode());
            jsonReturn.setRespMsg("成功");
            jsonReturn.setRetCont("依赖未满足");
            return jsonReturn;
        }

    }

    @RequestMapping(value = "getDependDetail")
    @ResponseBody
    public Object getDependDetail(Long jobId) {
        JobDependQo query = new JobDependQo();
        query.setJobId(jobId);
        JobDependVo jobDependVo = jobDependService.getDepended(query);

        return jobDependVo;
    }

}
