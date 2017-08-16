package com.mogujie.jarvis.web.controller.jarvis;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by hejian on 15/10/8.
 */
@Controller
public class ErrorController {

    @RequestMapping(value = "/error", method = RequestMethod.GET)
    public String error(ModelMap modelMap, String message) {
        modelMap.put("message", message);
        return "common/error";
    }
}
