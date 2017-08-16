package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.dto.generate.Department;
import com.mogujie.jarvis.web.entity.qo.DepartmentQo;
import com.mogujie.jarvis.web.entity.vo.DepartmentAndBizGroupVo;
import com.mogujie.jarvis.web.entity.vo.DepartmentVo;
import com.mogujie.jarvis.web.mapper.DepartmentMapper;
import com.mogujie.jarvis.web.utils.CommonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/23
 * time: 下午5:46
 */
@Service
public class DepartmentService {
  @Autowired
  private DepartmentMapper departmentMapper;

  public Map<String, Object> getSimilarDepartmentName(String name) {
    List<String> names = this.departmentMapper.getSimilarDepartmentName(name);
    return CommonUtils.getSimilarMsg(names);
  }

  public Map<String, Object> getSimilarBizGroupName(String name) {
    List<String> names = this.departmentMapper.getSimilarBizGroupName(name);
    return CommonUtils.getSimilarMsg(names);
  }

  public Map<String, Object> getSimilarBizGroupOwner(String name) {
    List<String> owners = this.departmentMapper.getSimilarBizGroupOwner(name);
    return CommonUtils.getSimilarMsg(owners);
  }

  public Map<String, Object> getDepartments(DepartmentQo departmentQo) {
    Map<String, Object> result = new HashMap<String, Object>();
    Integer count = this.departmentMapper.getCountByCondition(departmentQo);
    count = count == null ? 0 : count;

    List<DepartmentVo> departmentQoList = this.departmentMapper.getDepartmentByCondition(departmentQo);

    List<DepartmentVo> resultDepartmentList = new ArrayList<>();
    for(DepartmentVo vo : departmentQoList) {
      if(resultDepartmentList.size() == 0) {
        resultDepartmentList.add(vo);
        continue;
      }

      boolean isInsert = true;
      for(DepartmentVo departmentVo : resultDepartmentList) {
        if(vo.getDepartmentName().equals(departmentVo.getDepartmentName())) {
          departmentVo.setBizGroupName(new StringBuilder(departmentVo.getBizGroupName()).append(",").append(vo.getBizGroupName()).toString());
          isInsert = false;
          break;
        }
      }

      if(isInsert) {
        resultDepartmentList.add(vo);
      }
    }

    result.put("total", count);
    result.put("rows", resultDepartmentList);

    return result;
  }

  public List<Department> getAllByCondition(DepartmentQo departmentQo) {
    return this.departmentMapper.getAllByCondition(departmentQo);
  }

  public List<DepartmentAndBizGroupVo> getDepartmentAndBizGroupVoById(Long id) {
    List<DepartmentAndBizGroupVo> departmentAndBizGroupVoList = this.departmentMapper.getDepartmentById(id);
    return departmentAndBizGroupVoList;
  }
}
