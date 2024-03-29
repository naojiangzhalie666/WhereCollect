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
    //迁移隔层需要的
    //原始家庭code
    private String family_code;
    //目标家庭code
    private String target_family_code;
    private String background_url;
    private String image_url;

    private String objectId;
    private String object_id;

    public EditGoodsReq(String uid, String location_code) {
        this.uid = uid;
        this.location_code = location_code;
    }


    public EditGoodsReq() {
    }

    public String getFamily_code() {
        return family_code;
    }

    public void setFamily_code(String family_code) {
        this.family_code = family_code;
    }

    public String getTarget_family_code() {
        return target_family_code;
    }

    public void setTarget_family_code(String target_family_code) {
        this.target_family_code = target_family_code;
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

    public String getBackground_url() {
        return background_url;
    }

    public void setBackground_url(String background_url) {
        this.background_url = background_url;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObject_id() {
        return object_id;
    }

    public void setObject_id(String object_id) {
        this.object_id = object_id;
    }
}
