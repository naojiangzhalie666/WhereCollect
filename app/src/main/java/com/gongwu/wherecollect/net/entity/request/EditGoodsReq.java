package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

import java.util.List;

public class EditGoodsReq extends RequestBase {
    private String uid;
    private String location_code;
    private String object_codes;
    private String code;
    private String ids;
    private String furnitureCode;
    private List<String> objectIds;
    private Integer page;
    private String name;

    public EditGoodsReq(String uid, String location_code) {
        this.uid = uid;
        this.location_code = location_code;
    }


    public EditGoodsReq() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getObject_codes() {
        return object_codes;
    }

    public void setObject_codes(String object_codes) {
        this.object_codes = object_codes;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public String getFurnitureCode() {
        return furnitureCode;
    }

    public void setFurnitureCode(String furnitureCode) {
        this.furnitureCode = furnitureCode;
    }

    public List<String> getObjectIds() {
        return objectIds;
    }

    public void setObjectIds(List<String> objectIds) {
        this.objectIds = objectIds;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
