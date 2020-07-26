package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

import java.util.List;

public class AddFamilyReq extends RequestBase {
    private String uid;
    private String family_info;
    private List<AddFamilyAndRoomsReq> room_list;


    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFamily_info() {
        return family_info;
    }

    public void setFamily_info(String family_info) {
        this.family_info = family_info;
    }

    public List<AddFamilyAndRoomsReq> getRoom_list() {
        return room_list;
    }

    public void setRoom_list(List<AddFamilyAndRoomsReq> room_list) {
        this.room_list = room_list;
    }
}
