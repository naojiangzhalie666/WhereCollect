package com.gongwu.wherecollect.net.entity.response;

import android.app.Activity;
import android.content.Context;

import com.gongwu.wherecollect.base.BaseActivity;

import java.io.Serializable;
import java.util.List;

public class RoomFurnitureBean  implements Serializable {
    private String _id;
    private String code;
    private String name;
    private List<BaseBean> parents;
    private Point position;
    private Point scale;
    private int ratio;
    private boolean isSelect;
    //迁移隔层需要的
    //原始家庭code
    private String family_code;
    private int level;

    public String getFamily_code() {
        return family_code;
    }

    public void setFamily_code(String family_code) {
        this.family_code = family_code;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<BaseBean> getParents() {
        return parents;
    }

    public void setParents(List<BaseBean> parents) {
        this.parents = parents;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point position) {
        this.position = position;
    }

    public Point getScale() {
        return scale;
    }

    public void setScale(Point scale) {
        this.scale = scale;
    }

    public int getRatio() {
        return ratio;
    }

    public void setRatio(int ratio) {
        this.ratio = ratio;
    }

    /**
     * x坐标
     *
     * @param context
     * @return
     */
    public float getX(Context context) {
        if (position == null) {
            return 0;
        }
        return position.getX() * getInitSize(context);
    }

    /**
     * Y坐标
     *
     * @param context
     * @return
     */
    public float getY(Context context) {
        if (position == null) {
            return 0;
        }
        return position.getY() * getInitSize(context);
    }

    /**
     * 获取长度
     *
     * @param context
     * @return
     */
    public float getWidth(Context context) {
        if (scale == null) {
            return getInitSize(context);
        }
        return scale.getX() * getInitSize(context);
    }

    /**
     * 获取高度
     *
     * @param context
     * @return
     */
    public float getHeight(Context context) {
        if (scale == null) {
            return getInitSize(context);
        }
        return scale.getY() * getInitSize(context);
    }

    /**
     * 设置高度
     *
     * @param context
     * @param height
     */
    public void setHeight(Context context, int height) {
        if (scale == null) {
            scale = new Point();
        }
        scale.setY(height / getInitSize(context));
    }

    /**
     * 设置宽度
     *
     * @param context
     * @param width
     */
    public void setWidth(Context context, int width) {
        if (scale == null) {
            scale = new Point();
        }
        scale.setX(width / getInitSize(context));
    }

    /**
     * @param x
     * @param y
     */
    public void setPosition(Context context, float x, float y) {
        x = x < 0 ? 0 : x;
        y = y < 0 ? 0 : y;
        x = x > BaseActivity.getScreenWidth(((Activity) context)) - getWidth(context) ? BaseActivity.getScreenWidth(((Activity) context)) - getWidth(context) : x;
        if (position == null) {
            position = new Point();
        }
        x = x / getInitSize(context);
        y = y / getInitSize(context);
        position.setX(x);
        position.setY(y);
    }

    /**
     * 获取初始尺寸
     *
     * @param context
     * @return
     */
    public static float getInitSize(Context context) {
        return BaseActivity.getScreenWidth(((Activity) context)) / 4;
    }

    public Point getLeftTopBasePoint() {
        return position;
    }

    public Point getRightTopBasePoint() {
        Point point = new Point();
        point.setX(position.getX() + (scale.getX() - 1));
        point.setY(position.getY());
        return point;
    }

    public Point getLeftBottomBasePoint() {
        Point point = new Point();
        point.setY(position.getY() + (scale.getY() - 1));
        point.setX(position.getX());
        return point;
    }

    public Point getRightBottomBasePoint() {
        Point point = new Point();
        point.setY(position.getY() + (scale.getY() - 1));
        point.setX(position.getX() + (scale.getX() - 1));
        return point;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
