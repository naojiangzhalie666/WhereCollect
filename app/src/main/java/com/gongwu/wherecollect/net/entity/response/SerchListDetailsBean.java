package com.gongwu.wherecollect.net.entity.response;


import java.util.List;

/**
 * Function:
 * Date: 2017/9/12
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class SerchListDetailsBean {
    private List<ObjectBean> items;
    private int type;

    public List<ObjectBean> getItems() {
        return items;
    }

    public void setItems(List<ObjectBean> items) {
        this.items = items;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
