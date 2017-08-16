package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.vo.TaskHistoryVo;

import java.util.List;

/**
 * Created by hejian on 16/1/11.
 */
public interface TaskHistoryMapper {
    List<TaskHistoryVo> getByTaskId(Long taskId);

}
