package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class BindPhoneReq extends RequestBase {
    private String uid;
    private String type;
    private String mobile;
    private String code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
