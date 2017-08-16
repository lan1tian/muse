package com.mogujie.jarvis.web.entity.qo;

import com.mogujie.jarvis.core.util.JsonHelper;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/1
 * time: 下午8:45
 */
public class OperationQo {
  private String startOperDate;
  private String endOperDate;
  private List<String> titleList;
  private String title;
  private String opeDate;
  private String operator;
  private String operationType;
  private List<String> operatorList;

  private Integer offset;
  private Integer limit;
  private String order;

  public String getStartOperDate() {
    return startOperDate;
  }

  public void setStartOperDate(String startOperDate) {
    this.startOperDate = startOperDate;
  }

  public String getEndOperDate() {
    return endOperDate;
  }

  public void setEndOperDate(String endOperDate) {
    this.endOperDate = endOperDate;
  }

  public List<String> getTitleList() {
    return titleList;
  }

  public void setTitleList(String titleList) {
    if(StringUtils.isNotBlank(titleList)) {
      List<String> list = JsonHelper.fromJson(titleList, List.class);
      if (list.size() > 0) {
        this.titleList = list;
      }
    }
  }

  public List<String> getOperatorList() {
    return operatorList;
  }

  public void setOperatorList(String operatorList) {
    if(StringUtils.isNotBlank(operatorList)) {
      List<String> list = JsonHelper.fromJson(operatorList, List.class);
      if(list.size() > 0) {
        this.operatorList = list;
      }
    }
  }

  public String getOpeDate() {
    return opeDate;
  }

  public void setOpeDate(String opeDate) {
    this.opeDate = opeDate;
  }

  public Integer getOffset() {
    return offset;
  }

  public void setOffset(Integer offset) {
    this.offset = offset;
  }

  public Integer getLimit() {
    return limit;
  }

  public void setLimit(Integer limit) {
    this.limit = limit;
  }

  public String getOrder() {
    return order;
  }

  public void setOrder(String order) {
    this.order = order;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getOperator() {
    return operator;
  }

  public void setOperator(String operator) {
    this.operator = operator;
  }

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }


}
