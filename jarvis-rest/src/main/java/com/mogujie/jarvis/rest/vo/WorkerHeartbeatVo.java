package com.mogujie.jarvis.rest.vo;

import java.util.List;

/**
 * worker心跳返回类
 * @author muming
 */
public class WorkerHeartbeatVo extends  AbstractVo{

    private List<WorkerHeartbeatEntry> list;

    public List<WorkerHeartbeatEntry> getList() {
        return list;
    }
    public void setList(List<WorkerHeartbeatEntry> list) {
        this.list = list;
    }
    public static class WorkerHeartbeatEntry {
        private String ip;
        private int port;
        private int taskNum;

        public String getIp() {
            return ip;
        }

        public WorkerHeartbeatEntry setIp(String ip) {
            this.ip = ip;
            return  this;
        }

        public int getPort() {
            return port;
        }

        public WorkerHeartbeatEntry setPort(int port) {
            this.port = port;
            return this;
        }

        public int getTaskNum() {
            return taskNum;
        }

        public WorkerHeartbeatEntry setTaskNum(int taskNum) {
            this.taskNum = taskNum;
            return this;
        }
    }
}
