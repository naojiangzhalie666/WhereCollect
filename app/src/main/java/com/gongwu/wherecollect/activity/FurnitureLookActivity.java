package com.gongwu.wherecollect.activity;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.azhon.appupdate.utils.ScreenUtil;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.adapter.FurnitureLookAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IFurnitureContract;
import com.gongwu.wherecollect.contract.presenter.FurniturePresenter;
import com.gongwu.wherecollect.net.entity.ImageData;
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
import com.gongwu.wherecollect.util.PhotosDialog;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.PopupEditBox;
import com.gongwu.wherecollect.view.PopupEditFurnitureName;
import com.gongwu.wherecollect.view.PopupEditInterlayer;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.PopupImportGoods;
import com.gongwu.wherecollect.view.PopupScrollPickerView;
import com.gongwu.wherecollect.view.furniture.ChildView;
import com.gongwu.wherecollect.view.furniture.CustomTableRowLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
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
    @BindView(R.id.strut)
    View strut;
    @BindView(R.id.box_img_iv)
    GoodsImageView boxImageView;

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
    private String resetName, addBoxName;
    private String family_code;
    //房间结构
    private RoomFurnitureResponse mRoomFurnitureResponse;
    private boolean isAddBox = false;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_furniture_look;
    }

    @Override
    protected void initViews() {
        EventBus.getDefault().register(this);
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
                //点击隔层
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
                //点击收纳盒
                if (mAdapterData.get(positions).getLevel() == AppConstant.LEVEL_BOX) {
                    String boxName = mAdapterData.get(positions).getName();
                    selectGoods = 0;
                    selectBoxBean = mAdapterData.get(positions);
                    refreshBoxListView(selectBoxBean);
                    showSelectBoxButton();
                    isBox = true;
                    if (selectView != null && selectView.getObjectBean() != null) {
                        gcNameTv.setText(selectView.getObjectBean().getName() + "/" + boxName);
                    }
                    initBoxImg();
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

    private void initBoxImg() {
        if (selectBoxBean == null) {
            boxImageView.setVisibility(View.GONE);
            return;
        }
        boxImageView.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(selectBoxBean.getImage_url()) || "null".equals(selectBoxBean.getImage_url())) {
            boxImageView.head.setImageDrawable(mContext.getDrawable(R.drawable.icon_template_box));
        } else {
            boxImageView.setImg(selectBoxBean.getImage_url());
        }
    }

    @OnClick({R.id.back_btn, R.id.look_furniture_geceng_tv, R.id.furniture_edit_layer_tv, R.id.furniture_add_box_tv, R.id.furniture_import_tv, R.id.furniture_back_tv,
            R.id.furniture_goods_details_tv, R.id.furniture_del_tv, R.id.furniture_top_tv, R.id.furniture_cancel_tv, R.id.furniture_place_tv, R.id.furniture_move_box_tv,
            R.id.furniture_remove_tv, R.id.move_goods_iv, R.id.detailed_list_tv, R.id.box_img_iv})
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
                if (tablelayout != null && tablelayout.getChildBeans() != null && tablelayout.getChildBeans().size() > 0) {
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
                }
                break;
            case R.id.furniture_add_box_tv:
                //添加收纳盒
                showEditNamePopupWindow(false, false);
                break;
            case R.id.furniture_import_tv:
                //导入物品,先获取没有归位的物品然后显示pop
                getPresenter().getImportGoodsList(App.getUser(mContext).getId(), furnitureBean.getLocation_code());
                break;
            case R.id.furniture_back_tv:
                //选中收纳盒-后退
                refreshListView(selectView.getObjectBean().getCode());
                gcNameTv.setText(selectView.getObjectBean().getName());
                showSelectLayerButton();
                //只有在收纳盒内 才有后退
                isBox = false;
                //初始化选中物品数量,bean的状态会在添加新的数据时初始化
                selectGoods = 0;
                selectBoxBean = null;
                initBoxImg();
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
                getPresenter().moveLayer(App.getUser(mContext).getId(), selectView.getObjectBean().getCode(), MainActivity.moveLayerBean.getCode(), MainActivity.moveLayerBean.getFamily_code(), App.getSelectFamilyBean().getCode());
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
            case R.id.detailed_list_tv:
                if (mData == null || mData.size() == 0) {
                    Toast.makeText(mContext, "家具内没有物品,请添加物品", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (selectView != null) {
                    selectView.setEditable(false);
                    selectView = null;
                }
                InventoryActivity.start(mContext, family_code, roomBean.get_id(), roomBean.getCode(), furnitureBean.getCode(), mRoomFurnitureResponse);
                break;
            case R.id.box_img_iv:
                if (TextUtils.isEmpty(selectBoxBean.getImage_url()) || "null".equals(selectBoxBean.getImage_url())) {
                    return;
                }
                List<ImageData> imageDatas = new ArrayList<>();
                ImageData imageData = new ImageData();
                imageData.setUrl(selectBoxBean.getImage_url());
                imageDatas.add(imageData);
                PhotosDialog photosDialog = new PhotosDialog(this, false, false, imageDatas);
                photosDialog.showPhotos(0);
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
        //初始化数据
        mData.clear();
        objects.clear();
        mBoxlist.clear();
        boxCount = AppConstant.DEFAULT_INDEX_OF;
        if (data.getLocations() != null && data.getLocations().size() > 0) {
            boxCount = data.getLocations().size();
            for (ObjectBean boxBean : data.getLocations()) {
                boxBean.setLocations(boxBean.getParents());
            }
        }
        //排序
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
        getPresenter().initBoxData(mBoxlist, data.getObjects());
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
                if (isBox) {
                    locations.addAll(selectBoxBean.getLocations());
                    BaseBean baseBean = new BaseBean();
                    baseBean.setCode(selectBoxBean.getCode());
                    baseBean.setName(selectBoxBean.getName());
                    baseBean.setLevel(selectBoxBean.getLevel());
                    locations.add(baseBean);
                } else {
                    locations.addAll(selectView.getObjectBean().getParents());
                    BaseBean baseBean = new BaseBean();
                    baseBean.setCode(selectView.getObjectBean().getCode());
                    baseBean.setName(selectView.getObjectBean().getName());
                    baseBean.setLevel(2);
                    locations.add(baseBean);
                }
                //设置导入物品新的层级
                importBean.setLocations(locations);
                //重新导入后删除原先物品的数据然后再次添加
                String id = importBean.get_id();
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
                //boxCount判断盒子有几个,将物品移到盒子后面
                mData.add(boxCount, importBean);
                //将物品添加到物品集合第一个位置
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
            sb.append(selectView.getObjectBean().getName()).append("/").append(resetName);
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
            gcNameTv.setText(selectView.getObjectBean().getName());
            selectBoxBean = null;
            initBoxImg();
            isBox = false;
            //删除盒子后,直接请求新的数据
            showSelectLayerButton();
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    family_code);
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
        furnitureBean.setLayers(data.getLayers());
        furnitureBean.setName(data.getFurniture_name());
        gcNameTv.setText("全部");
        tablelayout.init(data.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng1);
        mRoomFurnitureResponse = data;
        if (data.getParents() != null && data.getParents().size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (BaseBean bean : data.getParents()) {
                sb.append(bean.getName()).append("/");
            }
            if (sb.toString().length() > 0) {
                sb.delete(sb.length() - 1, sb.length());
                roomNameTv.setText(sb.toString());
            }
        }
    }

    @Override
    public void resetLayerNameSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            selectView.getObjectBean().setName(resetName);
            gcNameTv.setText(selectView.getObjectBean().getName());
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

    @Override
    public void onUpLoadSuccess(String path) {
        if (isAddBox) {
            getPresenter().editBoxName(App.getUser(mContext).getId(), selectBoxBean.getCode(), resetName, path);
        } else {
            getPresenter().addBox(App.getUser(mContext).getId(), selectView.getObjectBean().getCode(), addBoxName, path);
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
        strut.setVisibility(isDown ? View.GONE : View.VISIBLE);
        Drawable drawable = getResources().getDrawable(
                isDown ? R.drawable.icon_look_arrow_down : R.drawable.icon_look_arrow_up);
        // / 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(),
                drawable.getMinimumHeight());
        gcNameTv.setCompoundDrawables(null, null, drawable, null);
        if (isDown) {
            boxImageView.setVisibility(View.GONE);
        } else {
            if (selectBoxBean != null) {
                boxImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    /**
     * 点击隔层
     */
    private void onClickTable(ChildView view) {
        selectGoods = 0;
        view.setEditable(!view.isEdit());
        if (selectView != null) {
            selectView.setEditable(false);
            selectView = null;
        }
        if (view.isEdit()) {
            selectView = view;
            refreshListView(view.getObjectBean().getCode());
            showSelectLayerButton();
            gcNameTv.setText(view.getObjectBean().getName());
        } else {
            refreshListView(null);
            initButton();
            gcNameTv.setText("全部");
        }
        moveLayerView.setAlpha(view.isEdit() ? 1.0f : 0.5f);
        moveBoxTv.setAlpha(view.isEdit() ? 1.0f : 0.5f);
        moveGoodsView.setAlpha(view.isEdit() ? 1.0f : 0.5f);
        moveLayerView.setEnabled(view.isEdit());
        moveBoxTv.setEnabled(view.isEdit());
        moveGoodsIV.setEnabled(view.isEdit());
        isBox = false;
        selectBoxBean = null;
        initBoxImg();
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
        //找到该收纳盒的隔层
        if (childView != null) {
            tablelayout.unSelectChildView();
            childView.setEditable(!childView.isEdit());
            selectView = childView;
        }
        refreshListView(location.getCode());
        if (selectView != null) {
            moveLayerView.setAlpha(selectView.isEdit() ? 1.0f : 0.5f);
            moveBoxTv.setAlpha(selectView.isEdit() ? 1.0f : 0.5f);
            moveGoodsView.setAlpha(selectView.isEdit() ? 1.0f : 0.5f);
            moveLayerView.setEnabled(selectView.isEdit());
            moveBoxTv.setEnabled(selectView.isEdit());
            moveGoodsIV.setEnabled(selectView.isEdit());
        }
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
            public void onItemsClick(int position, ObjectBean bean) {
                importBean = bean;
                importPosition = position;
                postImportGoods(importBean.get_id());
            }

            @Override
            public void onLookMoreClick() {
                ImportMoreGoodsActivity.start(mContext, furnitureBean.getCode());
            }

            @Override
            public void onAddMoreClick() {
                if (selectView != null && selectView.getObjectBean() != null) {
                    selectView.getObjectBean().setLevel(AppConstant.LEVEL_INTERLAYER);
                    CameraMainActivity.start(mContext, false, selectView.getObjectBean());
                }
            }
        });
        importGoodsPopup.showPopupWindow();
        int height = 0;
        if (ScreenUtil.checkDeviceHasNavigationBar(mContext)) {
            height = ScreenUtil.getHeight(mContext) - (ScreenUtil.checkDeviceHasNavigationBar(mContext) ? ScreenUtil.getNavigationBarHeight(this) : 0);
        }
        importGoodsPopup.update(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, mContext.getResources().getDisplayMetrics()),
                height);
        importGoodsPopup.initData(beans);
        importGoodsPopup.setLookMoveVisibility(isShowLookMove ? View.VISIBLE : View.GONE);
    }

    private PopupScrollPickerView editNamePopup;

    /**
     * 添加收纳盒或者修改收纳盒名称
     */
    private void showEditNamePopupWindow(boolean isResetName, boolean isResetBox) {
        editNamePopup = new PopupScrollPickerView(mContext);
        editNamePopup.setPopupGravity(Gravity.CENTER);
        editNamePopup.setPopupClickListener(new PopupScrollPickerView.PopupClickListener() {
            @Override
            public void onImgClick() {
            }

            @Override
            public void onCommitClick(FurnitureBean bean, File file) {
                isAddBox = isResetBox;
                if (isResetBox) {
                    resetName = bean.getName();
                } else {
                    addBoxName = bean.getName();
                }
                if (file != null) {
                    getPresenter().uploadImg(mContext, file);
                    return;
                }
                if (isResetBox) {
                    getPresenter().editBoxName(App.getUser(mContext).getId(), selectBoxBean.getCode(), resetName, null);
                } else {
                    getPresenter().addBox(App.getUser(mContext).getId(), selectView.getObjectBean().getCode(), bean.getName(), null);
                }
            }

            @Override
            public void onEditNameCommitClick(String name) {
                resetName = name;
                getPresenter().resetLayerName(App.getUser(mContext).getId(), resetName, selectView.getObjectBean().getCode(), furnitureBean.getCode());

            }
        });
        editNamePopup.showPopupWindow();
        if (isResetName) {
            editNamePopup.initData(FurnitureLookActivity.this, R.string.layer_name, selectView.getObjectBean().getName(), null, false);
        } else {
            String name = selectBoxBean != null ? selectBoxBean.getName() : null;
            editNamePopup.initData(FurnitureLookActivity.this, isResetBox ? R.string.edit_box : R.string.add_box, name, null, true);
            editNamePopup.setImgPathAndEnabled(R.drawable.icon_template_box, false);
        }
    }

    private PopupEditInterlayer popup;

    private void showPopupWindow() {
        //只选一个才显示编辑模式
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
                        RoomFurnitureBean moveLayerBean = selectView.getObjectBean();
                        moveLayerBean.setFamily_code(App.getSelectFamilyBean().getCode());
                        MainActivity.moveLayerBean = moveLayerBean;
                        showMoveLayerButton();
                    }
                }, null);
            }

            @Override
            public void onSecondClick() {
                showEditNamePopupWindow(true, false);
            }

        });
        popup.showPopupWindow(editLayerTv);
    }

    private PopupEditBox boxPopup;

    private void showEditBoxPopup() {
        //只选一个才显示编辑模式
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
        boxPopup.showPopupWindow(editLayerTv);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (editNamePopup != null) {
            editNamePopup.onActivityResult(requestCode, resultCode, data);
        }
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            //编辑结构
            furnitureBean = (FurnitureBean) data.getSerializableExtra("furnitureBean");
            tablelayout.init(furnitureBean.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng1);
            getPresenter().getFurnitureDetails(App.getUser(mContext).getId(), furnitureBean.getCode(), family_code);
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
                    //移除当前选中的物品
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
        if (loading != null) {
            loading.dismiss();
        }
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.RefreshFurnitureLook msg) {
        getPresenter().getFurnitureDetails(
                App.getUser(mContext).getId(),
                furnitureBean.getCode(),
                family_code
        );
    }
}
