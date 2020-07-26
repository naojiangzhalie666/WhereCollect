package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

import java.util.List;

public class LayerTemplateReq extends RequestBase {

    private String uid;
    private String system_furniture_code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getSystem_furniture_code() {
        return system_furniture_code;
    }

    public void setSystem_furniture_code(String system_furniture_code) {
        this.system_furniture_code = system_furniture_code;
    }
}
