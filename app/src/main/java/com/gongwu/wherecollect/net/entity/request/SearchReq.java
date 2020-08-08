package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class SearchReq extends RequestBase {
    private String uid;
    private String keyword;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }
}
