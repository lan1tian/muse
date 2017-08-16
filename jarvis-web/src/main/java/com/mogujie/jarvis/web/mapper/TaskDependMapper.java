package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.vo.TaskDependVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by hejian on 15/12/8.
 */
public interface TaskDependMapper {

    List<TaskDependVo> getTaskDependByIds(@Param("list") List<Long> taskIds);

}
