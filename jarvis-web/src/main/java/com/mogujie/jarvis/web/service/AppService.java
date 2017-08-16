package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.web.entity.qo.AppQo;
import com.mogujie.jarvis.web.entity.vo.AppVo;
import com.mogujie.jarvis.web.mapper.AppMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/24.
 */
@Service
public class AppService {
    @Autowired
    AppMapper appMapper;

    //获取应用列表
    public Map<String, Object> getApps(AppQo appQo) {
        Map<String, Object> result = new HashMap<String, Object>();

        Integer count = appMapper.getAppCount(appQo);
        List<AppVo> appVoList = appMapper.getAppList(appQo);

        result.put("total", count);
        result.put("rows", appVoList);

        return result;
    }

    public List<AppVo> getAppList(AppQo appQo) {
        List<AppVo> appVoList = appMapper.getAppList(appQo);
        return appVoList;
    }

    public List<String> getAllAppName() {
        return appMapper.getAllAppName();
    }

    public AppVo getAppById(Integer appId) {
        return appMapper.getAppById(appId);
    }

    public AppVo getAppByName(String appName) {
        return appMapper.getAppByName(appName);
    }

}
