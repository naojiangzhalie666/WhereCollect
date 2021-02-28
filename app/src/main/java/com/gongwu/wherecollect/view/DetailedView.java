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
import com.gongwu.wherecollect.net.entity.response.DetailedGoodsBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListBoxesBean;
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
    private List<DetailedGoodsBean> list = new ArrayList<>();

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
        mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 5));
        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.addItemDecoration(new DetailedViewAdapter.SpaceItemDecoration(0, 10));
    }

    //isShowTableView,false表示没有添加过进行显示View.true添加过就不显示
    public void initData(DetailedListGoodsBean bean, RoomFurnitureResponse structure, boolean isShowTableView) {
        if (bean == null) return;
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
        tableRowLayout.setVisibility(isShowTableView ? INVISIBLE : VISIBLE);
        gecengNameTv.setText(bean.getLayer_name());
        gecengNameTv.setVisibility(isShowTableView ? INVISIBLE : VISIBLE);
        list.clear();
        if (bean.getObjs() != null && bean.getObjs().size() > 0) {
            list.addAll(bean.getObjs());
        }
        //添加收纳盒内的物品
        if (bean.getBoxes() != null && bean.getBoxes().size() > 0) {
            for (int i = 0; i < bean.getBoxes().size(); i++) {
                DetailedListBoxesBean boxesBean = bean.getBoxes().get(i);
                if (boxesBean.getObjs() != null && boxesBean.getObjs().size() > 0) {
                    list.addAll(boxesBean.getObjs());
                }
            }
        }
        //若一个隔层内的数据太多,导致在下一页显示的话就不显示隔层type
        mAdapter.showGCTableView(isShowTableView);
        mAdapter.notifyDataSetChanged();
    }
}
