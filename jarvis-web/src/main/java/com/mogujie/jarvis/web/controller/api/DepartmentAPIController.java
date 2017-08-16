package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.dto.generate.Department;
import com.mogujie.jarvis.web.entity.qo.DepartmentQo;
import com.mogujie.jarvis.web.service.DepartmentService;
import com.mogujie.jarvis.web.utils.MessageStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/23
 * time: 下午8:33
 */
@Controller
@RequestMapping(value = "/api/department")
public class DepartmentAPIController {

  @Autowired
  private DepartmentService departmentService;

  @RequestMapping("/getSimilarDepartmentName")
  @ResponseBody
  public Object getSimilarDepartmentName(String q) {
    Map<String, Object> map = this.departmentService.getSimilarDepartmentName(q);
    return map;
  }

  @RequestMapping("/getSimilarBizGroupName")
  @ResponseBody
  public Object getSimilarBizGroupName(String q) {
    Map<String, Object> map = this.departmentService.getSimilarBizGroupName(q);
    return map;
  }

  @RequestMapping("/getSimilarBizGroupOwner")
  @ResponseBody
  public Object getSimilarBizGroupOwner(String q) {
    Map<String, Object> map = this.departmentService.getSimilarBizGroupOwner(q);
    return map;
  }

  @RequestMapping("/getDepartments")
  @ResponseBody
  public Object getDepartments(DepartmentQo departmentQo) {
    Map<String, Object> map = this.departmentService.getDepartments(departmentQo);
    return map;
  }

  @RequestMapping("/getAllNames")
  @ResponseBody
  public Object getAllNames(DepartmentQo departmentQo) {
    Map<String, Object> map = new HashMap();
    try {
      List<Department> list = this.departmentService.getAllByCondition(departmentQo);
      map.put("code", MessageStatus.SUCCESS.getValue());
      map.put("msg", MessageStatus.SUCCESS.getText());
      map.put("data", list);
    } catch (Exception e) {
      e.printStackTrace();
      map.put("code", MessageStatus.FAILED.getValue());
      map.put("msg", MessageStatus.SUCCESS.getText());
    }
    return map;
  }
}
