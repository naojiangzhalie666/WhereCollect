package com.gongwu.wherecollect.net.entity.response;

public class FeedbackBean {
    private String user_id;
    private String content;

    public String get_id() {
        return user_id;
    }

    public void set_id(String user_id) {
        this.user_id = user_id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
