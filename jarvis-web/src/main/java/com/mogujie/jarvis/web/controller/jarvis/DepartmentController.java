package com.mogujie.jarvis.web.controller.jarvis;

//import com.mogu.bigdata.admin.client.annotation.Passport;
import com.mogujie.jarvis.core.util.JsonHelper;
import com.mogujie.jarvis.web.auth.conf.JarvisAuthType;
import com.mogujie.jarvis.web.entity.vo.DepartmentAndBizGroupVo;
import com.mogujie.jarvis.web.mapper.DepartmentMapper;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/23
 * time: 下午5:08
 */
@Controller
@RequestMapping("/department")
public class DepartmentController {
  @Autowired
  private DepartmentMapper departmentMapper;
  @RequestMapping
//  @Passport(JarvisAuthType.department)
  public String index(ModelMap modelMap) {
    return "department/index";
  }

  /**
   * job任务新增或编辑页
   */
  @RequestMapping(value = "add")
//  @Passport(JarvisAuthType.department)
  public String addOrEdit(ModelMap modelMap) {
    return "department/add";
  }

  //单个业务类型详情
  @RequestMapping(value = "departmentDetail")
//  @Passport(JarvisAuthType.department)
  public String departmentDetail(ModelMap modelMap, Long id){
    List<DepartmentAndBizGroupVo> list = this.departmentMapper.getDepartmentById(id);
    if(list == null) {
      list = new ArrayList<>();
    }
    modelMap.put("data", JsonHelper.toJson(list));
    return "department/detail";
  }

}
