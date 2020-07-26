package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class FurnitureDetailsReq extends RequestBase {
    private String uid;
    private String family_code;
    private String code;

    public FurnitureDetailsReq() {
    }

    public FurnitureDetailsReq(String uid, String family_code, String code) {
        this.uid = uid;
        this.family_code = family_code;
        this.code = code;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFamily_code() {
        return family_code;
    }

    public void setFamily_code(String family_code) {
        this.family_code = family_code;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
