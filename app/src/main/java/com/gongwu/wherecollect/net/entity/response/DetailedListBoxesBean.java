package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class DetailedListBoxesBean {
    private String box_code;
    private String box_name;
    private List<DetailedGoodsBean> objs;

    public String getBox_code() {
        return box_code;
    }

    public void setBox_code(String box_code) {
        this.box_code = box_code;
    }

    public String getBox_name() {
        return box_name;
    }

    public void setBox_name(String box_name) {
        this.box_name = box_name;
    }

    public List<DetailedGoodsBean> getObjs() {
        return objs;
    }

    public void setObjs(List<DetailedGoodsBean> objs) {
        this.objs = objs;
    }
}
