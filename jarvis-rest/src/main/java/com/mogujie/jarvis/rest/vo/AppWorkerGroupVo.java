package com.mogujie.jarvis.rest.vo;

import java.util.List;

/**
 * AppWorkerGroupVo
 *
 * @author muming
 */

public class AppWorkerGroupVo extends AbstractVo {

    public static class AppWorkerGroupEntry {
        private Integer appId;
        private Integer workerGroupId;

        public Integer getAppId() {
            return appId;
        }

        public void setAppId(Integer appId) {
            this.appId = appId;
        }

        public Integer getWorkerGroupId() {
            return workerGroupId;
        }

        public void setWorkerGroupId(Integer workerGroupId) {
            this.workerGroupId = workerGroupId;
        }
    }

    private List<AppWorkerGroupEntry> list;

    public List<AppWorkerGroupEntry> getList() {
        return list;
    }

    public void setList(List<AppWorkerGroupEntry> list) {
        this.list = list;
    }
}
