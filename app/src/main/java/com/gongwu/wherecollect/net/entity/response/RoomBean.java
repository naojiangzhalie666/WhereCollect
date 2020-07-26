package com.gongwu.wherecollect.net.entity.response;

import java.io.Serializable;

public class RoomBean implements Serializable {

    //          "furnitureNum": 2,
//          "isBeShared": false,
//          "_id": "5cfbb49b9b9a0a4e7c9520d0",
//          "name": "厨房",
//          "code": "1C39F51EDDDCDF60653FEED4913E3D5F",
//          "weight": 0
    private int furnitureNum;
    private boolean isBeShared;
    private String _id;
    private String name;
    private String code;
    private int weight;
    private boolean isSelect;

    public int getFurnitureNum() {
        return furnitureNum;
    }

    public void setFurnitureNum(int furnitureNum) {
        this.furnitureNum = furnitureNum;
    }

    public boolean isBeShared() {
        return isBeShared;
    }

    public void setBeShared(boolean beShared) {
        isBeShared = beShared;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
