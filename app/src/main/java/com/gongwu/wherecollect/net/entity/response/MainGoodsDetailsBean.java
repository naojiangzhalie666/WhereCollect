package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class MainGoodsDetailsBean {
    private int __v;
    private String _id;
    private String img;
    private List<BaseBean> locations;
    private String object_url;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int get__v() {
        return __v;
    }

    public void set__v(int __v) {
        this.__v = __v;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<BaseBean> getLocations() {
        return locations;
    }

    public void setLocations(List<BaseBean> locations) {
        this.locations = locations;
    }

    public String getObject_url() {
        return object_url;
    }

    public void setObject_url(String object_url) {
        this.object_url = object_url;
    }
}
