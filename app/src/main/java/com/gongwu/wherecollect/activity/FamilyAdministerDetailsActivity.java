package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.AdministerFamilyDetailsAdapter;
import com.gongwu.wherecollect.adapter.AdministerUserImgAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAdministerFamilyDetailsContract;
import com.gongwu.wherecollect.contract.presenter.AdministerFamilyDetailsPresenter;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FamilyListDetailsBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.SharedUserBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.PopupEditFurnitureName;
import com.gongwu.wherecollect.view.Loading;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FamilyAdministerDetailsActivity extends BaseMvpActivity<FamilyAdministerDetailsActivity, AdministerFamilyDetailsPresenter> implements IAdministerFamilyDetailsContract.IAdministerFamilyDetailsView {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_maincolor_tv)
    TextView commitTv;
    @BindView(R.id.family_details_list_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.user_family_name)
    TextView userFamilyNameTv;
    @BindView(R.id.family_switch)
    Switch sharedSwitch;
    @BindView(R.id.add_shared_family_layout)
    View sharedLayout;
    @BindView(R.id.shared_family_list_layout)
    LinearLayout sharedListLayout;
    @BindView(R.id.shared_family_list_view)
    RecyclerView mSharedListView;

    private Loading loading;

    private String familyCode;
    private String familyName;
    private FamilyBean familyBean;
    private boolean isMyFamily;
    private FamilyListDetailsBean detailsBean;
    private List<RoomBean> mlist;
    private List<SharedUserBean> mUserlist;
    private AdministerFamilyDetailsAdapter mAdapter;
    private AdministerUserImgAdapter mUserAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_administer_family_details;
    }

    @Override
    protected void initViews() {
        familyBean = (FamilyBean) getIntent().getSerializableExtra("familyBean");
        familyCode = familyBean.getCode();
        familyName = familyBean.getName();
        isMyFamily = getIntent().getBooleanExtra("isMyFamily", false);
        mlist = new ArrayList<>();
        mUserlist = new ArrayList<>();
        commitTv.setText(R.string.administer_furniture);
        commitTv.setVisibility(isMyFamily ? View.VISIBLE : View.GONE);
        mAdapter = new AdministerFamilyDetailsAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);

        mUserAdapter = new AdministerUserImgAdapter(mContext, mUserlist);
        mSharedListView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        mSharedListView.setAdapter(mUserAdapter);
        mSharedListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    sharedListLayout.performClick();  //模拟父控件的点击
                }
                return false;
            }
        });
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));

        mAdapter.setOnItemClickListener(new AdministerFamilyDetailsAdapter.OnItemChildClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                EditHomeActivity.start(mContext, familyCode, position);
            }
        });
        sharedSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //vip功能
                if (!App.getUser(mContext).isIs_vip()) {
                    BuyVIPActivity.start(mContext);
                    sharedSwitch.setChecked(false);
                    return;
                }
                if (sharedSwitch.isChecked()) {
                    sharedLayout.setVisibility(View.VISIBLE);
                } else {
                    sharedSwitch.setChecked(true);
                    DialogUtil.show("确认关闭家庭共享吗？", "关闭后共享关系全部清空",
                            "确定", "取消", FamilyAdministerDetailsActivity.this, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getPresenter().disShareFamily(App.getUser(mContext).getId(), familyCode);
                                }
                            }, null);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(familyCode)) {
            getPresenter().getFamilyDetails(App.getUser(mContext).getId(), familyCode);
        }
    }

    @OnClick({R.id.back_btn, R.id.title_commit_maincolor_tv, R.id.delete_family, R.id.user_family_name, R.id.add_shared_family_layout, R.id.shared_family_list_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.title_commit_maincolor_tv:
                if (detailsBean != null) {
                    EditRoomActivity.start(mContext, familyCode, detailsBean.getRooms());
                }
                break;
            case R.id.delete_family:
                DialogUtil.show("确认删除该家庭吗？", "删除后所有物品位置信息将清空",
                        "确定", "取消", this, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                getPresenter().delFamily(App.getUser(mContext).getId(), familyCode);
                            }
                        }, null);
                break;
            case R.id.user_family_name:
                if (detailsBean != null) {
                    showPopupWindow();
                }
                break;
            case R.id.add_shared_family_layout:
            case R.id.shared_family_list_layout:
                FamilyAdministerSharedActivity.start(mContext, familyBean);
                break;
            default:
                break;
        }
    }

    @Override
    public void getFamilyDetailsSuccess(FamilyListDetailsBean bean) {
        this.detailsBean = bean;
        if (bean.getSharedUser() != null) {
            userFamilyNameTv.setText(familyName);
        }
        sharedSwitch.setChecked(bean.isFamilyShared());
        if (isMyFamily && bean.isFamilyShared() && bean.getBeSharedUsers() != null && bean.getBeSharedUsers().size() > 0) {
            sharedListLayout.setVisibility(View.VISIBLE);
            mUserlist.clear();
            mUserlist.add(bean.getSharedUser());
            mUserlist.addAll(bean.getBeSharedUsers());
            mUserAdapter.notifyDataSetChanged();
        }
        mlist.clear();
        mlist.addAll(bean.getRooms());
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void delFamilySuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            finish();
        }
    }

    @Override
    public void disShareFamilySuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            if (isMyFamily) {
                sharedSwitch.setChecked(false);
                sharedLayout.setVisibility(View.GONE);
                sharedListLayout.setVisibility(View.GONE);
            } else {
                finish();
            }
        }
    }

    @Override
    public void editFamilySuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            userFamilyNameTv.setText(familyName);
        }
    }

    PopupEditFurnitureName editNamePopup;

    private void showPopupWindow() {
        if (editNamePopup == null) {
            editNamePopup = new PopupEditFurnitureName(mContext);
            editNamePopup.setBackground(Color.TRANSPARENT);
            editNamePopup.setPopupGravity(Gravity.CENTER);
            editNamePopup.setPopupClickListener(new PopupEditFurnitureName.PopupClickListener() {

                @Override
                public void onCommitClick(FurnitureBean bean) {

                }

                @Override
                public void onEditNameCommitClick(String name) {
                    familyName = name;
                    getPresenter().editFamily(App.getUser(mContext).getId(), familyCode, name);
                }

                @Override
                public void onImgClick() {

                }
            });
        }
        editNamePopup.showPopupWindow();
        editNamePopup.initData(R.string.edit_family_name, userFamilyNameTv.getText().toString(), null, false);
    }

    public static void start(Context mContext, FamilyBean bean, boolean isMyFamily) {
        Intent intent = new Intent(mContext, FamilyAdministerDetailsActivity.class);
        intent.putExtra("familyBean", bean);
        intent.putExtra("isMyFamily", isMyFamily);
        mContext.startActivity(intent);
    }

    @Override
    protected AdministerFamilyDetailsPresenter createPresenter() {
        return AdministerFamilyDetailsPresenter.getInstance();
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
}
