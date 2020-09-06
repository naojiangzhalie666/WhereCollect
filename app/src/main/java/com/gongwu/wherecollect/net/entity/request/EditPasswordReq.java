package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class EditPasswordReq extends RequestBase {
    private String uid;
    private String original_password;
    private String password;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getOriginal_password() {
        return original_password;
    }

    public void setOriginal_password(String original_password) {
        this.original_password = original_password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
