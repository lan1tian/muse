/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年3月10日 下午3:48:34
 */

package com.mogujie.jarvis.rest.jarvis;

import com.mogujie.jarvis.protocol.SearchJobProtos.SimpleJobInfoEntry;

/**
 * @author guangming
 *
 */
public class SimpleJobInfo {
    private Long id;
    private String title;
    private String publisher;

    public SimpleJobInfo(SimpleJobInfoEntry jobInfoEntry) {
        this.id = jobInfoEntry.getJobId();
        this.title = jobInfoEntry.getJobName();
        this.publisher = jobInfoEntry.getUser();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

}
