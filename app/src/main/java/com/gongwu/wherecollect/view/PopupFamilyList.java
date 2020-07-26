package com.gongwu.wherecollect.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.FamilyPopupListAdapter;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupFamilyList extends BasePopupWindow {
    @BindView(R.id.popup_family_list_view)
    RecyclerView mRecyclerView;

    private List<FamilyBean> mlist;
    private FamilyPopupListAdapter mAdapter;

    public PopupFamilyList(Context context) {
        super(context);
    }

    public void initData(List<FamilyBean> mlist) {
        if (mlist != null && mAdapter != null) {
            this.mlist.clear();
            this.mlist.addAll(mlist);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_family_list);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
        mlist = new ArrayList<>();
        mAdapter = new FamilyPopupListAdapter(getContext(), mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new FamilyPopupListAdapter.OnItemClickListener() {
            @Override
            public void onItemsClick(int position, View v) {
                if (listener != null) {
                    listener.onItemsClick(position, v);
                }
                dismiss();
            }
        });
    }


    private OnItemClickListener listener;

    public interface OnItemClickListener {

        void onItemsClick(int position, View v);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
