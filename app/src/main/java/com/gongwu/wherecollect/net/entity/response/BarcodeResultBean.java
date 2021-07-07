package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class BarcodeResultBean {
//"name": "计算机简史",
//"object_url": "http://api.jisuapi.com/isbn/upload/05/e02f81500aabc2.jpg",
//"price": 89,
//"category_code": "B52642DEA13A35513B29B049C5A4F4F7",
//"category_name": "图书"
    private String name;
    private String object_url;
    private double price;
    private String category_code;
    private String category_name;
    private List<BaseBean> channel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getObject_url() {
        return object_url;
    }

    public void setObject_url(String object_url) {
        this.object_url = object_url;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory_code() {
        return category_code;
    }

    public void setCategory_code(String category_code) {
        this.category_code = category_code;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public List<BaseBean> getChannel() {
        return channel;
    }

    public void setChannel(List<BaseBean> channel) {
        this.channel = channel;
    }
}
