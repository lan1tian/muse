package com.mogujie.jarvis.web.entity.vo;


/**
 * @author  muming
 */
public class ScriptVo {

    private Integer id;

    private String title;

    private String content;

    private String creator;

    public Integer getId() {
        return id;
    }

    public ScriptVo setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public ScriptVo setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public ScriptVo setContent(String content) {
        this.content = content;
        return this;
    }

    public String getCreator() {
        return creator;
    }

    public ScriptVo setCreator(String creator) {
        this.creator = creator;
        return this;
    }
}
