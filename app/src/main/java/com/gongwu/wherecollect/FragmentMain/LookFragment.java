package com.gongwu.wherecollect.FragmentMain;


import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.gongwu.wherecollect.activity.AddChangWangGoodActivity;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.EditMoreGoodsActivity;
import com.gongwu.wherecollect.activity.FurnitureLookActivity;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.activity.SearchActivity;
import com.gongwu.wherecollect.activity.StatisticsActivity;
import com.gongwu.wherecollect.adapter.MainGoodsAdapter;
import com.gongwu.wherecollect.adapter.MainGoodsSortAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseFragment;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.ILookContract;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.contract.presenter.LookPresenter;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.object.GoodsDetailsActivity;
import com.gongwu.wherecollect.object.SealGoodsActivity;
import com.gongwu.wherecollect.util.AnimationUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.EmptyView;
import com.gongwu.wherecollect.view.MessageDialog;
import com.gongwu.wherecollect.view.MessageTwoBtnDialog;
import com.gongwu.wherecollect.view.PopupFamilyList;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @BindView(R.id.family_type_iv)
    ImageView familyTypeIv;
    @BindView(R.id.add_changwang_tv)
    TextView addCWGoodView;

    private FamilyBean familyBean;
    private List<ChangWangBean> changWangBeans;
    private List<FamilyBean> mFamilylist = new ArrayList<>();
    private List<MainGoodsBean> mList = new ArrayList<>();
    private List<ObjectBean> mDetailsList = new ArrayList<>();
    private MainGoodsSortAdapter mSortAdapter;
    private MainGoodsAdapter mAdapter;
    private int selectPosition = AppConstant.DEFAULT_INDEX_OF;
    private String changWangCode, goodType;
    private MessageDialog messageDialog;

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
        EventBus.getDefault().register(this);
        initStatusBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        initData();
    }

    private void initUI() {
        emptyImg.setVisibility(View.VISIBLE);
        emptyImg.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_no_goods_default));
        mSortAdapter = new MainGoodsSortAdapter(getActivity(), mList, false);
        mAdapter = new MainGoodsAdapter(getActivity(), mDetailsList, false);
        mSortRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mSortRecyclerView.setAdapter(mSortAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout mRefreshLayout) {
                getPresenter().getObjectBean(App.getUser(mContext).getId(), familyBean.getCode(), false);
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
                if (positions == AppConstant.DEFAULT_INDEX_OF) {
                    startStatistics();
                } else {
                    GoodsDetailsActivity.start(mContext, mDetailsList.get(positions));
                }
            }

            @Override
            public void onDeleteClick(int positions, View view) {
                getPresenter().delSelectGoods(App.getUser(mContext).getId(), mDetailsList.get(positions).get_id());
            }

            @Override
            public void onLocationClick(int positions, View view) {
                ObjectBean objectBean = mDetailsList.get(positions);
                String familyCode = "";
                FurnitureBean furnitureBean = new FurnitureBean();
                RoomBean roomBean = new RoomBean();
                for (BaseBean bean : objectBean.getLocations()) {
                    if (bean.getLevel() == AppConstant.LEVEL_FAMILY) {
                        familyCode = bean.getCode();
                    }
                    if (bean.getLevel() == AppConstant.LEVEL_ROOM) {
                        furnitureBean.set_id(bean.get_id());
                        furnitureBean.setLocation_code(bean.getCode());
                        roomBean.set_id(bean.get_id());
                        roomBean.setCode(bean.getCode());
                    }
                    if (bean.getLevel() == AppConstant.LEVEL_FURNITURE) {
                        furnitureBean.setCode(bean.getCode());
                    }
                }
                if (TextUtils.isEmpty(familyCode) ||
                        TextUtils.isEmpty(furnitureBean.get_id()) ||
                        TextUtils.isEmpty(furnitureBean.getLocation_code()) ||
                        TextUtils.isEmpty(furnitureBean.getCode())) {
                    return;
                }
                FurnitureLookActivity.start(mContext, familyCode, furnitureBean, objectBean, roomBean);
            }

            @Override
            public void onAddLocationClick(int positions, View view) {
                ObjectBean objectBean = mDetailsList.get(positions);
                MainActivity.moveGoodsList = new ArrayList<>();
                MainActivity.moveGoodsList.add(objectBean);
                EventBusMsg.SelectHomeTab tab = new EventBusMsg.SelectHomeTab();
                EventBus.getDefault().post(tab);
                ((MainActivity) getActivity()).selectTab(AppConstant.DEFAULT_INDEX_OF);
            }

            @Override
            public void onTopClick(int positions, View view) {
                if (mDetailsList.get(positions).getWeight() > 0) {
                    getPresenter().setGoodsNoWeight(App.getUser(mContext).getId(), mDetailsList.get(positions).get_id());
                } else {
                    getPresenter().setGoodsWeight(App.getUser(mContext).getId(), mDetailsList.get(positions).get_id());
                }
            }

            @Override
            public void onUnlickClick(int positions, View view) {
                getPresenter().goodsArchive(App.getUser(mContext).getId(), mDetailsList.get(positions).get_id());
            }
        });
    }

    private void initData() {
        if (!App.getUser(mContext).getTest()) {
            getPresenter().getChangWangList(App.getUser(mContext).getId());
        }
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


    @OnClick({R.id.look_family_name, R.id.batch_edit_iv, R.id.fm_search_layout, R.id.empty_img, R.id.add_changwang_tv})
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
            case R.id.empty_img:
            case R.id.add_changwang_tv:
                if (TextUtils.isEmpty(goodType) || TextUtils.isEmpty(changWangCode)) return;
                AddChangWangGoodActivity.start(getContext(), goodType, changWangCode);
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    private void startStatistics() {
        //vip功能
        if (mList != null && mList.size() > 0) {
            String type;
            if (mList.get(selectPosition).getCode().equals(StatisticsActivity.TYPE_ALL)) {
                type = StatisticsActivity.TYPE_ALL;
            } else if (mList.get(selectPosition).getName().equals("衣装打扮")) {
                type = StatisticsActivity.TYPE_CLOTHES;
                if (!App.getUser(mContext).isIs_vip()) {
                    BuyVIPActivity.start(mContext);
                    return;
                }
            } else if (mList.get(selectPosition).getName().equals("未分类")) {
                return;
            } else {
                type = StatisticsActivity.TYPE_OTHER;
                if (!App.getUser(mContext).isIs_vip()) {
                    BuyVIPActivity.start(mContext);
                    return;
                }
            }
            StatisticsActivity.start(mContext, familyBean.getCode(), mList.get(selectPosition).getCode(), type);
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
        if (mList != null && mList.size() > 0 && mList.get(AppConstant.DEFAULT_INDEX_OF).getObjects() != null
                && mList.get(AppConstant.DEFAULT_INDEX_OF).getObjects().size() > 0) {
            empty.setVisibility(View.GONE);
        } else {
            empty.setVisibility(View.VISIBLE);
        }
        initChangWang();
    }

    @Override
    public void delSelectGoodsSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void setGoodsWeightSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void setGoodsNoWeightSuccess(RequestSuccessBean bean) {
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

    @Override
    public void removeArchiveObjectsSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            mRefreshLayout.autoRefresh();
        }
    }

    @Override
    public void getChangWangListSuccess(List<ChangWangBean> changWangBeans) {
        if (changWangBeans != null && changWangBeans.size() > 0) {
            this.changWangBeans = changWangBeans;
        }
    }

    private void initChangWang() {
        ChangWangBean aiWangBean = changWangBeans.get(0);
        //name=爱忘爆款
        if (aiWangBean.getObject_count() > aiWangBean.getComplete()) {
            goodType = aiWangBean.getName();
            changWangCode = aiWangBean.getCode();
            if (mList != null && mList.size() > 0 && mList.get(AppConstant.DEFAULT_INDEX_OF).getObjects() != null
                    && mList.get(AppConstant.DEFAULT_INDEX_OF).getObjects().size() > 0) {
                if (addCWGoodView.getVisibility() == View.GONE) {
                    AnimationUtil.downSlide(addCWGoodView, 1000);
                    addCWGoodView.setVisibility(View.VISIBLE);
                }
            } else {
                if (addCWGoodView.getVisibility() == View.VISIBLE) {
                    addCWGoodView.setVisibility(View.GONE);
                }
            }
        } else if (changWangBeans.size() > 1) {
            //name=热门备余物
            ChangWangBean reMenBean = changWangBeans.get(1);
            if (reMenBean.getObject_count() > reMenBean.getComplete()) {
                goodType = reMenBean.getName();
                changWangCode = reMenBean.getCode();
                if (mList != null && mList.size() > 0 && mList.get(AppConstant.DEFAULT_INDEX_OF).getObjects() != null
                        && mList.get(AppConstant.DEFAULT_INDEX_OF).getObjects().size() > 0) {
                    if (addCWGoodView.getVisibility() == View.GONE) {
                        AnimationUtil.downSlide(addCWGoodView, 1000);
                        addCWGoodView.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (addCWGoodView.getVisibility() == View.VISIBLE) {
                        addCWGoodView.setVisibility(View.GONE);
                    }
                }
            } else {
                changWangCode = null;
            }
        }
    }

    private void selectSortGoods(int indexOf) {
        mSortAdapter.setSelectPosition(indexOf);
        mDetailsList.clear();
        MainGoodsBean mainGoodsBean = mList.get(mSortAdapter.getSelectPosition());
        ObjectBean headBean = new ObjectBean();
        headBean.setHead(true);
        headBean.setTotal(mainGoodsBean.getTotal());
        headBean.setNoLocation(mainGoodsBean.getNoLocation());
        mDetailsList.add(headBean);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.LookGoodsAct msg) {
        if (msg.isShowEndTimeHint) {
            StringUtils.showMessage(mContext, R.string.add_end_time_hint_text);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.SetGoodsLocationByCangWang msg) {
        MessageDialog dialog = new MessageDialog(mContext) {
            @Override
            public void submit() {
                if (msg != null && msg.addGoodList != null) {
                    MainActivity.moveGoodsList = new ArrayList<>();
                    MainActivity.moveGoodsList.clear();
                    for (ObjectBean bean : msg.addGoodList.values()) {
                        MainActivity.moveGoodsList.add(bean);
                    }
                    EventBus.getDefault().post(new EventBusMsg.SelectHomeFragmentTab());
                }
            }
        };
        dialog.show();
        dialog.setMessage(R.string.add_cangwang_hint_dialog_msg);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    public void startSealGoodsActivity() {
        initHintSeals();
    }

    private void initHintSeals() {
        if (!SaveDate.getInstence(mContext).isHintSeal()) {
            messageDialog = new MessageDialog(mContext) {
                @Override
                public void submit() {
                    initHintSeal();
                }
            };
            messageDialog.show();
            messageDialog.setMessage(R.string.seal_hint_one_tv);
        } else {
            startSealActivity();
        }
    }

    private void initHintSeal() {
        if (!SaveDate.getInstence(mContext).isHintSeal()) {
            messageDialog = new MessageDialog(mContext) {
                @Override
                public void submit() {
                    SaveDate.getInstence(mContext).setHintSeal(true);
                    startSealActivity();
                }
            };
            messageDialog.show();
            messageDialog.setMessage(R.string.seal_hint_two_tv);
        }
    }

    private void startSealActivity() {
        if (!App.getUser(mContext).isIs_vip()) {
            MessageTwoBtnDialog dialog = new MessageTwoBtnDialog(mContext) {
                @Override
                public void submit() {
                    BuyVIPActivity.start(mContext);
                }

                @Override
                public void cancel() {
                    getPresenter().removeArchiveObjects(App.getUser(mContext).getId());
                }
            };
            dialog.show();
            return;
        }
        if (familyBean != null) {
            SealGoodsActivity.start(mContext, familyBean.getCode());
        }
    }
}
