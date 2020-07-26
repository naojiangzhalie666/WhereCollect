package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class LayerReq extends RequestBase {

    private String uid;
    private String name;//隔层的新名称
    private String layerCode;//layerCode
    private String furnitureCode;//隔层所在家具的code

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLayerCode() {
        return layerCode;
    }

    public void setLayerCode(String layerCode) {
        this.layerCode = layerCode;
    }

    public String getFurnitureCode() {
        return furnitureCode;
    }

    public void setFurnitureCode(String furnitureCode) {
        this.furnitureCode = furnitureCode;
    }
}
