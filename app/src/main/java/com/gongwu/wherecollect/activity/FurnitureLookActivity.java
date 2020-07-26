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
import com.gongwu.wherecollect.util.StatusBarUtil;
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

    private boolean isDown;
    private boolean isBox;
    private Loading loading;
    private ChildView selectView;
    private FurnitureLookAdapter mAdapter;
    private FurnitureBean furnitureBean;
    //总数据
    private List<ObjectBean> mData = new ArrayList<>();
    //adapter 区分隔层物品
    private List<ObjectBean> mAdapterData = new ArrayList<>();
    //收纳盒list
    private List<ObjectBean> mBoxlist = new ArrayList<>();
    //物品集合
    private List<ObjectBean> objects = new ArrayList<>();
    //导入物品
    private ObjectBean importBean;
    //box数量
    private int boxCount = 0;
    private int selectGoods = 0;
    private float y;
    private String resetName;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_furniture_look;
    }

    @Override
    protected void initViews() {
        y = tabLayout.getY();
        furnitureBean = (FurnitureBean) getIntent().getSerializableExtra("furnitureBean");
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.maincolor));
        StatusBarUtil.setLightStatusBar(this, false);
        mAdapter = new FurnitureLookAdapter(mContext, mAdapterData);
        mRecyclerView.setLayoutManager(new GridLayoutManager(mContext, 4, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setAdapter(mAdapter);
        tablelayout.setOnItemClickListener(new CustomTableRowLayout.OnItemClickListener() {
            @Override
            public void itemClick(ChildView view) {
                tablelayout.unSelectChildView();
                onClickTable(view);
            }
        });
        mAdapter.setOnItemClickListener(new MyOnItemClickListener() {
            @Override
            public void onItemClick(int positions, View view) {
                if (mAdapterData.get(positions).getLevel() == AppConstant.LEVEL_BOX) {
                    gcNameTv.setText(gcNameTv.getText().toString() + "/" + mAdapterData.get(positions).getName());
                    selectGoods = 0;
                    refreshBoxListView(mAdapterData.get(positions));
                    showSelectBoxButton();
                    isBox = true;
                } else {
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
    }

    @OnClick({R.id.back_btn, R.id.look_furniture_geceng_tv, R.id.furniture_edit_layer_tv, R.id.furniture_add_box_tv, R.id.furniture_import_tv, R.id.furniture_back_tv,
            R.id.furniture_goods_details_tv, R.id.furniture_del_tv, R.id.furniture_top_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.look_furniture_geceng_tv:
                onClickGeCengTv();
                break;
            case R.id.furniture_edit_layer_tv:
                if (selectView != null) {
                    if (isBox) {
                        showEditBoxPopup();
                    } else {
                        showPopupWindow();
                    }
                } else {
                    EditFurniturePatternActivity.start(mContext, furnitureBean);
                }
                break;
            case R.id.furniture_add_box_tv:
                showEditNamePopupWindow(false);
                break;
            case R.id.furniture_import_tv:
                getPresenter().getImportGoodsList(App.getUser(mContext).getId(), furnitureBean.getLocation_code());
                break;
            case R.id.furniture_back_tv:
                refreshListView(selectView.getObjectBean().getCode());
                gcNameTv.setText(getPresenter().getLoction(selectView.getObjectBean()));
                showSelectLayerButton();
                break;
            case R.id.furniture_goods_details_tv:
                GoodsDetailsActivity.start(mContext, mAdapter.getSelectGoods());
                break;
            case R.id.furniture_del_tv:
                DialogUtil.show(null, "删除选中物品?", "确定", "取消", (Activity) mContext, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().delSelectGoods(App.getUser(mContext).getId(), mAdapter.getSelectGoodsIds());
                    }
                }, null);
                break;
            case R.id.furniture_top_tv:
                getPresenter().topSelectGoods(App.getUser(mContext).getId(), furnitureBean.getCode(), mAdapter.getSelectGoodsIdsToList());
                break;
            default:
                break;
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
                }

                @Override
                public void onSecondClick() {
                    showEditNamePopupWindow(true);
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

                }

                @Override
                public void onReMoveClick() {

                }

                @Override
                public void onDelClick() {

                }
            });
        }
        boxPopup.showPopupWindow(editLayerTv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (furnitureBean != null) {
            getPresenter().getFurnitureLayersOrBox(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    AppConstant.LEVEL_INTERLAYER,
                    getIntent().getStringExtra("family_code"),
                    furnitureBean.get_id()
            );
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    getIntent().getStringExtra("family_code")
            );
        }
    }

    @Override
    protected FurniturePresenter createPresenter() {
        return FurniturePresenter.getInstance();
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
        mData.addAll(data.getObjects());
        mAdapterData.clear();
        mAdapterData.addAll(mData);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getImportGoodsListSuccess(ImportGoodsBean bean) {
        showImportGoodsPopup(bean.getItems());
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
            getPresenter().getFurnitureDetails(
                    App.getUser(mContext).getId(),
                    furnitureBean.getCode(),
                    getIntent().getStringExtra("family_code")
            );
        }
    }

    @Override
    public void importGoodsSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            if (importBean != null) {
                List<BaseBean> locations = new ArrayList<>();
                BaseBean baseBean = new BaseBean();
                baseBean.setCode(selectView.getObjectBean().getCode());
                locations.add(baseBean);
                importBean.setLocations(locations);
                mData.add(boxCount, importBean);
                for (int i = 0; i < mAdapterData.size(); i++) {
                    if (mAdapterData.get(i).getLevel() != AppConstant.LEVEL_BOX) {
                        mAdapterData.add(i, importBean);
                        break;
                    }
                }
                mAdapter.notifyDataSetChanged();
                importBean = null;
            } else {
                EventBus.getDefault().post(new EventBusMsg.RefreshRoomsFragment());
                finish();
            }
        }
    }

    @Override
    public void getFurnitureLayersOrBoxSuccess(RoomFurnitureResponse data) {
        furnitureNameTv.setText(data.getFurniture_name());
        roomNameTv.setText(data.getRoom_name());
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
                    getIntent().getStringExtra("family_code")
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
        mAdapterData.clear();
        if (location == null || TextUtils.isEmpty(location.getCode())) {
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
                if (location.getCode().equals(bean.getLocations().get(j).getCode()) && bean.getLevel() != AppConstant.LEVEL_BOX) {
                    bean.setSelect(false);
                    mAdapterData.add(bean);
                }
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    private PopupImportGoods importGoodsPopup;

    /**
     * 导入物品
     */
    private void showImportGoodsPopup(List<ObjectBean> beans) {
        if (importGoodsPopup == null) {
            importGoodsPopup = new PopupImportGoods(mContext);
            importGoodsPopup.setPopupGravity(Gravity.CENTER_VERTICAL | Gravity.RIGHT);
            importGoodsPopup.setOnItemClickListener(new PopupImportGoods.OnItemClickListener() {
                @Override
                public void onItemsClick(int position, View v) {
                    importBean = beans.get(position);
                    getPresenter().importGoods(App.getUser(mContext).getId(), furnitureBean.getCode(), importBean.get_id(), selectView.getObjectBean().getCode());
                }

                @Override
                public void onLookMoreClick() {
                    ImportMoreGoodsActivity.start(mContext, furnitureBean.getCode());
                }
            });
        }
        importGoodsPopup.showPopupWindow();
        importGoodsPopup.initData(beans);
    }

    private PopupEditFurnitureName editNamePopup;

    /**
     * 添加收纳盒或者修改收纳盒名称
     */
    private void showEditNamePopupWindow(boolean isResetName) {
        if (editNamePopup == null) {
            editNamePopup = new PopupEditFurnitureName(mContext);
            editNamePopup.setPopupGravity(Gravity.CENTER);
            editNamePopup.setPopupClickListener(new PopupEditFurnitureName.PopupClickListener() {
                @Override
                public void onImgClick() {

                }

                @Override
                public void onCommitClick(FurnitureBean bean) {
                    getPresenter().addBox(App.getUser(mContext).getId(), selectView.getObjectBean().getCode(), bean.getName());
                }

                @Override
                public void onEditNameCommitClick(String name) {
                    resetName = name;
                    getPresenter().resetLayerName(App.getUser(mContext).getId(), name, selectView.getObjectBean().getCode(), furnitureBean.getCode());
                }
            });
        }
        editNamePopup.showPopupWindow();
        if (isResetName) {
            editNamePopup.initData(R.string.layer_name, selectView.getObjectBean().getName(), null, false);
        } else {
            editNamePopup.initData(R.string.add_box, null, null, true);
            editNamePopup.setImgPathAndEnabled(R.drawable.icon_template_box, false);
        }
    }

    public static void start(Context context, String family_code, FurnitureBean furnitureBean) {
        Intent intent = new Intent(context, FurnitureLookActivity.class);
        intent.putExtra("family_code", family_code);
        intent.putExtra("furnitureBean", furnitureBean);
        context.startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            furnitureBean = (FurnitureBean) data.getSerializableExtra("furnitureBean");
            tablelayout.init(furnitureBean.getLayers(), CustomTableRowLayout.shape_width, R.drawable.shape_geceng1);
        } else if (AppConstant.REQUEST_CODE_OTHER == requestCode && RESULT_OK == resultCode) {
            String codes = data.getStringExtra("location_codes");
            if (!TextUtils.isEmpty(codes)) {
                getPresenter().importGoods(App.getUser(mContext).getId(), furnitureBean.getCode(), codes, selectView.getObjectBean().getCode());
            }
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
        isBox = false;
    }

    private void initButton() {
        hideEditButton();
        editLayerView.setVisibility(View.VISIBLE);
        editLayerTv.setText(selectView == null ? R.string.edit_layer : R.string.edit_text);
    }

    private void showSelectBoxButton() {
        hideEditButton();
        editLayerView.setVisibility(View.VISIBLE);
        editLayerTv.setText(R.string.edit_box);
        importView.setVisibility(View.VISIBLE);
        editBackView.setVisibility(View.VISIBLE);
    }

    private void showSelectLayerButton() {
        initButton();
        addBoxView.setVisibility(View.VISIBLE);
        importView.setVisibility(View.VISIBLE);
    }

    private void showSelectMoreGoodsButton() {
        hideEditButton();
        delGoodsView.setVisibility(View.VISIBLE);
        topGoodsView.setVisibility(View.VISIBLE);
        removeGoodsView.setVisibility(View.VISIBLE);
    }

    private void showSelectGoodsButton() {
        showSelectMoreGoodsButton();
        detGoodsView.setVisibility(View.VISIBLE);
    }

    public void showEditButtonBySelectCount(boolean isSelect) {
        if (isSelect) {
            selectGoods++;
        } else {
            selectGoods--;
            if (selectGoods < 0) {
                selectGoods = 0;
            }
        }
        if (selectGoods == 0) {
            if (selectView != null) {
                if (editBackView.getVisibility() == View.VISIBLE) {
                    showSelectBoxButton();
                } else {
                    showSelectLayerButton();
                }
            } else {
                initButton();
            }
        } else if (selectGoods == 1) {
            if (editBackView.getVisibility() == View.VISIBLE) {
                showSelectGoodsButton();
                editBackView.setVisibility(View.VISIBLE);
            } else {
                showSelectGoodsButton();
            }
        } else if (selectGoods > 1) {
            if (editBackView.getVisibility() == View.VISIBLE) {
                showSelectMoreGoodsButton();
                editBackView.setVisibility(View.VISIBLE);
            } else {
                showSelectMoreGoodsButton();
            }
        }
    }
}
