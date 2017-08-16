/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2015 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2015年9月29日 下午5:04:00
 */

package com.mogujie.jarvis.server.domain;

import com.mogujie.jarvis.core.domain.OperationMode;
import com.mogujie.jarvis.core.domain.CommonStrategy;

/**
 * @author guangming
 *
 */
public class ModifyDependEntry {

    private OperationMode operation;
    private long preJobId;
    private int commonStrategy;
    private String offsetStrategy;

    public ModifyDependEntry(OperationMode operation, long preJobId) {
        this.operation = operation;
        this.preJobId = preJobId;
        this.commonStrategy = CommonStrategy.ALL.getValue();
    }

    public ModifyDependEntry(OperationMode operation, long preJobId,
            int commonStrategy, String offsetStrategy) {
        this.operation = operation;
        this.preJobId = preJobId;
        this.commonStrategy = commonStrategy;
        this.offsetStrategy = offsetStrategy;
    }

    public OperationMode getOperation() {
        return operation;
    }

    public void setOperation(OperationMode operation) {
        this.operation = operation;
    }

    public long getPreJobId() {
        return preJobId;
    }

    public void setPreJobId(long preJobId) {
        this.preJobId = preJobId;
    }

    public int getCommonStrategy() {
        return commonStrategy;
    }

    public void setCommonStrategy(int commonStrategy) {
        this.commonStrategy = commonStrategy;
    }

    public String getOffsetStrategy() {
        return offsetStrategy;
    }

    public void setOffsetStrategy(String offsetStrategy) {
        this.offsetStrategy = offsetStrategy;
    }

}
