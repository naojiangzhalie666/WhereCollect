package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class DetailedListGoodsBean {
    private Point scale;
    private Point position;
    private float ratio;
    private int obj_count;
    private String layer_code;
    private String layer_name;
    private List<DetailedGoodsBean> objs;
    private List<DetailedListBoxesBean> boxes;

    public Point getScale() {
        return scale;
    }

    public void setScale(Point scale) {
        this.scale = scale;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getObj_count() {
        return obj_count;
    }

    public void setObj_count(int obj_count) {
        this.obj_count = obj_count;
    }

    public String getLayer_code() {
        return layer_code;
    }

    public void setLayer_code(String layer_code) {
        this.layer_code = layer_code;
    }

    public String getLayer_name() {
        return layer_name;
    }

    public void setLayer_name(String layer_name) {
        this.layer_name = layer_name;
    }

    public List<DetailedGoodsBean> getObjs() {
        return objs;
    }

    public void setObjs(List<DetailedGoodsBean> objs) {
        this.objs = objs;
    }

    public List<DetailedListBoxesBean> getBoxes() {
        return boxes;
    }

    public void setBoxes(List<DetailedListBoxesBean> boxes) {
        this.boxes = boxes;
    }
}
