package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class MyFamilyListBean {
    private List<FamilyBean> my;
    private List<FamilyBean> beShared;

    public List<FamilyBean> getMy() {
        return my;
    }

    public void setMy(List<FamilyBean> my) {
        this.my = my;
    }

    public List<FamilyBean> getBeShared() {
        return beShared;
    }

    public void setBeShared(List<FamilyBean> beShared) {
        this.beShared = beShared;
    }
}
