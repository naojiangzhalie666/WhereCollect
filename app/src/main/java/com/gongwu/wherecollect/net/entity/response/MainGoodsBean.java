package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class MainGoodsBean {
    private String code;
    private int haveLocation;
    private String name;
    private int noLocation;
    private int total;
    private List<ObjectBean> objects;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getHaveLocation() {
        return haveLocation;
    }

    public void setHaveLocation(int haveLocation) {
        this.haveLocation = haveLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNoLocation() {
        return noLocation;
    }

    public void setNoLocation(int noLocation) {
        this.noLocation = noLocation;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ObjectBean> getObjects() {
        return objects;
    }

    public void setObjects(List<ObjectBean> objects) {
        this.objects = objects;
    }
}
