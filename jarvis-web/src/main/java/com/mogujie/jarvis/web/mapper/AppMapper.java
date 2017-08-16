package com.mogujie.jarvis.web.mapper;


import com.mogujie.jarvis.web.entity.qo.AppQo;
import com.mogujie.jarvis.web.entity.vo.AppVo;

import java.util.List;

/**
 * Created by hejian on 15/9/24.
 */
public interface AppMapper {
    AppVo getAppById(Integer appId);

    List<String> getAllAppName();

    Integer getAppCount(AppQo appQo);

    List<AppVo> getAppList(AppQo appQo);

    AppVo getAppByName(String appName);
}
