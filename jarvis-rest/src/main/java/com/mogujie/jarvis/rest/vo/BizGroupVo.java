package com.mogujie.jarvis.rest.vo;

/**
 * AlarmVo类
 * @author muming
 */
public class BizGroupVo extends  AbstractVo {

    private Integer id;

    private String name;

    private Integer status;

    private String owner;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
