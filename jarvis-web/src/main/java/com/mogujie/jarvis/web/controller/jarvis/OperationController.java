package com.mogujie.jarvis.web.controller.jarvis;

//import com.mogu.bigdata.admin.client.annotation.Passport;
import com.mogujie.jarvis.web.auth.conf.JarvisAuthType;
import com.mogujie.jarvis.web.service.OperationService;
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
 * date: 16/3/1
 * time: 下午6:56
 */
@Controller
@RequestMapping("/operation")
public class OperationController{
  @Autowired
  OperationService operationService;
  /**
   * job任务管理首页
   */
  @RequestMapping
//  @Passport(JarvisAuthType.operation)
  public String index(ModelMap modelMap) {
    List<String> titles = this.operationService.getAllOperationTitles();
    List<String> operators = this.operationService.getAllOperators();
    modelMap.put("titles", titles);
    modelMap.put("operators", operators);
    return "operation/index";
  }
}
