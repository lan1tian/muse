package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.qo.PlanQo;
import com.mogujie.jarvis.web.entity.qo.TaskQo;
import com.mogujie.jarvis.web.entity.vo.PlanVo;
import com.mogujie.jarvis.web.entity.vo.TaskVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Set;

/**
 * Created by muming on 15/9/17.
 */
public interface PlanMapper {

    Integer getPlanCountByCondition(PlanQo planQo);

    List<PlanVo> getPlansByCondition(PlanQo planQo);

    List<TaskVo> getRecentTasks(List<Long> jobIdList);

}
