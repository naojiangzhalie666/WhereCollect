package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class GoodsDetailsReq extends RequestBase {
    private String uid;
    private String code;
    private String object_id;
    private String obj_id;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }

    public String getObj_id() {
        return obj_id;
    }

    public void setObj_id(String obj_id) {
        this.obj_id = obj_id;
    }
}
