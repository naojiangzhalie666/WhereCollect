package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class MsgReq extends RequestBase {
    private String uid;
    private int page;
    private String content_type;
    private String type = "1";

    public MsgReq(String uid, int page,String content_type) {
        this.uid = uid;
        this.page = page;
        this.content_type=content_type;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getContent_type() {
        return content_type;
    }

    public void setContent_type(String content_type) {
        this.content_type = content_type;
    }
}
