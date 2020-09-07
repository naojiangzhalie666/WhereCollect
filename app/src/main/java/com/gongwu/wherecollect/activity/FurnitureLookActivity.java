package com.gongwu.wherecollect.activity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.FurnitureLookAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IFurnitureContract;
import com.gongwu.wherecollect.contract.presenter.FurniturePresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.object.GoodsDetailsActivity;
import com.gongwu.wherecollect.util.AnimationUtil;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.PopupEditBox;
import com.gongwu.wherecollect.view.PopupEditFurnitureName;
import com.gongwu.wherecollect.view.PopupEditInterlayer;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.PopupImportGoods;
import com.gongwu.wherecollect.view.furniture.ChildView;
import com.gongwu.wherecollect.view.furniture.CustomTableRowLayout;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 家具详情界面
 */
public class FurnitureLookActivity extends BaseMvpActivity<FurnitureLookActivity, FurniturePresenter> implements IFurnitureContract.IFurnitureView {

    private static final String TAG = "FurnitureLookActivity";

    @BindView(R.id.furniture_name_tv)
    TextView furnitureNameTv;
    @BindView(R.id.room_name_tv)
    TextView roomNameTv;
    @BindView(R.id.tablelayout)
    CustomTableRowLayout tablelayout;
    @BindView(R.id.look_furniture_geceng_tv)
    TextView gcNameTv;
    @BindView(R.id.tablelayout_view)
    LinearLayout tabLayout;
    @BindView(R.id.look_furniture_goods_layout)
    View goodsLayout;
    @BindView(R.id.furniture_look_recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.furniture_edit_layer_tv)
    TextView editLayerTv;
    @BindView(R.id.furniture_edit_layer_view)
    View editLayerView;
    @BindView(R.id.furniture_add_box_view)
    View addBoxView;
    @BindView(R.id.furniture_import_view)
    View importView;
    @BindView(R.id.furniture_del_view)
    View delGoodsView;
    @BindView(R.id.furniture_remove_view)
    View removeGoodsView;
    @BindView(R.id.furniture_top_view)
    View topGoodsView;
    @BindView(R.id.furniture_goods_details_view)
    View detGoodsView;
    @BindView(R.id.furniture_back_view)
    View editBackView;
    @BindView(R.id.furniture_cancel_view)
    View cancelView;
    @BindView(R.id.furniture_place_view)
    View placeView;
    @BindView(R.id.furniture_place_tv)
    View moveLayerView;
    @BindView(R.id.furniture_move_box_view)
    View moveBoxView;
    @BindView(R.id.furniture_move_box_tv)
    View moveBoxTv;
    @BindView(R.id.furniture_move_goods_view)
    View moveGoodsView;
    @BindView(R.id.move_goods_iv)
    GoodsImageView moveGoodsIV;
    @BindView(R.id.furniture_move_goods_number)
    TextView redNumberTv;
    @BindView(R.id.furniture_cancel_tv)
    TextView cancelTv;

    private boolean isDown;
    private boolean isBox;
    private boolean init;
    private Loading loading;
    private ChildView selectView;
    private FurnitureLookAdapter mAdapter;
    private RoomBean roomBean;
    private FurnitureBean furnitureBean;
    private ObjectBean selectGoodsBean;
    //总数据(含有物品和收纳盒)
    private List<ObjectBean> mData = new ArrayList<>();
    //adapter 区分隔层物品
    private List<ObjectBean> mAdapterData = new ArrayList<>();
    //收纳盒list
    private List<ObjectBean> mBoxlist = new ArrayList<>();
    //物品集合(只含物品)
    private List<ObjectBean> objects = new ArrayList<>();
    //导入物品
    private int importPosition = -1;
    private ObjectBean importBean;
    //box数量
    private int boxCount = 0;
    private ObjectBean selectBoxBean;
    private int selectGoods = 0;
    private float y;
    private String resetName;
    private String family_code;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_furniture_look;
    }

    @Override
    protected void initViews() {
        y = tabLayout.getY();
        family_code = getIntent().getStringExtra("family_code");
        roomBean = (RoomBean) getIntent().getSerializableExtra("roomBean");
        furnitureBean = (FurnitureBean) getIntent().getSerializableExtra("furnitureBean");
        selectGoodsBean = (ObjectBean) getIntent().getSerializableExtra("selectGoodsBean");
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.maincolor));
        StatusBarUtil.setLightStatusBar(this, false);
        mAdapter = new FurnitureLookAdapter(mContext, mAdapterData);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        tablelayout.setOnItemClickListener(new CustomTableRowLayout.OnItemClickListener() {
            @Override
            public void itemClick(ChildView view) {
                selectGoodsBean = null;
                tablelayout.unSelectChildView();
                onClickTable(view);
                mRecyclerView.smoothScrollToPosition(0);
            }
        });
        tablelayout.setOnInitListener(new CustomTableRowLayout.OnInitListener() {
            @Override
            public void OnInit() {
                if (selectGoodsBean != null) {
                    ChildView childView = tablelayout.findView(selectGoodsBean);
                    if (childView != null) {
                        childView.setSelectCount(true);
                    }
                }
            }
        });
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                selectGoodsBean = null;
                if (mAdapterData.get(positions).getLevel() == AppConstant.LEVEL_BOX) {
                    gcNameTv.setText(gcNameTv.getText().toString() + "/" + mAdapterData.get(positions).getName());
                    selectGoods = 0;
                    selectBoxBean = mAdapterData.get(positions);
                    refreshBoxListView(selectBoxBean);
                    showSelectBoxButton();
                    isBox = true;
                } else {
                    if (getEditMoveType()) return;
                    ChildView childView = tablelayout.findView(mAdapterData.get(positions));
                    if (childView != null) {
                        childView.setSelectCount(!mAdapterData.get(positions).isSelect());
                    }
                    mAdapterData.get(positions).setSelect(!mAdapterData.get(positions).isSelect());
                    mAdapter.notifyItemChanged(positions);
                    showEditButtonBySelectCount(mAdapterData.get(positions).isSelect());
                }
            }
        });

        if (MainActivity.moveLayerBean != null) {
            showMoveLayerButton();
        } else if (MainActivity.moveBoxBean != null) {
            showMoveBoxButton();
        } else if (MainActivity.moveGoodsList != null && MainActivity.moveGoodsList.size() > 0) {
            showMoveGoodsButton();
        }
    }

    @OnClick({R.id.back_btn, R.id.look_furniture_geceng_tv, R.id.furniture_edit_layer_tv, R.id.furniture_add_box_tv, R.id.furniture_import_tv, R.id.furniture_back_tv,
            R.id.furniture_goods_details_tv, R.id.furniture_del_tv, R.id.furniture_top_tv, R.id.furniture_cancel_tv, R.id.furniture_place_tv, R.id.furniture_move_box_tv,
            R.id.furniture_remove_tv, R.id.move_goods_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.look_furniture_geceng_tv:
                //物品列表布局扩大 缩小
                onClickGeCengTv();
                break;
            case R.id.furniture_edit_layer_tv:
                //编辑
                if (selectView != null) {
                    if (isBox) {
                        //编辑收纳盒
                        showEditBoxPopup();
                    } else {
                        //编辑隔层
                        showPopupWindow();
                    }
                } else {
                    //编辑结构
                    EditFurniturePatternActivity.start(mContext, furnitureBean, family_code);
                }
                break;
            case R.id.furniture_add_box_tv:
                //添加收纳盒
                showEditNamePopupWindow(false, false);
                break;
            case R.id.furniture_import_tv:
                //放置物品
                getPresenter().getImportGoodsList(App.getUser(mContext).getId(), furnitureBean.getLocation_code());
                break;
            case R.id.furniture_back_tv:
                //选中收纳盒-后退
                refreshListView(selectView.getObjectBean().getCode());
                gcNameTv.setText(getPresenter().getLoction(selectView.getObjectBean()));
                showSelectLayerButton();
                //只有在收纳盒内 才有后退
                isBox = false;
                selectBoxBean = null;
                break;
            case R.id.furniture_goods_details_tv:
                //物品详情
                GoodsDetailsActivity.start(mContext, mAdapter.getSelectGoods());
                break;
            case R.id.furniture_del_tv:
                //删除选择物品
                DialogUtil.show(null, "删除选中物品?", "确定", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().delSelectGoods(App.getUser(mContext).getId(), mAdapter.getSelectGoodsIds());
                    }
                }, null);
                break;
            case R.id.furniture_top_tv:
                //置顶物品
                getPresenter().topSelectGoods(App.getUser(mContext).getId(), furnitureBean.getCode(), mAdapter.getSelectGoodsIdsToList());
                break;
            case R.id.furniture_cancel_tv:
                //退出放置隔层
                MainActivity.moveBoxBean = null;
                MainActivity.moveLayerBean = null;
                if (MainActivity.moveGoodsList != null) {
                    mData.clear();
                    mData.addAll(mBoxlist);
                    mData.addAll(objects);
                    MainActivity.moveGoodsList = null;
                }
                if (selectView == null) {
                    initButton();
                } else {
                    if (isBox) {
                        showSelectBoxButton();
                    } else {
                        showSelectLayerButton();
                    }
                }
                refreshListView(selectView == null ? null : selectView.getObjectBean().getCode());
                break;
            case R.id.furniture_place_tv:
                if (selectView == null) {
                    Toast.makeText(mContext, "请选择隔层", Toast.LENGTH_SHORT).show();
                    return;
                }
                //放置隔层
                getPresenter().moveLayer(App.getUser(mContext).getId(), selectView.getObjectBean().getCode(), MainActivity.moveLayerBean.getCode());
                break;
            case R.id.furniture_move_box_tv:
                if (selectView == null) {
                    Toast.makeText(mContext, "请选择隔层", Toast.LENGTH_SHORT).show();
                    return;
                }
                getPresenter().moveBox(App.getUser(mContext).getId(), selectView.getObjectBean().getCode(), MainActivity.moveBoxBean.getCode());
                break;
            case R.id.furniture_remove_tv:
                showMoveGoodsButton();
                break;
            case R.id.move_goods_iv:
                if (MainActivity.moveGoodsList != null && MainActivity.moveGoodsList.size() > 0) {
                    if (MainActivity.moveGoodsList.size() > 1) {
                        showImportGoodsPopup(MainActivity.moveGoodsList, false);
                    } else {
                        importBean = MainActivity.moveGoodsList.get(AppConstant.DEFAULT_INDEX_OF);
                        importPosition = AppConstant.DEFAULT_INDEX_OF;
                        postImportGoods(importBean.get_id());
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (furnitureBean != null && !init) {
            getPresenter().getFurnitureLayersOrBox(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    AppConstant.LEVEL_INTERLAYER,
                    family_code,
                    roomBean.get_id()
            );
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    family_code
            );
            init = true;
        }
    }

    @Override
    protected FurniturePresenter createPresenter() {
        return FurniturePresenter.getInstance();
    }

    @Override
    public void getFurnitureDetailsSuccess(RoomFurnitureGoodsBean data) {
        mData.clear();
        if (data.getLocations() != null && data.getLocations().size() > 0) {
            boxCount = data.getLocations().size();
            for (ObjectBean boxBean : data.getLocations()) {
                boxBean.setLocations(boxBean.getParents());
            }
        }
        if (data.getObjects() != null && data.getObjects().size() > 0) {
            Collections.sort(data.getObjects(), new Comparator<ObjectBean>() {
                @Override
                public int compare(ObjectBean lhs, ObjectBean rhs) {
                    return rhs.getWeight() - lhs.getWeight();
                }
            });
        }
        mData.addAll(data.getLocations());
        mBoxlist.addAll(data.getLocations());
        mData.addAll(data.getObjects());
        objects.addAll(data.getObjects());
        mRecyclerView.smoothScrollToPosition(AppConstant.DEFAULT_INDEX_OF);
        if (selectView == null) {
            mAdapterData.clear();
            mAdapterData.addAll(mData);
            if (selectGoodsBean != null) {
                for (int i = 0; i < mAdapterData.size(); i++) {
                    ObjectBean bean = mAdapterData.get(i);
                    if (bean.get_id().equals(selectGoodsBean.get_id())) {
                        bean.setSelect(true);
                        showEditButtonBySelectCount(true);
                        mRecyclerView.smoothScrollToPosition(i);
                        break;
                    }
                }
            }
            mAdapter.notifyDataSetChanged();
        } else {
            refreshListView(selectView.getObjectBean().getCode());
        }
    }

    @Override
    public void getImportGoodsListSuccess(ImportGoodsBean bean) {
        showImportGoodsPopup(bean.getItems(), true);
    }

    @Override
    public void delSelectGoodsSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            mAdapter.delSelectGoods(mData);
        }
    }

    @Override
    public void topSelectGoodsSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            tablelayout.unSelectChildView();
            if (selectView == null) {
                initButton();
            }
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    family_code
            );
        }
    }

    /**
     * 导入物品成功
     *
     * @param bean
     */
    @Override
    public void importGoodsSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            //刷新首页房间数据
            EventBus.getDefault().post(new EventBusMsg.RefreshRoomsFragment());
            if (importBean != null) {
                List<BaseBean> locations = new ArrayList<>();
                BaseBean baseBean = new BaseBean();
                baseBean.setCode(selectView.getObjectBean().getCode());
                locations.add(baseBean);
                if (isBox) {
                    BaseBean baseBean1 = new BaseBean();
                    baseBean1.setCode(selectBoxBean.getCode());
                    locations.add(baseBean1);
                }
                //设置导入物品新的层级
                importBean.setLocations(locations);
                mData.add(boxCount, importBean);
                objects.add(AppConstant.DEFAULT_INDEX_OF, importBean);
                String code = isBox ? selectBoxBean.getCode() : selectView.getObjectBean().getCode();
                refreshListView(code);
                importBean = null;
                if (moveGoodsView.getVisibility() == View.VISIBLE
                        && importPosition >= 0 && MainActivity.moveGoodsList != null
                        && MainActivity.moveGoodsList.size() > 0) {
                    MainActivity.moveGoodsList.remove(importPosition);
                    showMoveGoodsButton();
                    if (MainActivity.moveGoodsList.size() == 0) {
                        cancelTv.performClick();
                        if (importGoodsPopup != null && importGoodsPopup.isShowing()) {
                            importGoodsPopup.dismiss();
                        }
                    }
                }
                importPosition = -1;
            } else {
                finish();
            }
        }
    }

    @Override
    public void editBoxNameSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            StringBuilder sb = new StringBuilder();
            sb.append(getPresenter().getLoction(selectView.getObjectBean())).append("/").append(resetName);
            gcNameTv.setText(sb.toString());
            for (ObjectBean goods : mData) {
                if (goods.getCode().equals(selectBoxBean.getCode())) {
                    goods.setName(resetName);
                    break;
                }
            }
            resetName = null;
        }
    }

    @Override
    public void delBoxSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            gcNameTv.setText(getPresenter().getLoction(selectView.getObjectBean()));
            showSelectLayerButton();
            for (ObjectBean goods : mData) {
                if (goods.getCode().equals(selectBoxBean.getCode())) {
                    mData.remove(goods);
                    break;
                }
            }
            selectBoxBean = null;
            refreshListView(selectView.getObjectBean().getCode());
        }
    }

    @Override
    public void moveLayerSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            MainActivity.moveLayerBean = null;
            showSelectLayerButton();
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    family_code);
        }
    }

    @Override
    public void moveBoxSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            MainActivity.moveBoxBean = null;
            showSelectLayerButton();
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    family_code);
        }
    }

    @Override
    public void getFurnitureLayersOrBoxSuccess(RoomFurnitureResponse data) {
        furnitureNameTv.setText(data.getFurniture_name());
        roomNameTv.setText(data.getRoom_name());
        furnitureBean.setLayers(data.getLayers());
        furnitureBean.setName(data.getFurniture_name());
        tablelayout.init(data.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng1);
        if (data.getParents() != null && data.getParents().size() > 0) {
            RoomFurnitureBean furnitureBean = data.getLayers().get(AppConstant.DEFAULT_INDEX_OF);
            gcNameTv.setText(getPresenter().getLoction(furnitureBean));
        }
    }

    @Override
    public void resetLayerNameSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            selectView.getObjectBean().setName(resetName);
            gcNameTv.setText(getPresenter().getLoction(selectView.getObjectBean()));
        }
    }

    @Override
    public void addBoxSuccess(RoomBean rooms) {
        if (rooms != null) {
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    family_code
            );
        }
    }

    private void onClickGeCengTv() {
        isDown = !isDown;
        if (isDown) {
            AnimationUtil.upSlide(goodsLayout, 300);
        } else {
            AnimationUtil.downSlide(goodsLayout, 300, y);
        }
        tabLayout.setVisibility(isDown ? View.GONE : View.VISIBLE);

        Drawable drawable = getResources().getDrawable(
                isDown ? R.drawable.icon_look_arrow_down : R.drawable.icon_look_arrow_up);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        gcNameTv.setCompoundDrawables(null, null, drawable, null);
    }

    private void onClickTable(ChildView view) {
        selectGoods = 0;
        view.setEditable(!view.isEdit());
        gcNameTv.setText(getPresenter().getLoction(view.getObjectBean()));
        if (selectView != null) {
            selectView.setEditable(false);
            selectView = null;
        }
        if (view.isEdit()) {
            selectView = view;
            refreshListView(view.getObjectBean().getCode());
            showSelectLayerButton();
        } else {
            refreshListView(null);
            initButton();
        }
        moveLayerView.setAlpha(view.isEdit() ? 1.0f : 0.5f);
        moveBoxTv.setAlpha(view.isEdit() ? 1.0f : 0.5f);
        moveGoodsView.setAlpha(view.isEdit() ? 1.0f : 0.5f);
        moveLayerView.setEnabled(view.isEdit());
        moveBoxTv.setEnabled(view.isEdit());
        moveGoodsIV.setEnabled(view.isEdit());
        isBox = false;
        selectBoxBean = null;
    }

    private void refreshListView(String location_code) {
        mAdapterData.clear();
        if (TextUtils.isEmpty(location_code)) {
            for (ObjectBean bean : mData) {
                bean.setSelect(false);
            }
            mAdapterData.addAll(mData);
            mAdapter.notifyDataSetChanged();
            return;
        }
        for (int i = 0; i < mData.size(); i++) {
            ObjectBean bean = mData.get(i);
            if (bean.getLocations() == null || bean.getLocations().size() <= 0) {
                continue;
            }
            for (int j = 0; j < bean.getLocations().size(); j++) {
                if (location_code.equals(bean.getLocations().get(j).getCode())) {
                    bean.setSelect(false);
                    mAdapterData.add(bean);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private void refreshBoxListView(ObjectBean location) {
        ChildView childView = tablelayout.findView(location);
        if (childView != null) {
            childView.setEditable(!childView.isEdit());
            selectView = childView;
        }
        refreshListView(location.getCode());
        moveLayerView.setAlpha(selectView.isEdit() ? 1.0f : 0.5f);
        moveBoxTv.setAlpha(selectView.isEdit() ? 1.0f : 0.5f);
        moveGoodsView.setAlpha(selectView.isEdit() ? 1.0f : 0.5f);
        moveLayerView.setEnabled(selectView.isEdit());
        moveBoxTv.setEnabled(selectView.isEdit());
        moveGoodsIV.setEnabled(selectView.isEdit());
    }

    private PopupImportGoods importGoodsPopup;

    /**
     * 导入物品
     */
    private void showImportGoodsPopup(final List<ObjectBean> beans, boolean isShowLookMove) {
        importGoodsPopup = new PopupImportGoods(mContext);
        importGoodsPopup.setPopupGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
        importGoodsPopup.setOnItemClickListener(new PopupImportGoods.OnItemClickListener() {
            @Override
            public void onItemsClick(int position, View v) {
                importBean = beans.get(position);
                importPosition = position;
                postImportGoods(importBean.get_id());
            }

            @Override
            public void onLookMoreClick() {
                ImportMoreGoodsActivity.start(mContext, furnitureBean.getCode());
            }
        });
        importGoodsPopup.showPopupWindow();
        importGoodsPopup.initData(beans);
        importGoodsPopup.setLookMoveVisibility(isShowLookMove ? View.VISIBLE : View.GONE);
    }

    private PopupEditFurnitureName editNamePopup;

    /**
     * 添加收纳盒或者修改收纳盒名称
     */
    private void showEditNamePopupWindow(boolean isResetName, boolean isResetBox) {
        if (editNamePopup == null) {
            editNamePopup = new PopupEditFurnitureName(mContext);
            editNamePopup.setPopupGravity(Gravity.CENTER);
            editNamePopup.setPopupClickListener(new PopupEditFurnitureName.PopupClickListener() {
                @Override
                public void onImgClick() {
                }

                @Override
                public void onCommitClick(FurnitureBean bean) {
                    if (isResetBox) {
                        resetName = bean.getName();
                        getPresenter().editBoxName(App.getUser(mContext).getId(), selectBoxBean.getCode(), resetName);
                    } else {
                        getPresenter().addBox(App.getUser(mContext).getId(), selectView.getObjectBean().getCode(), bean.getName());
                    }
                }

                @Override
                public void onEditNameCommitClick(String name) {
                    resetName = name;
                    getPresenter().resetLayerName(App.getUser(mContext).getId(), resetName, selectView.getObjectBean().getCode(), furnitureBean.getCode());

                }
            });
        }
        editNamePopup.showPopupWindow();
        if (isResetName) {
            editNamePopup.initData(R.string.layer_name, selectView.getObjectBean().getName(), null, false);
        } else {
            String name = selectBoxBean != null ? selectBoxBean.getName() : null;
            editNamePopup.initData(isResetBox ? R.string.edit_box : R.string.add_box, name, null, true);
            editNamePopup.setImgPathAndEnabled(R.drawable.icon_template_box, false);
        }
    }

    private PopupEditInterlayer popup;

    private void showPopupWindow() {
        //只选一个才显示编辑模式
        if (popup == null) {
            popup = new PopupEditInterlayer(mContext);
            popup.setBackground(Color.TRANSPARENT);
            popup.setPopupGravity(Gravity.TOP | Gravity.CENTER);
            popup.setItemName(R.string.layer_move, R.string.reset_name);
            popup.setPopupClickListener(new PopupEditInterlayer.EditInterlayerClickListener() {
                @Override
                public void onFirstClick() {
                    DialogUtil.show(null, "整体迁移该隔层内收纳盒和物品?", "确定", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MainActivity.moveLayerBean = selectView.getObjectBean();
                            showMoveLayerButton();
                        }
                    }, null);
                }

                @Override
                public void onSecondClick() {
                    showEditNamePopupWindow(true, false);
                }

            });
        }
        popup.showPopupWindow(editLayerTv);
    }

    private PopupEditBox boxPopup;

    private void showEditBoxPopup() {
        //只选一个才显示编辑模式
        if (boxPopup == null) {
            boxPopup = new PopupEditBox(mContext);
            boxPopup.setBackground(Color.TRANSPARENT);
            boxPopup.setPopupGravity(Gravity.TOP | Gravity.CENTER);
            boxPopup.setPopupClickListener(new PopupEditBox.EditInterlayerClickListener() {
                @Override
                public void onResetNameClick() {
                    showEditNamePopupWindow(false, true);
                }

                @Override
                public void onReMoveClick() {
                    DialogUtil.show(null, "整体迁移该盒子?", "确定", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ObjectBean objectBean = new ObjectBean();
                            objectBean.setCode(selectBoxBean.getCode());
                            MainActivity.moveBoxBean = objectBean;
                            refreshListView(selectView.getObjectBean().getCode());
                            showMoveBoxButton();
                        }
                    }, null);
                }

                @Override
                public void onDelClick() {
                    DialogUtil.show(null, "确定删除该收纳盒吗?", "确定", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            getPresenter().delBox(App.getUser(mContext).getId(), selectBoxBean.getCode());
                        }
                    }, null);
                }
            });
        }
        boxPopup.showPopupWindow(editLayerTv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            //编辑结构
            furnitureBean = (FurnitureBean) data.getSerializableExtra("furnitureBean");
            tablelayout.init(furnitureBean.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng1);
        } else if (AppConstant.REQUEST_CODE_OTHER == requestCode && RESULT_OK == resultCode) {
            //判断是否是导入更多物品界面返回
            postImportGoods(data.getStringExtra("location_codes"));
        }
    }

    private void postImportGoods(String object_codes) {
        if (!TextUtils.isEmpty(object_codes)) {
            String code = isBox ? selectBoxBean.getCode() : selectView.getObjectBean().getCode();
            getPresenter().importGoods(App.getUser(mContext).getId(), furnitureBean.getLocation_code(), object_codes, code);
        }
    }

    private void hideEditButton() {
        editLayerView.setVisibility(View.GONE);
        addBoxView.setVisibility(View.GONE);
        importView.setVisibility(View.GONE);
        delGoodsView.setVisibility(View.GONE);
        topGoodsView.setVisibility(View.GONE);
        removeGoodsView.setVisibility(View.GONE);
        detGoodsView.setVisibility(View.GONE);
        editBackView.setVisibility(View.GONE);
        cancelView.setVisibility(View.GONE);
        placeView.setVisibility(View.GONE);
        moveBoxView.setVisibility(View.GONE);
        moveGoodsView.setVisibility(View.GONE);
    }

    /**
     * 默认
     */
    private void initButton() {
        if (getEditMoveType()) return;
        hideEditButton();
        editLayerView.setVisibility(View.VISIBLE);
        editLayerTv.setText(selectView == null ? R.string.edit_layer : R.string.edit_text);
    }

    /**
     * 选收纳盒
     */
    private void showSelectBoxButton() {
        if (getEditMoveType()) return;
        hideEditButton();
        editLayerView.setVisibility(View.VISIBLE);
        editLayerTv.setText(R.string.edit_box);
        importView.setVisibility(View.VISIBLE);
        editBackView.setVisibility(View.VISIBLE);
    }

    /**
     * 选隔层
     */
    private void showSelectLayerButton() {
        if (getEditMoveType()) return;
        initButton();
        addBoxView.setVisibility(View.VISIBLE);
        importView.setVisibility(View.VISIBLE);
    }

    /**
     * 选多个物品
     */
    private void showSelectMoreGoodsButton() {
        if (getEditMoveType()) return;
        hideEditButton();
        delGoodsView.setVisibility(View.VISIBLE);
        topGoodsView.setVisibility(View.VISIBLE);
        removeGoodsView.setVisibility(View.VISIBLE);
    }

    /**
     * 选单个物品(有物品详情按钮)
     */
    private void showSelectGoodsButton() {
        if (getEditMoveType()) return;
        showSelectMoreGoodsButton();
        detGoodsView.setVisibility(View.VISIBLE);
    }

    /**
     * 迁移隔层
     */
    private void showMoveLayerButton() {
        hideEditButton();
        cancelView.setVisibility(View.VISIBLE);
        placeView.setVisibility(View.VISIBLE);
        moveLayerView.setAlpha(selectView != null ? 1.0f : 0.5f);
        moveLayerView.setEnabled(selectView != null);
    }

    /**
     * 迁移收纳盒
     */
    private void showMoveBoxButton() {
        hideEditButton();
        cancelView.setVisibility(View.VISIBLE);
        moveBoxView.setVisibility(View.VISIBLE);
        moveBoxTv.setAlpha(selectView != null ? 1.0f : 0.5f);
        moveBoxTv.setEnabled(selectView != null);
    }

    private void showMoveGoodsButton() {
        hideEditButton();
        cancelView.setVisibility(View.VISIBLE);
        moveGoodsView.setVisibility(View.VISIBLE);
        moveGoodsView.setAlpha(selectView != null ? 1.0f : 0.5f);
        moveGoodsIV.setEnabled(selectView != null);
        if (MainActivity.moveGoodsList == null || MainActivity.moveGoodsList.size() == 0) {
            MainActivity.moveGoodsList = new ArrayList<>();
            for (int i = 0; i < mAdapterData.size(); i++) {
                if (mAdapterData.get(i).isSelect()) {
                    MainActivity.moveGoodsList.add(mAdapterData.get(i));
                    String id = mAdapterData.get(i).get_id();
                    //删除总数据
                    for (int j = 0; j < mData.size(); j++) {
                        if (id.equals(mData.get(j).get_id())) {
                            mData.remove(j);
                            break;
                        }
                    }
                    //删除物品list
                    for (int a = 0; a < objects.size(); a++) {
                        if (id.equals(objects.get(a).get_id())) {
                            objects.remove(a);
                            break;
                        }
                    }
                    mAdapterData.remove(i);
                    i--;
                }
            }
            mAdapter.notifyDataSetChanged();
        }
        if (MainActivity.moveGoodsList == null || MainActivity.moveGoodsList.size() < 1) return;
        moveGoodsIV.setCircle(MainActivity.moveGoodsList.get(MainActivity.moveGoodsList.size() - 1));
        moveGoodsIV.setTextSize(8);
        if (MainActivity.moveGoodsList.size() > 1) {
            redNumberTv.setVisibility(View.VISIBLE);
            redNumberTv.setText(String.valueOf(MainActivity.moveGoodsList.size()));
        } else {
            redNumberTv.setVisibility(View.GONE);
        }
    }

    /**
     * 更新编辑按钮
     */
    public void showEditButtonBySelectCount(boolean isSelect) {
        //是否点击物品
        if (isSelect) {
            selectGoods++;
        } else {
            selectGoods--;
            if (selectGoods < 0) {
                selectGoods = 0;
            }
        }
        if (selectGoods == 0) {
            //是否选择隔层
            if (selectView != null) {
                //是否选择收纳盒
                if (editBackView.getVisibility() == View.VISIBLE) {
                    showSelectBoxButton();
                } else {
                    showSelectLayerButton();
                }
            } else {
                initButton();
            }
        } else if (selectGoods == 1) {
            //是否选择收纳盒
            if (editBackView.getVisibility() == View.VISIBLE) {
                showSelectGoodsButton();
                editBackView.setVisibility(View.VISIBLE);
            } else {
                showSelectGoodsButton();
            }
        } else if (selectGoods > 1) {
            //是否选择收纳盒
            if (editBackView.getVisibility() == View.VISIBLE) {
                showSelectMoreGoodsButton();
                editBackView.setVisibility(View.VISIBLE);
            } else {
                showSelectMoreGoodsButton();
            }
        }

    }

    private boolean getEditMoveType() {
        return MainActivity.moveLayerBean != null || MainActivity.moveBoxBean != null || (MainActivity.moveGoodsList != null && MainActivity.moveGoodsList.size() > 0);
    }

    public static void start(Context context, String family_code, FurnitureBean furnitureBean, ObjectBean selectGoodsBean, RoomBean roomBean) {
        Intent intent = new Intent(context, FurnitureLookActivity.class);
        intent.putExtra("family_code", family_code);
        intent.putExtra("furnitureBean", furnitureBean);
        intent.putExtra("selectGoodsBean", selectGoodsBean);
        intent.putExtra("roomBean", roomBean);
        context.startActivity(intent);
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
        if (loading != null) {
            loading.dismiss();
        }
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
    }
}
