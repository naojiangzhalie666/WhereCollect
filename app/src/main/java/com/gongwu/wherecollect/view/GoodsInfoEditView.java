package com.gongwu.wherecollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.gongwu.wherecollect.R;

import butterknife.ButterKnife;

public class GoodsInfoEditView extends FrameLayout {
    private Context mContext;

    public GoodsInfoEditView(Context context) {
        this(context, null);
    }

    public GoodsInfoEditView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        View v = View.inflate(mContext, R.layout.layout_goods_info_edit_view, null);
        this.addView(v);
        ButterKnife.bind(this, v);
    }
}
