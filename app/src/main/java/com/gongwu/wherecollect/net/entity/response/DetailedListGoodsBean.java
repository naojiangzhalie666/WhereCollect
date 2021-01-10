package com.gongwu.wherecollect.net.entity.response;

import java.io.Serializable;
import java.util.List;

public class DetailedListGoodsBean {
    private Point scale;
    private Point position;
    private float ratio;
    private int obj_count;
    private String layer_code;
    private String layer_name;
    private List<Goods> objs;

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

    public List<Goods> getObjs() {
        return objs;
    }

    public void setObjs(List<Goods> objs) {
        this.objs = objs;
    }

    public static class Goods implements Serializable {
        private String name;
        private String img;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImg() {
            return img;
        }

        public void setImg(String img) {
            this.img = img;
        }
    }

    public static class Box implements Serializable {
        private String box_code;
        private String box_name;
        private List<Goods> objs;

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

        public List<Goods> getObjs() {
            return objs;
        }

        public void setObjs(List<Goods> objs) {
            this.objs = objs;
        }
    }
}
