package com.gongwu.wherecollect.activity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.SharePersonDetailsSpaceListAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IShareContract;
import com.gongwu.wherecollect.contract.presenter.SharePresenter;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.view.Loading;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SharePersonDetailsActivity extends BaseMvpActivity<SharePersonDetailsActivity, SharePresenter> implements IShareContract.IShareView {

    @BindView(R.id.image_btn)
    ImageButton imageBtn;
    @BindView(R.id.share_person_iv)
    ImageView share_person_iv;
    @BindView(R.id.share_user_name)
    TextView share_user_name;
    @BindView(R.id.share_user_id_tv)
    TextView share_user_id_tv;
    @BindView(R.id.share_person_details_recycler_view)
    RecyclerView mRecyclerView;

    private Loading loading;
    private SharePersonDetailsSpaceListAdapter mAdapter;

    private int deleteSpacePosition = -1;
    private SharedPersonBean sharedPersonBean;
    private List<SharedLocationBean> mlist = new ArrayList<>();
    private boolean isCloseAll = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_share_person_details;
    }

    @Override
    protected SharePresenter createPresenter() {
        return SharePresenter.getInstance();
    }

    @Override
    protected void initViews() {
        imageBtn.setVisibility(View.VISIBLE);
        imageBtn.setImageDrawable(getResources().getDrawable(R.drawable.icon_link_iv));
        sharedPersonBean = (SharedPersonBean) getIntent().getSerializableExtra("sharedPersonBean");
        if (sharedPersonBean != null) {
            ImageLoader.loadCircle(this, share_person_iv, sharedPersonBean.getAvatar(), R.drawable.ic_user_error);
            share_user_name.setText(sharedPersonBean.getNickname());
            share_user_id_tv.setText("ID: " + sharedPersonBean.getUsid());
            for (SharedLocationBean bean : sharedPersonBean.getShared_locations()) {
                if (bean != null) {
                    mlist.add(bean);
                }
            }
        }
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new SharePersonDetailsSpaceListAdapter(this, mlist, App.getUser(mContext).getId()) {
            @Override
            public void closeSpace(int position) {
                closeSpaceDialog(position);
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * 断开空间
     */
    private void closeSpaceDialog(int position) {
        deleteSpacePosition = position;
        DialogUtil.show("", "确定断开【" + mlist.get(deleteSpacePosition).getName() + "】的共享?\n(断开后属于共享空间的非本人添加的物品也将被清空)", "确定", "取消", this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                getPresenter().closeShareUser(App.getUser(mContext).getId(), mlist.get(deleteSpacePosition).getCode()
                        , sharedPersonBean.getId(), 0);
            }
        }, null);
    }

    /**
     * 断开用户不保留数据
     */
    private void startDeletaHintDialog() {
        DialogUtil.show("", "确定断开与@" + sharedPersonBean.getNickname() + "的全部共享?\n(断开后属于共享空间的非本人添加的物品也将被清空)", "确定", "取消", this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                isCloseAll = true;
                getPresenter().closeShareUser(App.getUser(mContext).getId(), ""
                        , sharedPersonBean.getId(), 0);
            }
        }, null);
    }


    @OnClick({R.id.back_btn, R.id.image_btn, R.id.add_space_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.image_btn:
                startDeletaHintDialog();
                break;
            case R.id.add_space_iv:
                Toast.makeText(mContext, "没有更多可共享空间~", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public static void start(Context mContext, SharedPersonBean bean) {
        Intent intent = new Intent(mContext, SharePersonDetailsActivity.class);
        intent.putExtra("sharedPersonBean", bean);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    @Override
    public void closeShareUserSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            EventBus.getDefault().post(new EventBusMsg.UpdateShareMsg());
            if (isCloseAll) {
                finish();
                return;
            }
            if (deleteSpacePosition >= 0 && mlist.size() > deleteSpacePosition) {
                mlist.remove(deleteSpacePosition);
                mAdapter.notifyDataSetChanged();
                deleteSpacePosition = -1;
            }
        }
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

    @Override
    public void getSharedUsersListSuccess(List<SharedPersonBean> data) {

    }

    @Override
    public void getSharedLocationsSuccess(List<SharedLocationBean> data) {

    }
}
