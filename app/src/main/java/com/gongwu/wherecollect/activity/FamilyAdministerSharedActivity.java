package com.gongwu.wherecollect.activity;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.FamilyAdministerSharedAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAdministerFamilySharedContract;
import com.gongwu.wherecollect.contract.presenter.AdministerFamilySharedPresenter;
import com.gongwu.wherecollect.net.entity.request.SharedUserReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.PopupAddShareSpace;
import com.gongwu.wherecollect.view.Loading;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 家庭管理协作人列表
 */
public class FamilyAdministerSharedActivity extends BaseMvpActivity<FamilyAdministerSharedActivity, AdministerFamilySharedPresenter> implements IAdministerFamilySharedContract.IAdministerFamilySharedView, PopupAddShareSpace.PopupCommitClickListener {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_maincolor_tv)
    TextView commitTv;
    @BindView(R.id.share_person_refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.share_person_list)
    RecyclerView mRecyclerView;

    private Loading loading;
    private PopupAddShareSpace popup;

    private FamilyBean familyBean;
    private SharedPersonBean selectBean;
    private List<SharedPersonBean> mlist;
    private FamilyAdministerSharedAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_family_administer_shared;
    }

    @Override
    protected void initViews() {
        titleTv.setText(R.string.administer_family);
        commitTv.setVisibility(View.VISIBLE);
        commitTv.setText("添加协作人");
        familyBean = (FamilyBean) getIntent().getSerializableExtra("familyBean");
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mlist = new ArrayList<>();
        mAdapter = new FamilyAdministerSharedAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                if (familyBean != null && !TextUtils.isEmpty(familyBean.getCode())) {
                    getPresenter().getShareListUserByFamily(App.getUser(mContext).getId(), familyBean.getCode());
                }
            }
        });
        mRefreshLayout.autoRefresh();
        mAdapter.setOnItemClickListener(new FamilyAdministerSharedAdapter.OnItemClickListener() {
            @Override
            public void closeClick(int position, View v) {
                getPresenter().delCollaborator(App.getUser(mContext).getId(), mlist.get(position).get_id());
            }

            @Override
            public void onItemsClick(int position, View v) {
                selectBean = mlist.get(position);
                getPresenter().getShareRoomList(App.getUser(mContext).getId(), familyBean.getCode(), mlist.get(position).get_id());
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.title_commit_maincolor_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.title_commit_maincolor_tv:
                AddSharedPersonActivity.start(mContext, AppConstant.REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    @Override
    protected AdministerFamilySharedPresenter createPresenter() {
        return AdministerFamilySharedPresenter.getInstance();
    }

    @Override
    public void getShareListUserByFamilySuccess(List<SharedPersonBean> bean) {
        mlist.clear();
        if (bean != null && bean.size() > 0) {
            mlist.addAll(bean);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void getShareRoomListSuccess(List<BaseBean> data) {
        if (data == null || data.size() == 0) return;
        if (popup == null) {
            popup = new PopupAddShareSpace(mContext);
            popup.setPopupClickListener(this);
            popup.setPopupGravity(Gravity.CENTER);
        }
        List<BaseBean> mlist = new ArrayList<>();
        for (BaseBean baseBean : data) {
            if (baseBean.isBeShared()) {
                baseBean.setSelect(true);
            }
            mlist.add(baseBean);
        }
        popup.setTitleView(R.string.select_room, mlist);
        popup.showPopupWindow();
    }

    @Override
    public void delCollaboratorSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            finish();
        }
    }

    @Override
    public void shareOrCancelShareRoomsSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            mRefreshLayout.autoRefresh();
        }
    }

    public static void start(Context mContext, FamilyBean bean) {
        Intent intent = new Intent(mContext, FamilyAdministerSharedActivity.class);
        intent.putExtra("familyBean", bean);
        mContext.startActivity(intent);
    }

    private void refreshLayoutFinished() {
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(loading, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
        refreshLayoutFinished();
    }

    @Override
    public void onError(String result) {
        refreshLayoutFinished();
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCommitClickListener(List<BaseBean> beans) {
        if (selectBean == null) return;
        List<String> roomCodes = new ArrayList<>();
        List<String> unroomCodes = new ArrayList<>();
        for (int i = 0; i < beans.size(); i++) {
            if (!beans.get(i).isBeShared() && beans.get(i).isSelect()) {
                roomCodes.add(beans.get(i).getCode());
            }
            if (beans.get(i).isBeShared() && !beans.get(i).isSelect()) {
                unroomCodes.add(beans.get(i).getCode());
            }
        }
        SharedUserReq userReq = new SharedUserReq();
        userReq.setUser_id(selectBean.get_id());
        userReq.setValid(selectBean.isSharing());
        getPresenter().shareOrCancelShareRooms(App.getUser(mContext).getId(), userReq, familyBean.getId(), familyBean.getCode(), roomCodes, unroomCodes);
    }
}
