package com.gongwu.wherecollect.view;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.EditMoveFurnitureAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.ApiUtils;
import com.gongwu.wherecollect.util.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

public class PopupEditMoveFurniture extends BasePopupWindow implements MyOnItemClickListener {

    @BindView(R.id.edit_furniture_list_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.pop_title_tv)
    TextView titleTv;
    @BindView(R.id.pop_commit_tv)
    TextView commitTv;
    @BindView(R.id.pop_back_icon)
    ImageView backIv;

    private Loading loading;
    private EditMoveFurnitureAdapter mAdapter;
    private List<FamilyBean> mlist;
    private List<FamilyBean> mData;
    private FamilyBean oneBean;
    private FamilyBean twoBean;
    //只选家庭
    private boolean isMoveRoom;

    public PopupEditMoveFurniture(Context context) {
        super(context);
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(R.layout.popup_edit_furniture_move);
    }

    @Override
    public void onViewCreated(@NonNull View contentView) {
        super.onViewCreated(contentView);
        ButterKnife.bind(this, contentView);
        initView();
    }

    private void initView() {
        mlist = new ArrayList<>();
        mData = new ArrayList<>();
        mAdapter = new EditMoveFurnitureAdapter(getContext(), mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    @OnClick({R.id.pop_back_icon, R.id.cancel_iv, R.id.pop_commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_iv:
                dismiss();
                break;
            case R.id.pop_back_icon:
                mlist.clear();
                mlist.addAll(mData);
                mAdapter.setSelectMode(false);
                mAdapter.notifyDataSetChanged();
                backIv.setVisibility(View.GONE);
                commitTv.setVisibility(View.GONE);
                titleTv.setText(R.string.pop_select_family);
                break;
            case R.id.pop_commit_tv:
                if (listener != null) {
                    listener.onCommitClick(oneBean.getCode(), twoBean.getCode());
                }
                dismiss();
                break;

        }
    }

    private PopupClickListener listener;

    @Override
    public void onItemClick(int positions, View view) {
        if (isMoveRoom) {
            //只选家庭
            if (listener != null) {
                listener.onMoveRoomClick(mlist.get(positions));
            }
            dismiss();
        } else {
            //选择房间
            if (mAdapter.getSelectMode()) {
                for (FamilyBean bean : mlist) {
                    bean.setSelect(false);
                }
                mlist.get(positions).setSelect(true);
                mAdapter.notifyDataSetChanged();
                this.twoBean = mlist.get(positions);
            } else {
                titleTv.setText(R.string.select_room);
                oneBean = mlist.get(positions);
                initNextData(mlist.get(positions));
            }
        }
    }

    public interface PopupClickListener {
        void onCommitClick(String family_code, String room_code);

        void onMoveRoomClick(FamilyBean bean);
    }

    public void setPopupClickListener(PopupClickListener listener) {
        this.listener = listener;
    }

    public void initData() {
        loading = Loading.show(null, getContext(), "");
        ApiUtils.getUserFamily(App.getUser(getContext()).getId(), App.getUser(getContext()).getNickname(), new ApiCallBack<List<FamilyBean>>() {
            @Override
            public void onSuccess(List<FamilyBean> data) {
                if (loading != null) {
                    loading.dismiss();
                }
                if (data != null && mlist != null) {
                    mData.clear();
                    mData.addAll(data);
                    mlist.clear();
                    mlist.addAll(data);
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailed(String msg) {
                if (loading != null) {
                    loading.dismiss();
                }
            }
        });
    }

    public void initNextData(FamilyBean bean) {
        loading = Loading.show(null, getContext(), "");
        ApiUtils.getFamilyRoomLists(App.getUser(getContext()).getId(), bean.getCode(), new ApiCallBack<List<FamilyBean>>() {
            @Override
            public void onSuccess(List<FamilyBean> data) {
                if (loading != null) {
                    loading.dismiss();
                }
                if (data != null && mlist != null) {
                    mlist.clear();
                    mlist.addAll(data);
                    mAdapter.setSelectMode(true);
                    mAdapter.notifyDataSetChanged();
                    backIv.setVisibility(View.VISIBLE);
                    commitTv.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailed(String msg) {
                if (loading != null) {
                    loading.dismiss();
                }
            }
        });
    }

    public void setMoveRoom(boolean isMoveRoom) {
        this.isMoveRoom = isMoveRoom;
    }
}
