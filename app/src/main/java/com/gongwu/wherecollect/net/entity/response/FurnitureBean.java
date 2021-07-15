package com.gongwu.wherecollect.net.entity.response;

import java.io.Serializable;
import java.util.List;

public class FurnitureBean implements Serializable {
    private String _id;
    private String name;
    private String code;
    private float level;
    private String user_id;
    private String image_url;
    private String location_code;
    private String background_url;
    private String type;
    private String type_name;
    private String system_furniture_code;
    private boolean is_user;
    private int object_count;
    private int location_count;
    private float ratio;
    private Point scale;
    private List<RoomFurnitureBean> layers;
    private int weight;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public float getLevel() {
        return level;
    }

    public void setLevel(float level) {
        this.level = level;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getLocation_code() {
        return location_code;
    }

    public void setLocation_code(String location_code) {
        this.location_code = location_code;
    }

    public String getBackground_url() {
        return background_url;
    }

    public void setBackground_url(String background_url) {
        this.background_url = background_url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
    }

    public boolean isIs_user() {
        return is_user;
    }

    public void setIs_user(boolean is_user) {
        this.is_user = is_user;
    }

    public int getObject_count() {
        return object_count;
    }

    public void setObject_count(int object_count) {
        this.object_count = object_count;
    }

    public int getLocation_count() {
        return location_count;
    }

    public void setLocation_count(int location_count) {
        this.location_count = location_count;
    }

    public List<RoomFurnitureBean> getLayers() {
        return layers;
    }

    public void setLayers(List<RoomFurnitureBean> layers) {
        this.layers = layers;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Point getScale() {
        return scale;
    }

    public void setScale(Point scale) {
        this.scale = scale;
    }

    public String getSystem_furniture_code() {
        return system_furniture_code;
    }

    public void setSystem_furniture_code(String system_furniture_code) {
        this.system_furniture_code = system_furniture_code;
    }
}
