package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.EditMoreGoodsAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IEditMoreGoodsContract;
import com.gongwu.wherecollect.contract.presenter.EditMoreGoodsPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.object.SelectSortActivity;
import com.gongwu.wherecollect.util.AnimationUtil;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.Loading;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class EditMoreGoodsActivity extends BaseMvpActivity<EditMoreGoodsActivity, EditMoreGoodsPresenter> implements IEditMoreGoodsContract.IEditMoreGoodsView {

    private static final String TAG = "EditMoreGoodsActivity";

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.edit_more_goods_refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.edit_more_goods_list)
    RecyclerView mRecyclerView;
    @BindView(R.id.edit_more_goods_ed_layout)
    View editBtnLayout;
    @BindView(R.id.split_view)
    View splitView;

    private int selectCount;
    private String familyCode;
    private List<ObjectBean> mlist = new ArrayList<>();

    private Loading loading;
    private EditMoreGoodsAdapter mAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_edit_more_goods;
    }

    @Override
    protected void initViews() {
        titleTv.setText(R.string.edit_more_goods);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        familyCode = getIntent().getStringExtra("familyCode");
        mAdapter = new EditMoreGoodsAdapter(mContext, mlist);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                getPresenter().getEditMoreGoodsList(App.getUser(mContext).getId(), familyCode);
            }
        });
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (mlist != null && mlist.size() > positions) {
                    mlist.get(positions).setSelect(!mlist.get(positions).isSelect());
                    mAdapter.notifyItemChanged(positions);
                    if (mlist.get(positions).isSelect()) {
                        selectCount++;
                    } else {
                        selectCount--;
                    }
                    showEditBtnLayoutUpOrDown(selectCount > 0);
                }
            }
        });
        mRefreshLayout.autoRefresh();
    }

    @OnClick({R.id.back_btn, R.id.edit_more_goods_del, R.id.edit_more_goods_location, R.id.edit_more_goods_tag})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.edit_more_goods_del:
                if (isVip()) {
                    //删除选择物品
                    DialogUtil.show(null, "删除选中物品?", "确定", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().delSelectGoods(App.getUser(mContext).getId(), mAdapter.getSelectGoodsIds());
                        }
                    }, null);
                }
                break;
            case R.id.edit_more_goods_location:
                if (isVip()) {
                    MainActivity.moveGoodsList = new ArrayList<>();
                    for (ObjectBean bean : mlist) {
                        if (bean.isSelect()) {
                            MainActivity.moveGoodsList.add(bean);
                        }
                    }
                    EventBus.getDefault().post(new EventBusMsg.SelectHomeTab());
                    finish();
                }
                break;
            case R.id.edit_more_goods_tag:
                if (isVip()) {
                    SelectSortActivity.start(mContext, null);
                }
                break;
            default:
                break;
        }
    }

    private boolean isVip() {
        if (!App.getUser(mContext).isIs_vip()) {
            BuyVIPActivity.start(mContext);
            return false;
        }
        return true;
    }

    private void showEditBtnLayoutUpOrDown(boolean show) {
        editBtnLayout.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
        if (show) {
            AnimationUtil.upSlide(editBtnLayout, 300);
        } else {
            AnimationUtil.downSlide(editBtnLayout, 300);
        }
        splitView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    protected EditMoreGoodsPresenter createPresenter() {
        return EditMoreGoodsPresenter.getInstance();
    }

    @Override
    public void getEditMoreGoodsListSuccess(List<ObjectBean> data) {
        mlist.clear();
        if (data != null && data.size() > 0) {
            mlist.addAll(data);
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void delSelectGoodsSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            showEditBtnLayoutUpOrDown(false);
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void objectsAddCategorySuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            showEditBtnLayoutUpOrDown(false);
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            ObjectBean sortBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (sortBean == null || sortBean.getCategories() == null || sortBean.getCategories().size() < 1) {
                Lg.getInstance().d(TAG, "sortBean == null");
                return;
            }
            List<String> ids = new ArrayList<>();
            for (ObjectBean bean : mlist) {
                if (bean.isSelect()) {
                    ids.add(bean.get_id());
                }
            }
            List<String> categorys = new ArrayList<>();
            for (BaseBean bean : sortBean.getCategories()) {
                categorys.add(bean.getCode());
            }
            getPresenter().objectsAddCategory(App.getUser(mContext).getId(), ids, categorys);
        }
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(null, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        refreshLayoutFinished();
    }


    @Override
    public void onError(String result) {
        if (loading != null && loading.isShowing()) {
            loading.dismiss();
        }
        refreshLayoutFinished();
    }

    private void refreshLayoutFinished() {
        mRefreshLayout.finishRefresh(true);
    }

    public static void start(Context mContext, String familyCode) {
        Intent intent = new Intent(mContext, EditMoreGoodsActivity.class);
        intent.putExtra("familyCode", familyCode);
        mContext.startActivity(intent);
    }
}
