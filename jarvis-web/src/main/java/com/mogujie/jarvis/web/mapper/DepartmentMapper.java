package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.dto.generate.Department;
import com.mogujie.jarvis.web.entity.qo.DepartmentQo;
import com.mogujie.jarvis.web.entity.vo.DepartmentAndBizGroupVo;
import com.mogujie.jarvis.web.entity.vo.DepartmentVo;
import java.util.List;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/23
 * time: 下午7:17
 */
public interface DepartmentMapper {

  Integer getCountByCondition(DepartmentQo departmentQo);

  List<DepartmentVo> getDepartmentByCondition(DepartmentQo departmentQo);

  List<String> getSimilarDepartmentName(String name);

  List<String> getSimilarBizGroupName(String name);

  List<String> getSimilarBizGroupOwner(String name);

  List<Department> getAllByCondition(DepartmentQo departmentQo);

  List<DepartmentAndBizGroupVo> getDepartmentById(Long id);

}
