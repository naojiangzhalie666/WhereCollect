package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class GoodsListReq extends RequestBase {
    String uid;
    String query;
    int page;

    public GoodsListReq() {
    }

    public GoodsListReq(String uid, String query, int page) {
        this.uid = uid;
        this.query = query;
        this.page = page;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }
}
