package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.web.entity.vo.AlarmVo;
import com.mogujie.jarvis.web.mapper.AlarmMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hejian on 15/12/17.
 */
@Service
public class AlarmService {
    @Autowired
    AlarmMapper alarmMapper;

    public AlarmVo getAlarmByJobId(Long jobId){
        return alarmMapper.getAlarmByJobId(jobId);
    }

}
