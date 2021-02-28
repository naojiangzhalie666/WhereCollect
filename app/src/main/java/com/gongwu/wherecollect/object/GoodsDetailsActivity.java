package com.gongwu.wherecollect.object;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.AddRemindActivity;
import com.gongwu.wherecollect.activity.FurnitureLookActivity;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IGoodsDetailsContract;
import com.gongwu.wherecollect.contract.presenter.GoodsDetailsPresenter;
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
import com.gongwu.wherecollect.util.PhotosDialog;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.ObjectInfoLookView;
import com.gongwu.wherecollect.view.ObjectsLookMenuDialog;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsDetailsActivity extends BaseMvpActivity<GoodsDetailsActivity, GoodsDetailsPresenter> implements IGoodsDetailsContract.IGoodsDetailsView {
    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.goods_name_tv)
    TextView goodsNameTv;
    @BindView(R.id.add_goods_sort_tv)
    TextView sortTv;
    @BindView(R.id.add_img_view)
    GoodsImageView mImageView;
    @BindView(R.id.goodsInfo_view)
    ObjectInfoLookView goodsInfoView;
    @BindView(R.id.image_btn)
    ImageButton imageBtn;
    @BindView(R.id.goods_details_location_tv)
    TextView locationTv;
    @BindView(R.id.remind_layout)
    View remindLayout;
    @BindView(R.id.remind_name_tv)
    TextView remindNameTv;
    @BindView(R.id.remind_time_tv)
    TextView remindTimeTv;

    private ObjectBean objectBean;
    private RemindBean remindBean;
    private Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initViews() {
        mTitleView.setText(R.string.goods_text);
        imageBtn.setVisibility(View.VISIBLE);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        initData();
    }

    private void initData() {
        if (objectBean != null) {
            goodsInfoView.init(objectBean);
            if (!TextUtils.isEmpty(objectBean.getNameText())) {
                goodsNameTv.setText(objectBean.getNameText());
                goodsNameTv.setTextColor(getResources().getColor(R.color.color333));
            }
            if (objectBean.getCategories() != null && objectBean.getCategories().size() > 0) {
                sortTv.setText(objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getName());
                sortTv.setTextColor(getResources().getColor(R.color.color333));
            }
            if (objectBean.getObject_url().contains("#")) {
                int resId = Color.parseColor(objectBean.getObject_url());
                mImageView.setResourceColor(objectBean.getName(), resId, 10);
            } else {
                mImageView.setImg(objectBean.getObject_url(), 10);
            }
            goodsInfoView.showGoodsLayout();
            locationTv.setText(StringUtils.getGoodsLoction(objectBean));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (objectBean != null) {
            getPresenter().getGoodsRemindsById(App.getUser(mContext).getId(), objectBean.get_id());
        }
    }

    @OnClick({R.id.back_btn, R.id.add_img_view, R.id.image_btn, R.id.goods_details_location_tv, R.id.remind_item_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_img_view:
                if (objectBean.getObject_url().contains("#")) return;
                List<ImageData> imageDatas = new ArrayList<>();
                ImageData imageData = new ImageData();
                imageData.setUrl(objectBean.getObject_url());
                imageDatas.add(imageData);
                PhotosDialog photosDialog = new PhotosDialog(this, false, false, imageDatas);
                photosDialog.showPhotos(0);
                break;
            case R.id.image_btn:
                ObjectsLookMenuDialog dialog = new ObjectsLookMenuDialog(this, objectBean) {
                    @Override
                    public void editLocation() {
                        if (objectBean.getLocations() == null || objectBean.getLocations().size() == 0) {
                            MainActivity.moveGoodsList = new ArrayList<>();
                            MainActivity.moveGoodsList.add(objectBean);
                            EventBus.getDefault().post(new EventBusMsg.SelectHomeTab());
                            finish();
                        } else {
                            clearGoodsLocation();
                        }
                    }

                    @Override
                    public void editGoods() {
                        Intent intent = new Intent(mContext, AddGoodsActivity.class);
                        intent.putExtra("objectBean", objectBean);
                        if (remindBean != null) {
                            intent.putExtra("remindBean", remindBean);
                        }
                        startActivityForResult(intent, AppConstant.REQUEST_CODE);
                    }

                    @Override
                    public void deleteGoods() {
                        getPresenter().delGoods(App.getUser(mContext).getId(), objectBean.get_id());
                    }
                };
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
        }
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
    public void removeObjectFromFurnitrueSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            MainActivity.moveGoodsList = new ArrayList<>();
            MainActivity.moveGoodsList.add(objectBean);
            EventBus.getDefault().post(new EventBusMsg.SelectHomeTab());
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            finish();
        }
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
            objectBean = (ObjectBean) data.getSerializableExtra("objectBean");
            initData();
        } else if (AppConstant.REQUEST_CODE == requestCode && resultCode == AddGoodsActivity.RESULT_FINISH) {
            finish();
        }
    }
}
