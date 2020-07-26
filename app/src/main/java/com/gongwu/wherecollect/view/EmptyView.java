package com.gongwu.wherecollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gongwu.wherecollect.R;

/**
 * @ClassName: EmptyView
 * @Description: 用于没有数据或加载错误时显示
 */
public class EmptyView extends LinearLayout {
    private TextView error_tv;
    private ImageView error_img;

    public EmptyView(Context context) {
        super(context);
        initView(context, null);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet object) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_empty, this);
        error_tv = (TextView) view.findViewById(R.id.error_msg_tv);
        error_img = (ImageView) view.findViewById(R.id.empty_img);
    }

    /**
     * @param msg
     * @Title: setErrorMsg
     * @Description: 设置错误提示语
     * @return: void
     */
    public void setErrorMsg(String msg) {
        error_tv.setText(msg);
    }

    public void setEmptyImg(Context context, int resId) {
        error_img.setVisibility(View.VISIBLE);
        error_img.setImageDrawable(context.getDrawable(resId));
    }
}
