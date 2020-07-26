package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class ShareReq extends RequestBase {
    private String uid;
    private String usid;
    private int type;
    private String location_id;
    private String be_shared_user_id;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsid() {
        return usid;
    }

    public void setUsid(String usid) {
        this.usid = usid;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getBe_shared_user_id() {
        return be_shared_user_id;
    }

    public void setBe_shared_user_id(String be_shared_user_id) {
        this.be_shared_user_id = be_shared_user_id;
    }
}
