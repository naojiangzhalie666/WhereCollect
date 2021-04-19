package com.gongwu.wherecollect.net.entity.response;


public class DetailedGoodsBean {
    private String name;
    private String img;
    private boolean isShowGCType;
    private boolean isBoxType;
    private String boxName;
    private String boxImg;

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

    public boolean isShowGCType() {
        return isShowGCType;
    }

    public void setShowGCType(boolean showGCType) {
        isShowGCType = showGCType;
    }

    public boolean isBoxType() {
        return isBoxType;
    }

    public void setBoxType(boolean boxType) {
        isBoxType = boxType;
    }

    public String getBoxName() {
        return boxName;
    }

    public void setBoxName(String boxName) {
        this.boxName = boxName;
    }

    public String getBoxImg() {
        return boxImg;
    }

    public void setBoxImg(String boxImg) {
        this.boxImg = boxImg;
    }
}
