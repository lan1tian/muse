package com.mogujie.jarvis.web.controller.jarvis;

//import com.mogu.bigdata.admin.core.entity.User;
//import com.mogu.bigdata.admin.passport.user.UserContextHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by hejian on 15/9/14.
 */
@Controller
public class IndexController {

    @RequestMapping(value = "/")
    public String index(ModelMap mp) {
//        User user = UserContextHolder.getUser();
//        if (null == user || StringUtils.isBlank(user.getUname())) {
//            return "index";
//        } else {
//            mp.clear();
//            return "redirect:/task";
//        }
        return "index";
    }
}
