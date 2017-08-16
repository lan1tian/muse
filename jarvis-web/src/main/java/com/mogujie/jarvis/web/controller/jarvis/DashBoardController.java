package com.mogujie.jarvis.web.controller.jarvis;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hejian on 16/3/11.
 */
@Controller
@RequestMapping("/dashboard")
public class DashBoardController{
    @RequestMapping

    public String index(ModelMap modelMap) {


        return "dashboard/index";
    }
}
