package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.qo.WorkerGroupQo;
import com.mogujie.jarvis.web.entity.vo.WorkerGroupVo;

import java.util.List;

/**
 * Created by hejian on 16/1/8.
 */
public interface WorkerGroupMapper {
    //WorkerGroup
    List<WorkerGroupVo> getAllWorkerGroup();

    WorkerGroupVo getWorkerGroupById(Integer id);

    WorkerGroupVo getWorkerGroupByName(String name);

    Integer getWorkerGroupCount(WorkerGroupQo workerGroupSearchVo);

    List<WorkerGroupVo> getWorkerGroupList(WorkerGroupQo workerGroupQo);
    List<WorkerGroupVo> getByAppId(Long appId);

    List<String> getAllWorkerGroupCreator();
}
