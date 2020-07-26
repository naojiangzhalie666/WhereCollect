package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class LayerTemplateBean {
    private List<FurnitureBean> allTemp;
    private List<FurnitureBean> suggestTemp;

    public List<FurnitureBean> getAllTemp() {
        return allTemp;
    }

    public void setAllTemp(List<FurnitureBean> allTemp) {
        this.allTemp = allTemp;
    }

    public List<FurnitureBean> getSuggestTemp() {
        return suggestTemp;
    }

    public void setSuggestTemp(List<FurnitureBean> suggestTemp) {
        this.suggestTemp = suggestTemp;
    }
}
