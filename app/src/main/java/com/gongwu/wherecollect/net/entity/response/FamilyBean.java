package com.gongwu.wherecollect.net.entity.response;

import java.io.Serializable;

public class FamilyBean implements Serializable {
    //        "isBeShared": false,
//        "__v": 0,
//        "name": "10010@qq.c的家",
//        "user_id": "5cfbb4979b9a0a4e7c9520cf",
//        "level": -1,
//        "code": "C43B8A7F8C7D8DA77140E49908A65C43",
//        "_id": "5edc9bd00f732431b826e647",
//        "updated_at": "2020-06-07T07:48:32.346Z",
//        "created_at": "2020-06-07T07:48:32.346Z",
//        "default_create": 0,
//        "tags": [],
//        "official": 0,
//        "recommend": 0,
//        "parents": [],
//        "black_list": [],
//        "white_list": [],
//        "is_user": false,
//        "location_count": 0,
//        "object_count": 0,
//        "weight": 0
    private boolean isBeShared;
    private String name;
    private String user_id;
    private int __v;
    private int level;
    private String code;
    private String _id;
    private String updated_at;
    private int default_create;
    private int official;
    private int recommend;
    private boolean is_user;
    private int location_count;
    private int object_count;
    private int weight;
    private boolean isSelect;
    private boolean isSystemEdit;
    private int roomNum;
    private int objectNum;

    public boolean isBeShared() {
        return isBeShared;
    }

    public void setBeShared(boolean beShared) {
        isBeShared = beShared;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return _id;
    }

    public void setId(String _id) {
        this._id = _id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public boolean isSystemEdit() {
        return isSystemEdit;
    }

    public void setSystemEdit(boolean systemEdit) {
        isSystemEdit = systemEdit;
    }

    public int getRoomNum() {
        return roomNum;
    }

    public void setRoomNum(int roomNum) {
        this.roomNum = roomNum;
    }

    public int getObjectNum() {
        return objectNum;
    }

    public void setObjectNum(int objectNum) {
        this.objectNum = objectNum;
    }
}
