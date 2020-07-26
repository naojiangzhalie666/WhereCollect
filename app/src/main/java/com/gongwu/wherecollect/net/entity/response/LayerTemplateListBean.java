package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class LayerTemplateListBean {
    private String name;
    private List<FurnitureBean> tempList;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<FurnitureBean> getTempList() {
        return tempList;
    }

    public void setTempList(List<FurnitureBean> tempList) {
        this.tempList = tempList;
    }
}
