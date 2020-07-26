package com.gongwu.wherecollect.net.entity.request;

import androidx.annotation.NonNull;

import com.gongwu.wherecollect.util.JsonUtils;

public class SharedUserReq {
    private String user_id;
    private boolean valid;

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    @NonNull
    @Override
    public String toString() {
        return JsonUtils.jsonFromObject(this);
    }
}
