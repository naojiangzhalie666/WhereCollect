package com.gongwu.wherecollect.object;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.FurnitureLookActivity;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.adapter.MainGoodsAdapter;
import com.gongwu.wherecollect.adapter.MainGoodsSortAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.ILookContract;
import com.gongwu.wherecollect.contract.presenter.LookPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.EmptyView;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SealGoodsActivity extends BaseMvpActivity<SealGoodsActivity, LookPresenter> implements ILookContract.ILookView {

    @BindView(R.id.empty)
    EmptyView empty;
    @BindView(R.id.empty_good_layout)
    View emptyGoodLayout;
    @BindView(R.id.empty_img)
    ImageView emptyImg;
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.seal_sort_list_view)
    RecyclerView mSortRecyclerView;

    private List<MainGoodsBean> mList = new ArrayList<>();
    private List<ObjectBean> mDetailsList = new ArrayList<>();
    private MainGoodsSortAdapter mSortAdapter;
    private MainGoodsAdapter mAdapter;
    private int selectPosition = AppConstant.DEFAULT_INDEX_OF;
    private String familyCode;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_seal_goods;
    }

    @Override
    protected void initViews() {
        familyCode = getIntent().getStringExtra("familyCode");
        initStatusBar();
        initUI();
    }

    private void initStatusBar() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.seal_goods_title_bg));
        StatusBarUtil.setLightStatusBar(this, false);
    }

    private void initUI() {
        mSortAdapter = new MainGoodsSortAdapter(mContext, mList, true);
        mAdapter = new MainGoodsAdapter(mContext, mDetailsList, true);
        mSortRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mSortRecyclerView.setAdapter(mSortAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                getPresenter().getObjectBean(App.getUser(mContext).getId(), familyCode, true);
            }
        });

        mSortAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                selectPosition = positions;
                selectSortGoods(selectPosition);
            }
        });
        mAdapter.setOnItemClickListener(new MainGoodsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                GoodsDetailsActivity.start(mContext, mDetailsList.get(positions));
            }

            @Override
            public void onDeleteClick(int positions, View view) {
                getPresenter().delSelectGoods(App.getUser(mContext).getId(), mDetailsList.get(positions).get_id());
            }

            @Override
            public void onLocationClick(int positions, View view) {
                ToastUtil.show(mContext, "正在封存中的物品,无法进行定位。请先进行还原");
            }

            @Override
            public void onAddLocationClick(int positions, View view) {
                ToastUtil.show(mContext, "正在封存中的物品,无法进行添加位置。请先进行还原");
            }

            @Override
            public void onLockClick(int positions, View view) {
                getPresenter().goodsArchive(App.getUser(mContext).getId(), mDetailsList.get(positions).get_id());
            }

            @Override
            public void onUnlickClick(int positions, View view) {
                getPresenter().goodsArchive(App.getUser(mContext).getId(), mDetailsList.get(positions).get_id());
            }
        });
        mRefreshLayout.autoRefresh();
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

    private void selectSortGoods(int indexOf) {
        mSortAdapter.setSelectPosition(indexOf);
        mDetailsList.clear();
        MainGoodsBean mainGoodsBean = mList.get(mSortAdapter.getSelectPosition());
        mDetailsList.addAll(mainGoodsBean.getObjects());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(AppConstant.DEFAULT_INDEX_OF);
    }

    @Override
    protected LookPresenter createPresenter() {
        return LookPresenter.getInstance();
    }


    @Override
    public void getGoodsListSuccess(List<MainGoodsBean> data) {
        refreshLayoutFinished();
        mList.clear();
        mList.addAll(data);
        if (data.size() <= selectPosition) {
            selectPosition = AppConstant.DEFAULT_INDEX_OF;
        }
        selectSortGoods(selectPosition);
    }

    @Override
    public void delSelectGoodsSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void goodsArchiveSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            mRefreshLayout.autoRefresh();
        }
    }


    private void refreshLayoutFinished() {
        mRefreshLayout.finishRefresh(true);
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {
        refreshLayoutFinished();
    }

    @Override
    public void getUserFamilySuccess(List<FamilyBean> data) {
        //该界面不需要用到这个接口
    }

    @Override
    public void getChangWangListSuccess(List<ChangWangBean> data) {
        //该界面不需要用到这个接口
    }

    @Override
    public void removeArchiveObjectsSuccess(RequestSuccessBean bean) {
        //该界面不需要用到这个接口
    }

    public static void start(Context mContext, String familyCode) {
        Intent intent = new Intent(mContext, SealGoodsActivity.class);
        intent.putExtra("familyCode", familyCode);
        mContext.startActivity(intent);
    }
}
