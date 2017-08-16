package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.web.entity.qo.WorkerGroupQo;
import com.mogujie.jarvis.web.entity.qo.WorkerQo;
import com.mogujie.jarvis.web.entity.vo.WorkerGroupVo;
import com.mogujie.jarvis.web.entity.vo.WorkerVo;
import com.mogujie.jarvis.web.mapper.WorkerGroupMapper;
import com.mogujie.jarvis.web.mapper.WorkerMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hejian on 15/9/28.
 */

@Service
public class WorkerGroupService {
    @Autowired
    WorkerGroupMapper workerGroupMapper;

    /*
    * 获取
    * */
    public List<WorkerGroupVo> getAllWorkerGroup() {
        return workerGroupMapper.getAllWorkerGroup();
    }

    /*
    * 根据id获取workerGroup信息
    * */
    public WorkerGroupVo getWorkerGroupById(Integer id) {
        return workerGroupMapper.getWorkerGroupById(id);
    }

    public WorkerGroupVo getWorkerGroupByName(String name) {
        return workerGroupMapper.getWorkerGroupByName(name);
    }

    /*
    * 根据条件获取workerGroup数量
    * */
    public Integer getWorkerGroupCount(WorkerGroupQo workerGroupSearchVo) {
        return workerGroupMapper.getWorkerGroupCount(workerGroupSearchVo);
    }

    /*
    * 根据条件获取WorkerGroup列表
    * */
    public Map<String, Object> getWorkerGroups(WorkerGroupQo workerGroupSearchVo) {
        Integer total = workerGroupMapper.getWorkerGroupCount(workerGroupSearchVo);
        List<WorkerGroupVo> workerGroupVoList = workerGroupMapper.getWorkerGroupList(workerGroupSearchVo);

        Map<String, Object> result = new HashMap<String, Object>();
        result.put("total", total);
        result.put("rows", workerGroupVoList);
        return result;
    }

    /*
    * 获取所有WorkerGroup的创建者
    * */
    public List<String> getAllWorkerGroupCreator() {
        return workerGroupMapper.getAllWorkerGroupCreator();
    }

    public List<WorkerGroupVo> getByAppId(Long appId){
        return workerGroupMapper.getByAppId(appId);
    }

}
