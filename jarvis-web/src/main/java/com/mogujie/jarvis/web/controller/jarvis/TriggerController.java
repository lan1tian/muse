package com.mogujie.jarvis.web.controller.jarvis;

//import com.mogu.bigdata.admin.client.annotation.Passport;
import com.mogujie.jarvis.web.auth.conf.JarvisAuthType;
import com.mogujie.jarvis.web.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Created by hejian on 15/9/15.
 */
@Controller
@RequestMapping("/trigger")
public class TriggerController {

    @Autowired
    JobService jobService;

    @RequestMapping
//    @Passport(JarvisAuthType.trigger)
    public String index() {
        return "trigger/index";
    }


}
