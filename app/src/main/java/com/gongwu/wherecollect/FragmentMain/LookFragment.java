package com.gongwu.wherecollect.FragmentMain;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.EditMoreGoodsActivity;
import com.gongwu.wherecollect.activity.SearchActivity;
import com.gongwu.wherecollect.adapter.MainGoodsAdapter;
import com.gongwu.wherecollect.adapter.MainGoodsSortAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.ILookContract;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.contract.presenter.LookPresenter;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.object.GoodsDetailsActivity;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.EmptyView;
import com.gongwu.wherecollect.view.PopupFamilyList;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页-查看fragment
 */
public class LookFragment extends BaseFragment<LookPresenter> implements ILookContract.ILookView {
    private static final String TAG = "LookFragment";

    private boolean init = false;

    @BindView(R.id.empty)
    EmptyView empty;
    @BindView(R.id.empty_good_layout)
    View emptyGoodLayout;
    @BindView(R.id.empty_img)
    ImageView emptyImg;
    @BindView(R.id.look_family_name)
    TextView mFamilyName;
    @BindView(R.id.refresh_layout)
    RefreshLayout mRefreshLayout;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.sort_list_view)
    RecyclerView mSortRecyclerView;
    @BindView(R.id.goods_number_tv)
    TextView goodsNumberTv;
    @BindView(R.id.goods_not_location_tv)
    TextView goodsNotLocationTv;
    @BindView(R.id.family_type_iv)
    ImageView familyTypeIv;

    private FamilyBean familyBean;
    private List<FamilyBean> mFamilylist = new ArrayList<>();
    private List<MainGoodsBean> mList = new ArrayList<>();
    private List<ObjectBean> mDetailsList = new ArrayList<>();
    private MainGoodsSortAdapter mSortAdapter;
    private MainGoodsAdapter mAdapter;
    private int selectPosition = AppConstant.DEFAULT_INDEX_OF;

    private LookFragment() {
    }

    public static LookFragment getInstance() {
        return new LookFragment();
    }


    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_look, container, false);
    }

    @Override
    public LookPresenter initPresenter() {
        return LookPresenter.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isAdded()) return;
        if (!init) {
            initUI();
            init = true;
        }
        initStatusBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initUI() {
        mSortAdapter = new MainGoodsSortAdapter(getActivity(), mList);
        mAdapter = new MainGoodsAdapter(getActivity(), mDetailsList);
        mSortRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mSortRecyclerView.setAdapter(mSortAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                getPresenter().getObjectBean(App.getUser(mContext).getId(), familyBean.getCode());
            }
        });

        mSortAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                selectPosition = positions;
                selectSortGoods(selectPosition);
            }
        });
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                GoodsDetailsActivity.start(getContext(), mDetailsList.get(positions));
            }
        });
    }

    private void initData() {
        getPresenter().getUserFamily(App.getUser(mContext).getId(), App.getUser(mContext).getNickname());
    }

    private void initStatusBar() {
        StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.maincolor));
        StatusBarUtil.setLightStatusBar(getActivity(), false);
    }

    @Override
    public void getUserFamilySuccess(List<FamilyBean> data) {
        mFamilylist.clear();
        if (data != null && data.size() > 0 && mRefreshLayout != null) {
            mFamilylist.addAll(data);
            //判断是有缓存
            String saveFamilyCode = SaveDate.getInstence(mContext).getFamilyCode();
            int selectPostition = AppConstant.DEFAULT_INDEX_OF;
            if (!TextUtils.isEmpty(saveFamilyCode)) {
                for (int i = 0; i < mFamilylist.size(); i++) {
                    if (!TextUtils.isEmpty(mFamilylist.get(i).getCode()) && saveFamilyCode.equals(mFamilylist.get(i).getCode())) {
                        selectPostition = i;
                        break;
                    }
                }
            }
            mFamilyName.setText(data.get(selectPostition).getName());
            this.familyBean = data.get(selectPostition);
            familyTypeIv.setVisibility(View.VISIBLE);
            familyTypeIv.setImageDrawable(getResources().getDrawable(mFamilylist.get(selectPostition).isBeShared() ? R.drawable.ic_shared : R.drawable.ic_home));
            SaveDate.getInstence(mContext).setFamilyCode(this.familyBean.getCode());
            //设置当前家庭
            App.setSelectFamilyBean(this.familyBean);
            mRefreshLayout.autoRefresh();
        }
    }

    @OnClick({R.id.look_family_name, R.id.batch_edit_iv, R.id.fm_search_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.look_family_name:
                showPopupWindow();
                break;
            case R.id.batch_edit_iv:
                EditMoreGoodsActivity.start(mContext, familyBean.getCode());
                break;
            case R.id.fm_search_layout:
                SearchActivity.start(mContext);
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    private PopupFamilyList popup;

    private void showPopupWindow() {
        //只选一个才显示编辑模式
        if (popup == null) {
            popup = new PopupFamilyList(mContext);
            popup.setBackground(Color.TRANSPARENT);
            popup.setPopupGravity(Gravity.BOTTOM | Gravity.CENTER);
            popup.setOnItemClickListener(new PopupFamilyList.OnItemClickListener() {
                @Override
                public void onItemsClick(int position, View v) {
                    if (!mFamilylist.get(position).isSystemEdit()) {
                        if (!mFamilylist.get(position).getCode().equals(familyBean.getCode())) {
                            mFamilyName.setText(mFamilylist.get(position).getName());
                            familyBean = mFamilylist.get(position);
                            //设置当前家庭
                            familyTypeIv.setImageDrawable(getResources().getDrawable(mFamilylist.get(position).isBeShared() ? R.drawable.ic_shared : R.drawable.ic_home));
                            SaveDate.getInstence(mContext).setFamilyCode(mFamilylist.get(position).getCode());
                            App.setSelectFamilyBean(mFamilylist.get(position));
                            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
                            mRefreshLayout.autoRefresh();
                        }
                    }
                    mRefreshLayout.autoRefresh();
                }
            });
        }
        popup.initData(mFamilylist);
        popup.showPopupWindow(mFamilyName);
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

    private void selectSortGoods(int indexOf) {
        mSortAdapter.setSelectPosition(indexOf);
        mDetailsList.clear();
        MainGoodsBean mainGoodsBean = mList.get(mSortAdapter.getSelectPosition());
        goodsNumberTv.setText(String.valueOf(mainGoodsBean.getTotal()));
        goodsNotLocationTv.setText(String.valueOf(mainGoodsBean.getNoLocation()));
        mDetailsList.addAll(mainGoodsBean.getObjects());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.smoothScrollToPosition(AppConstant.DEFAULT_INDEX_OF);
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
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    private void refreshLayoutFinished() {
        mRefreshLayout.finishRefresh(true);
    }

}
