package com.gongwu.wherecollect.view.furniture;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;

/**
 * Function:
 * Date: 2017/8/30
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class ChildView extends RelativeLayout {
    Context context;
    float x, y;
    TextView textView;
    private RoomFurnitureBean bean;
    private int resID;
    private int selectCount;

    public ChildView(Context context, int resId) {
        this(context, null, resId);
    }

    public ChildView(Context context, AttributeSet attrs, int resId) {
        super(context, attrs);
        View view = View.inflate(context, R.layout.item_geceng_child_view, null);
        textView = (TextView) view.findViewById(R.id.textView);
        this.resID = resId;
        addView(view);
        this.context = context;
    }

    /**
     * @param isEdit 是否是编辑状态
     */
    public void setEditable(boolean isEdit) {
        if (bean != null) {
            bean.setSelect(isEdit);
        }
        if (isEdit) {
            textView.setBackgroundResource(R.drawable.shape_dush_bg_white);
        } else {
            textView.setBackgroundResource(resID);
            //            setBackgroundResource(R.drawable.shape_dush_bg_white);
        }
    }

    public void setEditableByFalse() {
        if (bean != null) {
            bean.setSelect(false);
        }
        if (selectCount == 0) {
            textView.setBackgroundResource(resID);
        }
    }

    public void setSelectCount(boolean isSelect) {
        if (bean.isSelect()) return;
        if (isSelect) {
            selectCount++;
        } else {
            selectCount--;
        }
        if (selectCount > 0) {
            textView.setBackgroundResource(R.drawable.shape_geceng2);
        } else {
            textView.setBackgroundResource(resID);
        }
    }

    public void setUnSelectCount() {
        selectCount = 0;
        textView.setBackgroundResource(resID);
    }

    public boolean isEdit() {
        return bean == null ? false : bean.isSelect();
    }

    /**
     * 设置或刷新物品的位置或大小
     *
     * @param bean
     */
    public void setObject(RoomFurnitureBean bean, View parentView) {
        this.bean = bean;
        setX(bean.getPosition().getX() * (parentView.getWidth() / CustomTableRowLayout.MAXW));
        setY(bean.getPosition().getY() * (parentView.getHeight() / CustomTableRowLayout.MAXH));
        LayoutParams layoutParams = new LayoutParams(((int) (bean.getScale().getX() *
                parentView.getWidth() / CustomTableRowLayout.MAXW)), ((int) (bean.getScale().getY() * parentView
                .getHeight() / CustomTableRowLayout.MAXH)));
        setLayoutParams(layoutParams);
        setVisibility(VISIBLE);
        setEditable(bean.isSelect());
    }

    public void setObjectBean(RoomFurnitureBean bean) {
        this.bean = bean;

    }

    public RoomFurnitureBean getObjectBean() {
        return bean;
    }
}
