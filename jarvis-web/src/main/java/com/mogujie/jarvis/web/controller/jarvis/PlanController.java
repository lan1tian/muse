package com.mogujie.jarvis.web.controller.jarvis;

//import com.mogu.bigdata.admin.client.annotation.Passport;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.web.auth.conf.JarvisAuthType;
import com.mogujie.jarvis.web.entity.qo.PlanQo;
import com.mogujie.jarvis.web.service.JobService;
import com.mogujie.jarvis.web.service.TaskDependService;
import com.mogujie.jarvis.web.service.TaskService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * Created by hejian on 15/9/15.
 */
@Controller
@RequestMapping("/plan")
public class PlanController {
    @Autowired
    JobService jobService;
    @Autowired
    TaskService taskService;
    @Autowired
    TaskDependService taskDependService;

    /**
     * 执行计划首页
     */
    @RequestMapping
//    @Passport(JarvisAuthType.plan)
    public String index(ModelMap modelMap) {
        PlanQo planQo = new PlanQo();
        planQo.setScheduleDate(DateTime.now().toString("yyyy-MM-dd"));
        modelMap.put("planQo", JsonHelper.toJson(planQo));
        return "plan/index";
    }

    @RequestMapping("detail")
//    @Passport(JarvisAuthType.plan)
    public String detail(ModelMap modelMap,Long jobId,Long scheduleTime) {
        modelMap.put("jobId",jobId);
        modelMap.put("scheduleTime",scheduleTime);
        return "plan/detail";
    }
}
