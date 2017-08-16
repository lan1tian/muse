package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.core.domain.AlarmType;
import com.mogujie.jarvis.web.entity.qo.AlarmQo;
import com.mogujie.jarvis.web.entity.vo.AlarmVo;
import com.mogujie.jarvis.web.service.AlarmService;
import com.mogujie.jarvis.web.utils.MessageStatus;
import com.mogujie.jarvis.web.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 16/1/8.
 */
@Controller
@RequestMapping(value = "/api/alarm")
public class AlarmAPIController {
    @Autowired
    AlarmService alarmService;

    /*
    * 获取某个jobId对应报警信息
    * */
    @RequestMapping(value = "getByJobId")
    @ResponseBody
    public Object getByJobId(AlarmQo alarmQo) {
        Map<String,Object> map = new HashMap<String, Object>();
        if (null == alarmQo.getJobId()) {
            map.put("code", MessageStatus.FAILED.getValue());
            map.put("msg","未传入id");
            map.put("supportFields", Tools.getObjectField(AlarmQo.class));
            return map;
        }

        try {
            AlarmVo alarmVo = alarmService.getAlarmByJobId(alarmQo.getJobId());
            if (null == alarmVo) {
                alarmVo = new AlarmVo();
            }
            map.put("code",MessageStatus.SUCCESS.getValue());
            map.put("msg","查询成功");
            map.put("data",alarmVo);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code",MessageStatus.FAILED.getValue());
            map.put("msg", e.getMessage());
        }
        return map;
    }

    /*
    * 获取报警类型
    * */
    @RequestMapping(value = "/getAlarmType")
    @ResponseBody
    public Object getAlarmType() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        AlarmType[] alarmTypes = AlarmType.values();
        for (AlarmType alarmType : alarmTypes) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", alarmType.getValue());
            map.put("text", alarmType.getDescription());
            list.add(map);
        }
        return list;
    }
}
