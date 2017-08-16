package com.mogujie.jarvis.web.entity.vo;

import com.mogujie.jarvis.dto.generate.Worker;

/**
 * Created by hejian on 15/9/28.
 */
public class WorkerVo extends Worker {
    private String workerGroupName;

    public String getWorkerGroupName() {
        return workerGroupName;
    }

    public void setWorkerGroupName(String workerGroupName) {
        this.workerGroupName = workerGroupName;
    }
}
