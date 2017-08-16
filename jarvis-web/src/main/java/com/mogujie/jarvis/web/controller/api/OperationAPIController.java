package com.mogujie.jarvis.web.controller.api;

import com.mogujie.jarvis.web.entity.qo.JobQo;
import com.mogujie.jarvis.web.entity.qo.OperationQo;
import com.mogujie.jarvis.web.service.OperationService;
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
 * date: 16/3/1
 * time: 下午8:58
 */
@Controller
@RequestMapping(value = "/api/operation")
public class OperationAPIController {
  @Autowired
  OperationService operationService;

  @RequestMapping("/getOperations")
  @ResponseBody
  public Object getOperations(OperationQo operationQo) {
    Map<String, Object> map = this.operationService.getOperations(operationQo);
    return map;
  }

  @RequestMapping("/getAllOperators")
  @ResponseBody
  public List<String> getAllOperators() {
    List<String> operators = this.operationService.getAllOperators();
    return operators;
  }

  @RequestMapping("/getSimilarOperationTitles")
  @ResponseBody
  public Object getSimilarOperationTitles(String q) {
    Map<String, Object> map = this.operationService.getSimilarOperationTitle(q);
    return map;
  }


}
