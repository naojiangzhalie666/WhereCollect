package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

import java.util.List;

public class AddShareReq extends RequestBase {
    private String uid;
    private String family_code;
    private String family_id;
    private List<String> room_codes;
    private List<SharedUserReq> be_shared_users;

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

    public String getFamily_id() {
        return family_id;
    }

    public void setFamily_id(String family_id) {
        this.family_id = family_id;
    }

    public List<String> getRoom_codes() {
        return room_codes;
    }

    public void setRoom_codes(List<String> room_codes) {
        this.room_codes = room_codes;
    }

    public List<SharedUserReq> getBe_shared_users() {
        return be_shared_users;
    }

    public void setBe_shared_users(List<SharedUserReq> be_shared_users) {
        this.be_shared_users = be_shared_users;
    }
}
