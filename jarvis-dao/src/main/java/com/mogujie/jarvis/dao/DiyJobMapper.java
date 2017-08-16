/*
 * 蘑菇街 Inc.
 * Copyright (c) 2010-2016 All Rights Reserved.
 *
 * Author: guangming
 * Create Date: 2016年2月25日 上午10:21:34
 */

package com.mogujie.jarvis.dao;

import com.mogujie.jarvis.dto.generate.Job;

/**
 * @author guangming
 *
 */
public interface DiyJobMapper {
    Job selectByScriptId(int scriptId);

    // add by @qingyuan
    Job selectByScriptTitle(String title);
}
