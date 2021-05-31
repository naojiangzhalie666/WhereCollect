package com.gongwu.wherecollect.object;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.AddRemindActivity;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.FurnitureLookActivity;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.adapter.GoodsInfoViewAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IGoodsDetailsContract;
import com.gongwu.wherecollect.contract.presenter.GoodsDetailsPresenter;
import com.gongwu.wherecollect.net.entity.GoodsInfoBean;
import com.gongwu.wherecollect.net.entity.ImageData;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.DateUtil;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.FileUtil;
import com.gongwu.wherecollect.util.PhotosDialog;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.EditGoodsImgDialog;
import com.gongwu.wherecollect.view.EditTextWatcher;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.ObjectInfoEditView;
import com.gongwu.wherecollect.view.PopupEditMenuGoods;
import com.gongwu.wherecollect.view.SortBelongerDialog;
import com.gongwu.wherecollect.view.SortChildDialog;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsDetailsActivity extends BaseMvpActivity<GoodsDetailsActivity, GoodsDetailsPresenter> implements IGoodsDetailsContract.IGoodsDetailsView, ObjectInfoEditView.OnItemClickListener {

    @BindView(R.id.back_btn)
    ImageButton backBtn;
    @BindView(R.id.image_btn)
    ImageButton imageBtn;
    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.title_save_tv)
    TextView mSaveView;
    @BindView(R.id.title_canle_tv)
    TextView mCanleView;
    @BindView(R.id.add_goods_sort_tv)
    TextView sortTv;
    @BindView(R.id.add_img_view)
    GoodsImageView mImageView;
    @BindView(R.id.location_layout)
    LinearLayout locationLayout;
    @BindView(R.id.goods_details_location_tv)
    TextView locationTv;
    @BindView(R.id.remind_layout)
    View remindLayout;
    @BindView(R.id.remind_name_tv)
    TextView remindNameTv;
    @BindView(R.id.remind_time_tv)
    TextView remindTimeTv;
    @BindView(R.id.goods_info_view)
    RecyclerView goodsInfoListView;
    @BindView(R.id.goods_info_tv)
    TextView goodsInfoTypeTv;
    @BindView(R.id.goods_info_edit_view)
    ObjectInfoEditView goodsInfoEditView;
    @BindView(R.id.goods_info_edit_tv)
    TextView goodsInfoEditTypeView;
    @BindView(R.id.goods_info_name_et)
    EditText mEditText;
    @BindView(R.id.goods_info_empty_view)
    View goodsInfoEmptyView;
    @BindView(R.id.activity_goods_add)
    View activityLayout;

    private GoodsInfoViewAdapter mAdapter;
    private List<GoodsInfoBean> mGoodsInfos = new ArrayList<>();

    private ObjectBean objectBean;
    private ObjectBean oldBean;
    private RemindBean remindBean;
    private Loading loading;
    private PopupEditMenuGoods popup;
    private boolean isEditGoodsInfo = false;

    private SortChildDialog sortChildDialog;
    private EditGoodsImgDialog imgDialog;
    private List<BaseBean> mOneLists = new ArrayList<>();
    private List<BaseBean> mTwoLists = new ArrayList<>();
    private List<BaseBean> mThreeLists = new ArrayList<>();
    private boolean initOne, initTwo;
    private String type = null;

    /**
     * 图书扫描后修改公共属性
     */
    String ISBN = "";

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initViews() {
        isEditGoodsInfo = false;
        mTitleView.setText(R.string.goods_text);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mAdapter = new GoodsInfoViewAdapter(mContext, mGoodsInfos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {
                //禁止上下滑动
                return false;
            }
        };
        goodsInfoListView.setLayoutManager(gridLayoutManager);
        goodsInfoListView.setAdapter(mAdapter);
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        oldBean = (ObjectBean) objectBean.clone();
        initData();
        goodsInfoEditView.setViewBackground(R.drawable.shape_white_r10dp);
        goodsInfoEditView.setOnItemClickListener(this);
        goodsInfoEditView.setOnEditListener(new ObjectInfoEditView.OnEditListener() {
            @Override
            public void change() {
                objectBean = goodsInfoEditView.getGoodsInfoBean();
                setTitleViewState(getPresenter().goodsInfosIsEdit(objectBean, oldBean));
            }
        });
        mEditText.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                objectBean.setName(mEditText.getText().toString().trim());
                setTitleViewState(getPresenter().goodsInfosIsEdit(objectBean, oldBean));
            }
        });
    }

    private void initData() {
        if (objectBean != null) {
            initInfoData();
            goodsInfoEditView.init(objectBean);
            if (!TextUtils.isEmpty(objectBean.getName())) {
                mEditText.setText(objectBean.getName());
                mEditText.setSelection(mEditText.getText().toString().trim().length());
            }
            if (objectBean.getCategories() != null && objectBean.getCategories().size() > 0) {
                sortTv.setText(objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getName());
                sortTv.setTextColor(getResources().getColor(R.color.color333));
            }
            if (objectBean.getObject_url().contains("#")) {
                int resId = Color.parseColor(objectBean.getObject_url());
                mImageView.setResourceColor(objectBean.getName(), resId, 3);
            } else {
                mImageView.setImg(objectBean.getObject_url(), 3, true);
            }
            if (StringUtils.getGoodsLoction(objectBean).equals("未归位")) {
                locationLayout.setVisibility(View.GONE);
            } else {
                locationLayout.setVisibility(View.VISIBLE);
                locationTv.setText(StringUtils.getGoodsLoction(objectBean));
            }
        }
    }

    private void initInfoData() {
        mGoodsInfos.clear();
        mGoodsInfos.addAll(StringUtils.getGoodsInfos(objectBean));
        if (!isEditGoodsInfo) {
            if (mGoodsInfos.size() > 0) {
                goodsInfoListView.setVisibility(View.VISIBLE);
                goodsInfoEditView.setVisibility(View.GONE);
                goodsInfoEmptyView.setVisibility(View.GONE);
                goodsInfoEditTypeView.setVisibility(View.VISIBLE);
                goodsInfoEditTypeView.setText(isEditGoodsInfo ? R.string.stop_text : R.string.edit_text);
                mAdapter.notifyDataSetChanged();
            } else {
                goodsInfoListView.setVisibility(View.GONE);
                goodsInfoEditView.setVisibility(View.GONE);
                goodsInfoEmptyView.setVisibility(View.VISIBLE);
                goodsInfoEditTypeView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (objectBean != null) {
            getPresenter().getGoodsRemindsById(App.getUser(mContext).getId(), objectBean.get_id());
        }
    }

    /**
     * 点击确认
     */
    private void onClickCommit() {
        //如果图片没有地址，则传一个颜色服务牌
        if (TextUtils.isEmpty(objectBean.getObject_url())) {
            objectBean.setObject_url("#E66868");
        }
        if (objectBean.getObject_url().contains("#")) {
            //调用接口
            addOrEditGoods();
        } else if (objectBean.getObject_url().contains("http")) {
            addOrEditGoods();
        } else {
            //图片有地址 直接上传
            getPresenter().uploadImg(mContext, objectBean.getObjectFiles());
        }
    }

    private void addOrEditGoods() {
        getPresenter().editGoods(mContext, objectBean, mEditText.getText().toString(), ISBN);
    }

    @OnClick({R.id.back_btn, R.id.title_canle_tv, R.id.title_save_tv, R.id.add_goods_sort_tv, R.id.add_img_view,
            R.id.image_btn, R.id.goods_details_location_tv, R.id.remind_item_layout, R.id.goods_info_edit_tv,
            R.id.goods_info_empty_view})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_canle_tv:
                isEditGoodsInfo = false;
                objectBean = (ObjectBean) oldBean.clone();
                setTitleViewState(isEditGoodsInfo);
                initData();
                break;
            case R.id.title_save_tv:
                onClickCommit();
                break;
            case R.id.add_img_view:
                imgDialog = new EditGoodsImgDialog(this, objectBean) {
                    @Override
                    public void getResult(File file) {
                        objectBean.setObject_url(file.getAbsolutePath());
                        mImageView.setImg(objectBean.getObject_url(), 3);
                        setTitleViewState(true);
                    }

                    @Override
                    public void editImg() {
                        if (objectBean.getObject_url().contains("http")) {
                            if (mImageView.getBitmap() != null) {
                                imgDialog.cropBitmap(FileUtil.getFile(mImageView.getBitmap(), System.currentTimeMillis() + ".jpg"));
                            }
                        } else if (!TextUtils.isEmpty(objectBean.getObjectUrl())) {
                            //图片有地址 直接传
                            imgDialog.cropBitmap(objectBean.getObjectFiles());
                        }

                    }
                };
                break;
            case R.id.add_goods_sort_tv:
                SelectSortActivity.start(mContext, objectBean);
                break;
            case R.id.image_btn:
                if (popup == null) {
                    popup = new PopupEditMenuGoods(mContext);
                    popup.setBackground(Color.TRANSPARENT);
//                    popup.setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE, Gravity.END | Gravity.BOTTOM);
                    popup.setPopupClickListener(new PopupEditMenuGoods.EditMenuPopupClickListener() {
                        @Override
                        public void onEditGoodsLocationClick() {
                            if (locationLayout.getVisibility() == View.VISIBLE) {
                                clearGoodsLocation();
                            } else {
                                if (StringUtils.getGoodsLoction(objectBean).equals("未归位")) {
                                    selectLocation();
                                }
                            }
                        }

                        @Override
                        public void onAddRemingClick() {
                            AddRemindActivity.start(mContext, objectBean);
                        }

                        @Override
                        public void onDeleteGoodsClick() {
                            getPresenter().delGoods(App.getUser(mContext).getId(), objectBean.get_id());
                        }

                        @Override
                        public void onLockClick() {
                            getPresenter().goodsArchive(App.getUser(mContext).getId(), objectBean.get_id());
                        }
                    });
                }
                popup.showPopupWindow(imageBtn);
                break;
            case R.id.goods_details_location_tv:
                if (locationTv.getText().toString().equals("未归位")) return;
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
                break;
            case R.id.remind_item_layout:
                Intent intent = new Intent(mContext, AddRemindActivity.class);
                intent.putExtra("remind_bean", remindBean);
                startActivity(intent);
                break;
            case R.id.goods_info_empty_view:
                isEditGoodsInfo = true;
                goodsInfoEmptyView.setVisibility(View.GONE);
                goodsInfoEditTypeView.setText(isEditGoodsInfo ? R.string.stop_text : R.string.edit_text);
                goodsInfoEditView.setVisibility(isEditGoodsInfo ? View.VISIBLE : View.GONE);
                goodsInfoListView.setVisibility(isEditGoodsInfo ? View.GONE : View.VISIBLE);
                break;
            case R.id.goods_info_edit_tv:
                isEditGoodsInfo = !isEditGoodsInfo;
                goodsInfoEditTypeView.setText(isEditGoodsInfo ? R.string.stop_text : R.string.edit_text);
                goodsInfoEditView.setVisibility(isEditGoodsInfo ? View.VISIBLE : View.GONE);
                goodsInfoListView.setVisibility(isEditGoodsInfo ? View.GONE : View.VISIBLE);
                if (isEditGoodsInfo) {
                    goodsInfoEditView.init(objectBean);
                } else {
                    objectBean = goodsInfoEditView.getGoodsInfoBean();
                    initInfoData();
                }
                break;
        }
    }

    private void setTitleViewState(boolean isEdit) {
        imageBtn.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        backBtn.setVisibility(isEdit ? View.GONE : View.VISIBLE);
        mSaveView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
        mCanleView.setVisibility(isEdit ? View.VISIBLE : View.GONE);
    }

    private void clearGoodsLocation() {
        DialogUtil.show("提示", "将原有位置清空？", "确定", "取消", this, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getPresenter().removeObjectFromFurnitrue(App.getUser(mContext).getId(), objectBean.get_id());
            }
        }, null);
    }

    public static void start(Context mContext, ObjectBean objectBean) {
        Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
        intent.putExtra("objectBean", objectBean);
        mContext.startActivity(intent);
    }

    @Override
    public void goodsArchiveSuccess(RequestSuccessBean bean) {
        if (bean.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "封存成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void editGoodsSuccess(ObjectBean data) {
        if (data != null) {
            Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
            objectBean = data;
            oldBean = (ObjectBean) objectBean.clone();
            isEditGoodsInfo = false;
            setTitleViewState(false);
            initData();
            if (objectBean != null) {
                getPresenter().getGoodsRemindsById(App.getUser(mContext).getId(), objectBean.get_id());
            }
        }
    }

    @Override
    public void removeObjectFromFurnitrueSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            selectLocation();
        }
    }

    private void selectLocation() {
        if (MainActivity.moveGoodsList == null) {
            MainActivity.moveGoodsList = new ArrayList<>();
        }
        MainActivity.moveGoodsList.clear();
        MainActivity.moveGoodsList.add(objectBean);
        EventBus.getDefault().post(new EventBusMsg.SelectHomeTab());
        EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
        finish();
    }

    @Override
    public void delGoodsSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "删除成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void getGoodsRemindsByIdSuccess(List<RemindBean> data) {
        if (data != null && data.size() > 0) {
            remindLayout.setVisibility(View.VISIBLE);
            remindBean = data.get(AppConstant.DEFAULT_INDEX_OF);
            //标题
            remindNameTv.setText(remindBean.getTitle());
            //时间
            if (remindBean.getTips_time() != 0) {
                remindTimeTv.setText(DateUtil.dateToString(remindBean.getTips_time(), DateUtil.DatePattern.ONLY_MINUTE));
            }
        } else {
            remindLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void getBelongerListSuccess(List<BaseBean> data) {
        mOneLists.clear();
        mOneLists.addAll(data);
        SortBelongerDialog belongerDialog = new SortBelongerDialog(mContext) {
            @Override
            public void addSortChildClick() {
                if (App.getUser(mContext).isIs_vip()) {
                    SelectSortChildNewActivity.start(mContext, objectBean, false, true);
                } else {
                    BuyVIPActivity.start(mContext);
                }
            }

            @Override
            public void submitClick(int currentIndex) {
                objectBean.setBelonger(mOneLists.get(currentIndex).getName());
                setTitleViewState(getPresenter().goodsInfosIsEdit(objectBean, oldBean));
                goodsInfoEditView.init(objectBean);
            }
        };
        belongerDialog.initData(mOneLists);
        belongerDialog.setTitle(R.string.add_belonger_tv);
    }

    @Override
    public void getBuyFirstCategoryListSuccess(List<BaseBean> data) {
        initOneView(data);
    }

    @Override
    public void getSubCategoryListSuccess(List<BaseBean> data) {
        initOneView(data);
    }

    @Override
    public void getTwoSubCategoryListSuccess(List<BaseBean> data) {
        mTwoLists.clear();
        mTwoLists.addAll(data);
        if (sortChildDialog.isShow()) {
            sortChildDialog.initTwoData(mTwoLists);
        }
        if (!initTwo && mTwoLists.size() > 0) {
            getPresenter().getThreeSubCategoryList(App.getUser(mContext).getId(), mTwoLists.get(AppConstant.DEFAULT_INDEX_OF).getCode(), type);
            initTwo = true;
        } else {
            mThreeLists.clear();
            if (sortChildDialog.isShow()) {
                sortChildDialog.initThreeData(mThreeLists);
            }
        }
    }

    @Override
    public void getThreeSubCategoryListSuccess(List<BaseBean> data) {
        mThreeLists.clear();
        mThreeLists.addAll(data);
        if (sortChildDialog.isShow()) {
            sortChildDialog.initThreeData(mThreeLists);
        }
    }

    @Override
    public void onUpLoadSuccess(String path) {
        objectBean.setObject_url(path);
        addOrEditGoods();
    }

    private void initOneView(List<BaseBean> data) {
        mOneLists.clear();
        mOneLists.addAll(data);
        sortChildDialog = new SortChildDialog(mContext) {
            @Override
            public void updateTwoData(int currentItem) {
                if (mOneLists.size() > 0 && mOneLists.size() > currentItem) {
                    initTwo = false;
                    getPresenter().getTwoSubCategoryList(App.getUser(mContext).getId(), mOneLists.get(currentItem).getCode(), type);
                }
            }

            @Override
            public void updateThreeData(int currentItem) {
                if (mTwoLists.size() > 0 && mTwoLists.size() > currentItem) {
                    getPresenter().getThreeSubCategoryList(App.getUser(mContext).getId(), mTwoLists.get(currentItem).getCode(), type);
                }
            }

            @Override
            public void addSortChildClick() {
                if (App.getUser(mContext).isIs_vip()) {
                    SelectSortChildNewActivity.start(mContext, objectBean, TextUtils.isEmpty(type), false);
                } else {
                    BuyVIPActivity.start(mContext);
                }
            }

            @Override
            public void submitClick(int oneCurrentIndex, int twoCurrentIndex, int threeCurrentIndex) {
                List<BaseBean> beanList = new ArrayList<>();
                if (!AppConstant.BUY_TYPE.equals(type)) {
                    beanList.add(objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF));
                }
                if (mOneLists.size() > 0) {
                    beanList.add(mOneLists.get(oneCurrentIndex));
                }
                if (mTwoLists.size() > 0) {
                    beanList.add(mTwoLists.get(twoCurrentIndex));
                }
                if (mThreeLists.size() > 0) {
                    beanList.add(mThreeLists.get(threeCurrentIndex));
                }
                if (!AppConstant.BUY_TYPE.equals(type)) {
                    objectBean.setCategories(beanList);
                } else {
                    List<String> channels = new ArrayList<>();
                    for (BaseBean baseBean : beanList) {
                        channels.add(baseBean.getName());
                    }
                    objectBean.setChannel(channels);
                }
                setTitleViewState(getPresenter().goodsInfosIsEdit(objectBean, oldBean));
                goodsInfoEditView.init(objectBean);
            }
        };
        sortChildDialog.initData(mOneLists);
        sortChildDialog.setType(type);
        if (!initOne && mOneLists.size() > 0) {
            getPresenter().getTwoSubCategoryList(App.getUser(mContext).getId(), mOneLists.get(AppConstant.DEFAULT_INDEX_OF).getCode(), type);
            initOne = true;
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
    protected GoodsDetailsPresenter createPresenter() {
        return GoodsDetailsPresenter.getInstance();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode && resultCode == RESULT_OK) {
            ObjectBean newBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (newBean != null && !TextUtils.isEmpty(newBean.getExpire_date()) && !newBean.getExpire_date().equals(objectBean.getExpire_date())) {
                StringUtils.showMessage(mContext, R.string.add_end_time_hint_text);
            }
            if (newBean != null) {
                objectBean = newBean;
                setTitleViewState(getPresenter().goodsInfosIsEdit(newBean, oldBean));
                initData();
            }
        } else if (AppConstant.REQUEST_CODE == requestCode && resultCode == AddGoodsActivity.RESULT_FINISH) {
            finish();
        }
        if (AppConstant.START_GOODS_INFO_CODE == requestCode && RESULT_OK == resultCode) {
            ObjectBean newBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (newBean != null) {
                objectBean = newBean;
                setTitleViewState(getPresenter().goodsInfosIsEdit(newBean, oldBean));
                initData();
            }
        }
        if (AppConstant.START_GOODS_REMARKS_CODE == requestCode && RESULT_OK == resultCode) {
            String remarks = data.getStringExtra("remind_remarks");
            objectBean.setDetail(remarks);
            setTitleViewState(getPresenter().goodsInfosIsEdit(objectBean, oldBean));
            initData();
        }
        if (imgDialog != null) {
            imgDialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void onItemSortClick(BaseBean baseBean) {
        type = "";
        initOne = false;
        getPresenter().getSubCategoryList(App.getUser(mContext).getId(), baseBean.getCode(), type);
    }

    @Override
    public void onItemBuyClick() {
        type = AppConstant.BUY_TYPE;
        initOne = false;
        getPresenter().getBuyFirstCategoryList(App.getUser(mContext).getId());
    }

    @Override
    public void onItemBelongerClick() {
        getPresenter().getBelongerList(App.getUser(mContext).getId());
    }

    @Override
    protected void onDestroy() {
        if (mImageView != null) {
            mImageView.clear();
        }
        super.onDestroy();
    }
}
