package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class FamilyListBean {
    private String title;
    private List<FamilyBean> familys;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<FamilyBean> getFamilys() {
        return familys;
    }

    public void setFamilys(List<FamilyBean> familys) {
        this.familys = familys;
    }
}
