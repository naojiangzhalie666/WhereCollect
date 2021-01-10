package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class DetailedListBean {
    private String room_name;
    private String furniture_name;
    private String furniture_img;
    private int obj_count;
    private List<DetailedListGoodsBean> objects;

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public String getFurniture_name() {
        return furniture_name;
    }

    public void setFurniture_name(String furniture_name) {
        this.furniture_name = furniture_name;
    }

    public String getFurniture_img() {
        return furniture_img;
    }

    public void setFurniture_img(String furniture_img) {
        this.furniture_img = furniture_img;
    }

    public int getObj_count() {
        return obj_count;
    }

    public void setObj_count(int obj_count) {
        this.obj_count = obj_count;
    }

    public List<DetailedListGoodsBean> getObjects() {
        return objects;
    }

    public void setObjects(List<DetailedListGoodsBean> objects) {
        this.objects = objects;
    }
}
