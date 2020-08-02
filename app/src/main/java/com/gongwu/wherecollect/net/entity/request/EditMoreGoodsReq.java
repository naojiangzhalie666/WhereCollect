package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

import java.util.List;

public class EditMoreGoodsReq extends RequestBase {
    private String uid;
    private List<String> object_ids;
    private List<String> category_codes;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public List<String> getObject_ids() {
        return object_ids;
    }

    public void setObject_ids(List<String> object_ids) {
        this.object_ids = object_ids;
    }

    public List<String> getCategory_codes() {
        return category_codes;
    }

    public void setCategory_codes(List<String> category_codes) {
        this.category_codes = category_codes;
    }
}
