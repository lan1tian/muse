/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月26日 下午5:48:01
 */

package com.mogujie.jarvis.rest.jarvis;

import java.util.List;

/**
 * @author guangming
 *
 */
public class CriticalPathResult extends Result {
    private static final long serialVersionUID = -6973315002028942220L;
    private List<TaskInfo> list;

    public List<TaskInfo> getList() {
        return list;
    }

    public void setList(List<TaskInfo> list) {
        this.list = list;
    }
}
