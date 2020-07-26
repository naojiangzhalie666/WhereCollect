package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

import java.util.List;

public class SharedRoomsReq extends RequestBase {
    private String uid;
    private SharedUserReq be_shared_user;
    private String family_code;
    private String family_id;
    private List<String> shared_rooms;
    private List<String> un_shared_rooms;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public SharedUserReq getBe_shared_user() {
        return be_shared_user;
    }

    public void setBe_shared_user(SharedUserReq be_shared_user) {
        this.be_shared_user = be_shared_user;
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

    public List<String> getShared_rooms() {
        return shared_rooms;
    }

    public void setShared_rooms(List<String> shared_rooms) {
        this.shared_rooms = shared_rooms;
    }

    public List<String> getUn_shared_rooms() {
        return un_shared_rooms;
    }

    public void setUn_shared_rooms(List<String> un_shared_rooms) {
        this.un_shared_rooms = un_shared_rooms;
    }
}
