package com.mogujie.jarvis.web.entity.vo;

import com.mogujie.jarvis.dto.generate.Job;

/**
 * Created by hejian on 15/9/17.
 */
public class JobVo extends Job {

    private String appName;
    private String appKey;

    private Integer expressionType;
    private Integer expressionId;
    private String expression;
    private String workerGroupName;

    private String scriptId;            //脚本ID
    private String scriptTitle;         //脚本标题
    private String scriptContent;       //脚本内容

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Integer getExpressionType() {
        return expressionType;
    }

    public void setExpressionType(Integer expressionType) {
        this.expressionType = expressionType;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getWorkerGroupName() {
        return workerGroupName;
    }

    public void setWorkerGroupName(String workerGroupName) {
        this.workerGroupName = workerGroupName;
    }

    public Integer getExpressionId() {
        return expressionId;
    }

    public void setExpressionId(Integer expressionId) {
        this.expressionId = expressionId;
    }

    public String getScriptId() {
        return scriptId;
    }

    public JobVo setScriptId(String scriptId) {
        this.scriptId = scriptId;
        return this;
    }

    public String getScriptTitle() {
        return scriptTitle;
    }

    public JobVo setScriptTitle(String scriptTitle) {
        this.scriptTitle = scriptTitle;
        return this;
    }

    public String getScriptContent() {
        return scriptContent;
    }

    public JobVo setScriptContent(String scriptContent) {
        this.scriptContent = scriptContent;
        return this;
    }

}
