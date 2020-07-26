package com.gongwu.wherecollect.FragmentMain;


import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.FamilyAdministerActivity;
import com.gongwu.wherecollect.activity.EditHomeActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IHomeContract;
import com.gongwu.wherecollect.contract.presenter.HomePresenter;
import com.gongwu.wherecollect.fragment.FamilyRoomFragment;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.PopupFamilyList;
import com.gongwu.wherecollect.view.HomeFragmentAdapter;
import com.google.android.material.tabs.TabLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 主页-空间fragment
 */
public class HomeFragment extends BaseFragment<HomePresenter> implements IHomeContract.IHomeView, ViewPager.OnPageChangeListener {

    private static final String TAG = "HomeFragment";

    @BindView(R.id.user_family_name)
    TextView mFamilyName;
    @BindView(R.id.home_tab_layout)
    TabLayout mTabLayout;
    @BindView(R.id.home_view_pager)
    ViewPager mViewPager;
    @BindView(R.id.family_layout)
    View familyView;
    @BindView(R.id.family_type_iv)
    ImageView familyTypeIv;

    private boolean init;
    private boolean refreshFragment;
    private int selectPosition;
    private String familyCode;
    private List<FamilyBean> mFamilylist = new ArrayList<>();
    private List<RoomBean> rooms = new ArrayList<>();
    private List<BaseFragment> fragments = new ArrayList<>();
    private HomeFragmentAdapter adapter;

    private HomeFragment() {
    }

    public static HomeFragment getInstance() {
        return new HomeFragment();
    }

    @Override
    public View initView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public HomePresenter initPresenter() {
        return HomePresenter.getInstance();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (!isAdded()) return;
        if (!init) {
            initViews();
            init = true;
        }
        initUI();
    }

    private void initViews() {
        familyView.setVisibility(View.GONE);
        mTabLayout.setupWithViewPager(mViewPager);
        adapter = new HomeFragmentAdapter(getActivity().getSupportFragmentManager(), fragments, rooms);
        //联动
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(this);
        EventBus.getDefault().register(this);
    }

    private void initUI() {
        StatusBarUtil.setStatusBarColor(getActivity(), getResources().getColor(R.color.maincolor));
        StatusBarUtil.setLightStatusBar(getActivity(), false);
        getPresenter().getUserFamily(App.getUser(mContext).getId(), App.getUser(mContext).getNickname());
    }

    @OnClick({R.id.edit_home_iv, R.id.family_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_home_iv:
                EditHomeActivity.start(mContext, familyCode, AppConstant.DEFAULT_INDEX_OF);
                break;
            case R.id.family_layout:
                showPopupWindow();
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
                        if (!mFamilylist.get(position).getCode().equals(familyCode)) {
                            mFamilyName.setText(mFamilylist.get(position).getName());
                            familyCode = mFamilylist.get(position).getCode();
                            //设置当前家庭
                            App.setSelectFamilyBean(mFamilylist.get(position));
                            familyTypeIv.setImageDrawable(getResources().getDrawable(mFamilylist.get(position).isBeShared() ? R.drawable.ic_shared : R.drawable.ic_home));
                            SaveDate.getInstence(mContext).setFamilyCode(mFamilylist.get(position).getCode());
                            getPresenter().getUserFamilyRoom(App.getUser(mContext).getId(), familyCode);
                        }
                    } else {
                        FamilyAdministerActivity.start(mContext);
                    }
                }
            });
        }
        popup.initData(mFamilylist);
        popup.showPopupWindow(familyView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (refreshFragment) {
            getPresenter().getUserFamily(App.getUser(mContext).getId(), App.getUser(mContext).getNickname());
            refreshFragment = false;
        }
    }

    @Override
    public void getUserFamilySuccess(List<FamilyBean> data) {
        mFamilylist.clear();
        if (data != null && data.size() > 0) {
            mFamilylist.addAll(data);
            FamilyBean familyBean = new FamilyBean();
            familyBean.setSystemEdit(true);
            mFamilylist.add(familyBean);
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
            familyCode = data.get(selectPostition).getCode();
            familyView.setVisibility(View.VISIBLE);
            familyTypeIv.setImageDrawable(getResources().getDrawable(data.get(selectPostition).isBeShared() ? R.drawable.ic_shared : R.drawable.ic_home));
            SaveDate.getInstence(mContext).setFamilyCode(familyCode);
            //设置当前家庭
            App.setSelectFamilyBean(data.get(selectPostition));
            getPresenter().getUserFamilyRoom(App.getUser(mContext).getId(), familyCode);
        }
    }

    @Override
    public void getUserFamilyRoomSuccess(HomeFamilyRoomBean data) {
        fragments.clear();
        rooms.clear();
        if (data != null && data.getRooms() != null && data.getRooms().size() > 0) {
            Collections.sort(data.getRooms(), new Comparator<RoomBean>() {
                @Override
                public int compare(RoomBean lhs, RoomBean rhs) {
                    return rhs.getWeight() - lhs.getWeight();
                }
            });
            rooms.addAll(data.getRooms());
            for (int i = 0; i < data.getRooms().size(); i++) {
                fragments.add(FamilyRoomFragment.getInstance(data.getRooms().get(i), familyCode, false));
            }
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.RefreshFragment msg) {
        refreshFragment = true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.RefreshRoomsFragment msg) {
        if (fragments.size() > selectPosition) {
            fragments.get(selectPosition).refreshFragment();
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        this.selectPosition = position;
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
