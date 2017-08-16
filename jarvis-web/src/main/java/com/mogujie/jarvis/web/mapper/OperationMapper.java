package com.mogujie.jarvis.web.mapper;

import com.mogujie.jarvis.web.entity.qo.OperationQo;
import com.mogujie.jarvis.web.entity.vo.OperationVo;
import java.util.List;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/1
 * time: 下午8:49
 */
public interface OperationMapper {
  Integer getCountByCondition(OperationQo operationQo);

  List<OperationVo> getOperationsByCondition(OperationQo operationQo);

  List<String> getAllOperationTitles();

  List<String> getSimilarOperationTitle(String title);

  List<String> getAllOperators();



}
