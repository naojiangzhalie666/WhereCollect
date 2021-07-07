package com.gongwu.wherecollect.net.entity.response;

public class EnergyPriceBean {
    private int price;//价格
    private int count;//对应能量值
    private int give_count;//额外赠送

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getGive_count() {
        return give_count;
    }

    public void setGive_count(int give_count) {
        this.give_count = give_count;
    }
}
