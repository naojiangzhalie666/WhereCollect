package com.gongwu.wherecollect.net.entity.response;

import java.io.Serializable;
import java.util.List;

public class RoomFurnitureResponse implements Serializable {

    private String furniture_name;
    private String room_code;
    private String room_name;
    private List<BaseBean> parents;
    private List<RoomFurnitureBean> layers;

    public String getFurniture_name() {
        return furniture_name;
    }

    public void setFurniture_name(String furniture_name) {
        this.furniture_name = furniture_name;
    }

    public String getRoom_code() {
        return room_code;
    }

    public void setRoom_code(String room_code) {
        this.room_code = room_code;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public List<BaseBean> getParents() {
        return parents;
    }

    public void setParents(List<BaseBean> parents) {
        this.parents = parents;
    }

    public List<RoomFurnitureBean> getLayers() {
        return layers;
    }

    public void setLayers(List<RoomFurnitureBean> layers) {
        this.layers = layers;
    }
}
