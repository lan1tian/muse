package com.mogujie.jarvis.web.entity.vo;

import java.util.Map;

/**
 * Created by hejian on 16/3/23.
 */
public class JSTreeNode {
    private String id;
    private Object children;
    private String text;
    private String type;
    private Map li_attr;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Object getChildren() {
        return children;
    }

    public void setChildren(Object children) {
        this.children = children;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map getLi_attr() {
        return li_attr;
    }

    public void setLi_attr(Map li_attr) {
        this.li_attr = li_attr;
    }
}

