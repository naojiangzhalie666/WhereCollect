package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class FurnitureTemplateBean {
    private String _id;
    private String name;
    private List<FurnitureBean> funiture;

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

    public List<FurnitureBean> getFuniture() {
        return funiture;
    }

    public void setFuniture(List<FurnitureBean> funiture) {
        this.funiture = funiture;
    }
}
