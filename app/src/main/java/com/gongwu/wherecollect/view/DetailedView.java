package com.gongwu.wherecollect.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.DetailedViewAdapter;
import com.gongwu.wherecollect.net.entity.response.DetailedListGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.view.furniture.ChildView;
import com.gongwu.wherecollect.view.furniture.CustomTableRowLayout;

import java.util.ArrayList;
import java.util.List;

public class DetailedView extends LinearLayout {
    private CustomTableRowLayout tableRowLayout;
    private TextView gecengNameTv;
    private RecyclerView mRecyclerView;
    private DetailedViewAdapter mAdapter;
    private List<DetailedListGoodsBean.Goods> list = new ArrayList<>();

    public DetailedView(Context context) {
        this(context, null);
    }

    public DetailedView(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public DetailedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context mContext) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.view_detailed, this);
        tableRowLayout = view.findViewById(R.id.tab_item_view);
        gecengNameTv = view.findViewById(R.id.geceng_name_tv);
        mRecyclerView = view.findViewById(R.id.detailed_list_view);
        mAdapter = new DetailedViewAdapter(mContext, list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 6));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void initData(DetailedListGoodsBean bean, RoomFurnitureResponse structure) {
        if (bean.getObjs() != null && bean.getObjs().size() > 0) {
            tableRowLayout.setEnabled(false);
            tableRowLayout.setNotChildViewClick(true);
            tableRowLayout.setPaddingView(1);
            tableRowLayout.init(structure.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng3);
            tableRowLayout.setOnInitListener(new CustomTableRowLayout.OnInitListener() {
                @Override
                public void OnInit() {
                    ChildView view = (ChildView) tableRowLayout.findViewByPoint(bean.getPosition());
                    if (view != null) {
                        view.setBackgroundResource();
                    }
                }
            });
            gecengNameTv.setText(bean.getLayer_name());
            list.clear();
            list.addAll(bean.getObjs());
            mAdapter.notifyDataSetChanged();
        }
    }
}
