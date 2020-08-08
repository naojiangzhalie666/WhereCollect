package com.gongwu.wherecollect.net.entity.request;

import com.gongwu.wherecollect.net.entity.base.RequestBase;

public class AddGoodsReq extends RequestBase {
    private String uid;
    private String ISBN;
    private String category_codes;
    private String channel;
    private String color;
    private String detail;
    private String price_max;
    private String price_min;
    private String season;
    private String star;
    private String name;
    private String image_urls;
    private String image_url;
    private String count;
    private String buy_date;
    private String expire_date;
    private String key;
    private String code;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getCategory_codes() {
        return category_codes;
    }

    public void setCategory_codes(String category_codes) {
        this.category_codes = category_codes;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getPrice_max() {
        return price_max;
    }

    public void setPrice_max(String price_max) {
        this.price_max = price_max;
    }

    public String getPrice_min() {
        return price_min;
    }

    public void setPrice_min(String price_min) {
        this.price_min = price_min;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_urls() {
        return image_urls;
    }

    public void setImage_urls(String image_urls) {
        this.image_urls = image_urls;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getBuy_date() {
        return buy_date;
    }

    public void setBuy_date(String buy_date) {
        this.buy_date = buy_date;
    }

    public String getExpire_date() {
        return expire_date;
    }

    public void setExpire_date(String expire_date) {
        this.expire_date = expire_date;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
