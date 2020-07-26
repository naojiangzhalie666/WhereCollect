package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.adapter.ShareSpaceDetailsListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IShareContract;
import com.gongwu.wherecollect.contract.presenter.SharePresenter;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.view.Loading;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShareSpaceDetailsActivity extends BaseMvpActivity<SharePersonDetailsActivity, SharePresenter> implements IShareContract.IShareView {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.share_space_details_refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.share_space_details_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.share_person_empty_view)
    View mEmptyView;

    private Loading loading;

    private List<SharedPersonBean> mlist = new ArrayList<>();
    private List<SharedLocationBean> locationBeans = new ArrayList<>();
    private ShareSpaceDetailsListAdapter mAdapter;
    private SharedLocationBean locationBean;
    private String content;
    private SharedPersonBean managerUser;
    private int selectPositon;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_space_details;
    }

    @Override
    protected void initViews() {
        locationBean = (SharedLocationBean) getIntent().getSerializableExtra("locationBean");
        titleTv.setText(locationBean.getName() + ">共享详情");
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new ShareSpaceDetailsListAdapter(this, mlist, App.getUser(mContext).getId()) {
            @Override
            public void closeClick(int position) {
                startDialogHintCloseShareUser(position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        initEvent();
        mRefreshLayout.autoRefresh();

    }

    @Override
    protected SharePresenter createPresenter() {
        return SharePresenter.getInstance();
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstant.REQUEST_CODE && resultCode == RESULT_OK) {
            mRefreshLayout.autoRefresh();
        }
    }


    @Override
    public void getSharedLocationsSuccess(List<SharedLocationBean> beans) {
        refreshLayoutFinished();
        mlist.clear();
        locationBeans.clear();
        content = "";
        if (beans != null && beans.size() > 0) {
            for (int i = 0; i < beans.size(); i++) {
                content += beans.get(i).getName() + " ";
                if (beans.get(i).getCode().equals(locationBean.getCode())) {
                    mlist.addAll(beans.get(i).getShared_users());
                    managerUser = beans.get(i).getUser();
//                    if (user.getId().equals(managerUser.getId())) {
//                        addShareTv.setVisibility(View.VISIBLE);
//                    }
                    if (mAdapter != null) {
                        mAdapter.setManager(managerUser);
                    }
                }
            }
        }
        if (mlist.size() > 0) {
            locationBeans.addAll(beans);
            mAdapter.setContent(content);
            mEmptyView.setVisibility(View.GONE);
            mAdapter.notifyDataSetChanged();
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void closeShareUserSuccess(RequestSuccessBean data) {
        if (mlist.get(selectPositon).getUid().equals(App.getUser(mContext).getId())) {
            setResult(RESULT_OK);
            finish();
        } else {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void getSharedUsersListSuccess(List<SharedPersonBean> data) {

    }

    public static void start(Context mContext, SharedLocationBean locationBean) {
        Intent intent = new Intent(mContext, ShareSpaceDetailsActivity.class);
        intent.putExtra("locationBean", locationBean);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(null, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {
        refreshLayoutFinished();
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    private void refreshLayoutFinished() {
        mRefreshLayout.finishRefresh(true);
    }

    private void initEvent() {
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                getPresenter().getSharedLocations(App.getUser(mContext).getId());
            }
        });
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                //获取点击的是哪个item用户 如果是自己则提示
                SharedPersonBean sharedPersonBean = mlist.get(positions);
                if (!sharedPersonBean.getId().equals(App.getUser(mContext).getId())) {
                    sharedPersonBean.setShared_locations(locationBeans);
                    SharePersonDetailsActivity.start(mContext, sharedPersonBean);
                } else {
                    startHintDialog();
                }
            }
        });
    }

    private void startDialogHintCloseShareUser(int position) {
        DialogUtil.show("", "确定断开【" + locationBean.getName() + "】的共享?\n(断开后属于共享空间的非本人添加的物品也将被清空)", "确定", "取消", ShareSpaceDetailsActivity.this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                selectPositon = position;
                getPresenter().closeShareUser(App.getUser(mContext).getId(), locationBean.getCode(), mlist.get(position).getId(), 0);
            }
        }, null);
    }

    private void startHintDialog() {
        DialogUtil.show("", "请在“我的-共享管理”中管理个人共享信息", "好的", "", this, null, null);
    }
}
