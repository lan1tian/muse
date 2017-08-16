package com.mogujie.jarvis.web.service;

import com.mogujie.jarvis.core.domain.OperationInfo;
import com.mogujie.jarvis.web.entity.qo.OperationQo;
import com.mogujie.jarvis.web.entity.vo.OperationVo;
import com.mogujie.jarvis.web.mapper.OperationMapper;
import com.mogujie.jarvis.web.utils.CommonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/1
 * time: 下午8:04
 */
@Service
public class OperationService {
  @Autowired
  private OperationMapper operationMapper;

  Logger logger = Logger.getLogger(this.getClass());

  public Map<String, Object> getOperations(OperationQo operationQo) {
    Map<String, Object> result = new HashMap<String, Object>();
    Integer count = operationMapper.getCountByCondition(operationQo);
    count = count == null ? 0 : count;

    List<OperationVo> operationVoList = this.operationMapper.getOperationsByCondition(operationQo);

    for(OperationVo operationVo : operationVoList) {
      operationVo.setOperationType(OperationInfo.valueOf(operationVo.getOperationType().toUpperCase()).getDescription());
    }

    result.put("total", count);
    result.put("rows", operationVoList);

    return result;
  }

  public List<String> getAllOperationTitles() {
    return this.operationMapper.getAllOperationTitles();
  }

  public Map<String, Object> getSimilarOperationTitle(String title) {
    List<String> titles = this.operationMapper.getSimilarOperationTitle(title);

    return CommonUtils.getSimilarMsg(titles);
  }

  public List<String> getAllOperators() {
    return this.operationMapper.getAllOperators();
  }

}
