package com.gongwu.wherecollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.util.ImageLoader;

public class DetailedListView extends LinearLayout {

    private Context mContext;
    private ImageView furnitureImgIv;
    private TextView furnitureNameTv;
    private TextView furnitureCountTv;
    private LinearLayout detailedViewLayout;
    private final double row_count = 6.0;
    private int maxLine = 6;
    private int maxView = 6;
    private int viewCount = 0;
    private int initLine = 0;

    public DetailedListView(Context context) {
        this(context, null);
    }

    public DetailedListView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DetailedListView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView(context);
    }

    private void initView(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_detailed_list, this);
        furnitureImgIv = view.findViewById(R.id.furniture_img_iv);
        furnitureNameTv = view.findViewById(R.id.furniture_name_tv);
        furnitureCountTv = view.findViewById(R.id.furniture_count_tv);
        detailedViewLayout = view.findViewById(R.id.detailed_view_layout);
    }

    public DetailedListBean initData(DetailedListBean bean, RoomFurnitureResponse structure) {
        initLine = 0;
        ImageLoader.load(mContext, furnitureImgIv, bean.getFurniture_img());
        furnitureNameTv.setText(bean.getFurniture_name());
        furnitureCountTv.setText(bean.getObj_count() + "");
        for (int i = 0; i < bean.getObjects().size(); i++) {
            DetailedListGoodsBean childBean = bean.getObjects().get(i);
            int line = (int) Math.ceil((childBean.getObjs().size()) / row_count);
            if (initLine >= 5) {
                break;
            }
            initLine += line;
            DetailedView detailedView = new DetailedView(mContext);
            detailedView.initData(bean.getObjects().get(i), structure);
            detailedViewLayout.addView(detailedView);
            bean.getObjects().remove(i);
            i--;
        }
        if (bean.getObjects() != null && bean.getObjects().size() > 0) {
            return bean;
        } else {
            return null;
        }
    }
}
