package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class MsgReq extends RequestBase {
    private String uid;
    private int page;
    private String type = "1";

    public MsgReq(String uid, int page) {
        this.uid = uid;
        this.page = page;
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
}
