package com.gongwu.wherecollect.net.entity;

public class BarcodeBean {

    public BarcodeBean(String uid, String tkey) {
        this.tkey = tkey;
        this.uid = uid;
    }

    public BarcodeBean(String uid, String barcode, String type) {
        this.barcode = barcode;
        this.type = type;
        this.uid = uid;
    }

    private String barcode;
    private String type;
    private String uid;
    private String tkey;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTkey() {
        return tkey;
    }

    public void setTkey(String tkey) {
        this.tkey = tkey;
    }
}
