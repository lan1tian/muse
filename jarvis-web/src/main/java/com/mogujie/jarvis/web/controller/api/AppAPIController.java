package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.core.domain.AppStatus;
import com.mogujie.jarvis.core.domain.AppType;
import com.mogujie.jarvis.web.entity.qo.AppQo;
import com.mogujie.jarvis.web.entity.vo.AppVo;
import com.mogujie.jarvis.web.service.AppService;
import com.mogujie.jarvis.web.utils.MessageStatus;
import com.mogujie.jarvis.web.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/24.
 */
@Controller
@RequestMapping(value = "/api/app")
public class AppAPIController {
    @Autowired
    AppService appService;

    @RequestMapping(value = "getByAppId")
    @ResponseBody
    public Object getByAppId(AppQo appQo) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (null == appQo.getAppId()) {
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", "未传入appId");
            map.put("supportFields", Tools.getObjectField(AppQo.class));
            return map;
        }
        try {
            AppVo appVo = appService.getAppById(appQo.getAppId());
            map.put("code", MessageStatus.SUCCESS.getValue());
            map.put("msg", "获取app信息成功");
            map.put("data", appVo);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }

        return map;
    }

    @RequestMapping(value = "getApps")
    @ResponseBody
    public Object getApps(AppQo appQo) {
        Map<String, Object> result = appService.getApps(appQo);
        return result;
    }

    /*
    * APP的状态
    * */
    @RequestMapping(value = "getAppStatus")
    @ResponseBody
    public Object getAppStatus() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        AppStatus[] appStatuses = AppStatus.values();
        for (AppStatus appStatus : appStatuses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", appStatus.getValue());
            map.put("text", appStatus.getDescription());
            list.add(map);
        }

        return list;
    }

    /*
    * APP的类型
    * */
    @RequestMapping(value = "getAppType")
    @ResponseBody
    public Object getAppType() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        AppType[] appStatuses = AppType.values();
        for (AppType appType : appStatuses) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", appType.getValue());
            map.put("text", appType.getDescription());
            list.add(map);
        }

        return list;
    }
}

