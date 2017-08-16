package com.mogujie.jarvis.web.controller.api;

import com.mogu.bigdata.admin.core.entity.User;

import com.mogujie.jarvis.web.service.TaskService;

import com.mogujie.jarvis.web.utils.MessageStatus;
import org.mogujie.ppcenter.sdk.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 16/1/14.
 */
@Controller
@RequestMapping(value = "/api/common")
public class CommonAPIController {

    @Autowired
    TaskService taskService;


    /*
    * 获取内网所有用户
    * */
    @RequestMapping(value = "/getAllUser")
    @ResponseBody
    public Object getAllUser() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> allUsers = UserService.getAllUsers();
            List<User> userList = new ArrayList<User>();
            for(Map<String, Object> m: allUsers) {
                User user = new User();
                user.setUid(Long.valueOf(m.get("workId").toString()));
                user.setUname(m.get("domain").toString());
                user.setNick(m.get("nickname").toString());
                user.setEmail(m.get("email").toString());
                userList.add(user);
            }
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg","查询用户信息成功");
            map.put("rows",userList);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",MessageStatus.FAILED.getValue());
            map.put("msg",e.getMessage());
        }
        return map;
    }

    /*
    * 获取返回状态码说明
    * */
    @RequestMapping(value = "/getMessageStatus")
    @ResponseBody
    public Object getMessageStatus(){
        Map<Object, Object> map = new HashMap<Object, Object>();
        MessageStatus[] messageStatuses=MessageStatus.values();
        for(MessageStatus messageStatus:messageStatuses){
            map.put(messageStatus.getValue(),messageStatus.getText());
        }
        return map;
    }

    @RequestMapping(value = "getExecuteUsers")
    @ResponseBody
    public Object getExecuteUsers() {
        List<String> executeUserList = taskService.getAllExecuteUser();

        return executeUserList;
    }

}
