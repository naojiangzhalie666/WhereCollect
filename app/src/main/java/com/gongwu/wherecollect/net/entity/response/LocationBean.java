package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class LocationBean {
    private String _id;
    private int level;
    private String name;
    private String location_code;
    private int object_count;
    private List<BaseBean> parents;
    private int ratio;
    private String user_id;


    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public int getObject_count() {
        return object_count;
    }

    public void setObject_count(int object_count) {
        this.object_count = object_count;
    }

    public List<BaseBean> getParents() {
        return parents;
    }

    public void setParents(List<BaseBean> parents) {
        this.parents = parents;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
