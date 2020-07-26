package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class RoomFurnitureGoodsBean {
    private List<ObjectBean> objects;
    private List<ObjectBean> locations;

    public List<ObjectBean> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectBean> objects) {
        this.objects = objects;
    }

    public List<ObjectBean> getLocations() {
        return locations;
    }

    public void setLocations(List<ObjectBean> locations) {
        this.locations = locations;
    }
}
