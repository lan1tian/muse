package com.mogujie.jarvis.rest.domain;

import com.mogujie.jarvis.rest.RestResult;
import org.junit.Test;

/**
 * Created by muming on 15/12/10.
 */
public class RestResult4TestEntity<T>{
        private int code;
        private String msg;
        private T data;
        public RestResult4TestEntity() {
        }
        public int getCode() {
            return code;
        }
        public void setCode(int code) {
            this.code = code;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public T getData() {
            return data;
        }
        public void setData(T data) {
            this.data = data;
        }

    @Test
    public void test() {
        RestResult result = new RestResult();
        result.setCode(1);
        result.setMsg("msg is welcome!");
    }

}
