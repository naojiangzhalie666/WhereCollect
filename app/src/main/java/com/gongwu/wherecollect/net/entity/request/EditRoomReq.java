package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

import java.util.List;

public class EditRoomReq extends RequestBase {
    private String uid;
    private String location_codes;
    private String location_code;
    private String location_name;
    private String name;
    private String location_id;
    private String old_family_code;
    private String new_family_code;
    private String new_family_name;
    private List<String> rooms_code;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation_codes() {
        return location_codes;
    }

    public void setLocation_codes(String location_codes) {
        this.location_codes = location_codes;
    }

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getOld_family_code() {
        return old_family_code;
    }

    public void setOld_family_code(String old_family_code) {
        this.old_family_code = old_family_code;
    }

    public String getNew_family_code() {
        return new_family_code;
    }

    public void setNew_family_code(String new_family_code) {
        this.new_family_code = new_family_code;
    }

    public String getNew_family_name() {
        return new_family_name;
    }

    public void setNew_family_name(String new_family_name) {
        this.new_family_name = new_family_name;
    }

    public List<String> getRooms_code() {
        return rooms_code;
    }

    public void setRooms_code(List<String> rooms_code) {
        this.rooms_code = rooms_code;
    }
}
