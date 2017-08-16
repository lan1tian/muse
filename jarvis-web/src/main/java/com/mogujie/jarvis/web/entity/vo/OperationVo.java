package com.mogujie.jarvis.web.entity.vo;

/**
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 * User: 清远
 * mail: qingyuan@mogujie.com
 * date: 16/3/1
 * time: 下午8:31
 */
public class OperationVo {

  private String title;
  private String operator;
  private String opeDate;
  private String operationType;
  private String preOperationContent;
  private String afterOperationContent;

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

  public String getOpeDate() {
    return opeDate;
  }

  public void setOpeDate(String opeDate) {
    this.opeDate = opeDate;
  }

  public String getOperationType() {
    return operationType;
  }

  public void setOperationType(String operationType) {
    this.operationType = operationType;
  }

  public String getPreOperationContent() {
    return preOperationContent;
  }

  public void setPreOperationContent(String preOperationContent) {
    this.preOperationContent = preOperationContent;
  }

  public String getAfterOperationContent() {
    return afterOperationContent;
  }

  public void setAfterOperationContent(String afterOperationContent) {
    this.afterOperationContent = afterOperationContent;
  }
}
