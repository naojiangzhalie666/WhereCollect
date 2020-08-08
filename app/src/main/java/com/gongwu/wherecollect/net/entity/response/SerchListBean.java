package com.gongwu.wherecollect.net.entity.response;

/**
 * Function:
 * Date: 2017/9/12
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class SerchListBean {
    private SerchListDetailsBean objects;
    private SerchListDetailsBean locations;
    private SerchListDetailsBean categories;

    public SerchListDetailsBean getObjects() {
        return objects;
    }

    public void setObjects(SerchListDetailsBean objects) {
        this.objects = objects;
    }

    public SerchListDetailsBean getLocations() {
        return locations;
    }

    public void setLocations(SerchListDetailsBean locations) {
        this.locations = locations;
    }

    public SerchListDetailsBean getCategories() {
        return categories;
    }

    public void setCategories(SerchListDetailsBean categories) {
        this.categories = categories;
    }
}
