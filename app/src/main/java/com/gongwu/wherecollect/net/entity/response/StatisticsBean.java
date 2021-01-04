package com.gongwu.wherecollect.net.entity.response;

public class StatisticsBean {
//"name": "0", // 价格区间名称
//"count": 2, // 数量
//"ratio": 0.4 // 占比
    private String name;
    private int count;
    private double ratio;

    public StatisticsBean() {
    }

    public StatisticsBean(String name, int count) {
        this.name = name;
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public double getRatio() {
        return ratio;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }
}
