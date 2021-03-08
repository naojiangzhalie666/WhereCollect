package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.AdministerFamilyListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IAdministerFamilyContract;
import com.gongwu.wherecollect.contract.presenter.AdministerFamilyPresenter;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FamilyListBean;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.Loading;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FamilyAdministerActivity extends BaseMvpActivity<FamilyAdministerActivity, AdministerFamilyPresenter> implements IAdministerFamilyContract.IAdministerFamilyView {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.family_list_view)
    RecyclerView mRecyclerView;

    private List<FamilyListBean> mlist = new ArrayList<>();
    private AdministerFamilyListAdapter mAdapter;
    private Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_administer_family;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        titleTv.setText(R.string.administer_family);
        mAdapter = new AdministerFamilyListAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new AdministerFamilyListAdapter.OnItemChildClickListener() {
            @Override
            public void onItemClick(FamilyBean bean) {
                boolean isMyFamily = App.getUser(mContext).getId().equals(bean.getUser_id());
                FamilyAdministerDetailsActivity.start(mContext, bean, isMyFamily);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getPresenter().getFamilyList(App.getUser(mContext).getId());
    }

    @OnClick({R.id.back_btn, R.id.add_family})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.add_family:
                if (App.getUser(mContext).isIs_vip()) {
                    AddFamilyActivity.start(mContext);
                } else {
                    BuyVIPActivity.start(mContext);
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected AdministerFamilyPresenter createPresenter() {
        return AdministerFamilyPresenter.getInstance();
    }

    @Override
    public void getFamilyListSuccess(MyFamilyListBean bean) {
        mlist.clear();
        if (bean.getMy() != null && bean.getMy().size() > 0) {
            FamilyListBean familyListBean = new FamilyListBean();
            familyListBean.setTitle("我的");
            familyListBean.setFamilys(bean.getMy());
            mlist.add(familyListBean);
        }
        if (bean.getBeShared() != null && bean.getBeShared().size() > 0) {
            FamilyListBean familyListBean = new FamilyListBean();
            familyListBean.setTitle("共享中");
            familyListBean.setFamilys(bean.getBeShared());
            mlist.add(familyListBean);
        }
        mAdapter.notifyDataSetChanged();
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
    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    public static void start(Context mContext) {
        Intent intent = new Intent(mContext, FamilyAdministerActivity.class);
        mContext.startActivity(intent);
    }
}
