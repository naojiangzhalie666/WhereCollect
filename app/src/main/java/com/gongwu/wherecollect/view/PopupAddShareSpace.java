package com.gongwu.wherecollect.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.AddShareSpaceAdapter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupAddShareSpace extends BasePopupWindow {
    @BindView(R.id.popup_title_tv)
    TextView mTitleView;
    @BindView(R.id.popup_list_view)
    RecyclerView mRecyclerView;

    private List<BaseBean> mlist;
    private AddShareSpaceAdapter mAdapter;

    public PopupAddShareSpace(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_add_share_space);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
        initView();
    }

    private void initView() {
        mlist = new ArrayList<>();
        mAdapter = new AddShareSpaceAdapter(getContext(), mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
    }

    public void setTitleView(int stringId, List<BaseBean> mData) {
        mTitleView.setText(stringId);
        mlist.clear();
        mlist.addAll(mData);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.popup_back_iv, R.id.popup_commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.popup_back_iv:
                dismiss();
                break;
            case R.id.popup_commit_tv:
                onCommitClick();
                break;
        }
    }

    private void onCommitClick() {
        if (listener != null) {
            listener.onCommitClickListener(mlist);
        }
        dismiss();
    }

    private PopupCommitClickListener listener;

    public interface PopupCommitClickListener {
        void onCommitClickListener(List<BaseBean> mlist);
    }

    public void setPopupClickListener(PopupCommitClickListener listener) {
        this.listener = listener;
    }
}
