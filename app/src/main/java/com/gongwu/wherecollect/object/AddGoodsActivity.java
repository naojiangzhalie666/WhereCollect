package com.gongwu.wherecollect.object;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.activity.CameraMainActivity;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.adapter.GoodsInfoViewAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddGoodsContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPresenter;
import com.gongwu.wherecollect.net.entity.GoodsInfoBean;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.util.DateUtil;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.ObjectInfoEditView;
import com.gongwu.wherecollect.view.PopupAddGoods;
import com.gongwu.wherecollect.view.EditTextWatcher;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.ObjectInfoLookView;
import com.gongwu.wherecollect.view.SortBelongerDialog;
import com.gongwu.wherecollect.view.SortChildDialog;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import razerdp.basepopup.BasePopupWindow;

/**
 * 添加物品界面
 */
public class AddGoodsActivity extends BaseMvpActivity<AddGoodsActivity, AddGoodsPresenter> implements IAddGoodsContract.IAddGoodsView, PopupAddGoods.AddGoodsPopupClickListener, ObjectInfoEditView.OnItemClickListener {
    private static final String TAG = AddGoodsActivity.class.getSimpleName();
    public static final int BOOK_CODE = 0x112;
    public static final int RESULT_FINISH = 0x193;
    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.add_img_view)
    GoodsImageView mImageView;
    //GoodsImageView控件head、name
    @BindView(R.id.head)
    ImageView head;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.add_goods_sort_tv)
    TextView sortNameTv;
    @BindView(R.id.goods_name_et)
    EditText mEditText;
    @BindView(R.id.commit_bt)
    Button mCommitBt;
    @BindView(R.id.select_location_bt)
    Button mSelectLocationBt;
    @BindView(R.id.goods_location_layout)
    View locationView;
    @BindView(R.id.goods_location_tv)
    TextView locationTv;
    @BindView(R.id.remind_layout)
    View remindLayout;
    @BindView(R.id.remind_name_tv)
    TextView remindNameTv;
    @BindView(R.id.remind_time_tv)
    TextView remindTimeTv;
    @BindView(R.id.commit_layout)
    LinearLayout commitLayout;
    @BindView(R.id.title_commit_bg_main_color_tv)
    TextView editInfoCommitTv;
    @BindView(R.id.add_goods_info_view)
    ObjectInfoEditView addGoodsInfotView;

    private Loading loading;
    private File imgFile;
    private File imgOldFile;

    private ObjectBean objectBean;
    private PopupAddGoods popup;
    private boolean setGoodsLocation;
    private RoomFurnitureBean location;
    private SortChildDialog sortChildDialog;
    private List<BaseBean> mOneLists = new ArrayList<>();
    private List<BaseBean> mTwoLists = new ArrayList<>();
    private List<BaseBean> mThreeLists = new ArrayList<>();
    private boolean initOne, initTwo;
    private String type = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_goods;
    }

    @Override
    protected void initViews() {
        initEvent();
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mTitleView.setText(R.string.add_goods_text);
        head.setImageDrawable(getResources().getDrawable(R.drawable.icon_add_goods));
        initData();
    }

    @Override
    protected AddGoodsPresenter createPresenter() {
        return AddGoodsPresenter.getInstance();
    }

    /**
     * 初始化数据，判断是否是直接添加物品，还是编辑物品
     */
    private void initData() {
        //初始化物品实体类
        objectBean = new ObjectBean();
        //添加物品的时候，拍照获取照片
        String path = getIntent().getStringExtra("filePath");
        if (!TextUtils.isEmpty(path) && StringUtils.fileIsExists(path)) {
            imgFile = new File(path);
            imgOldFile = new File(path);
            mImageView.setHead(AppConstant.IMG_COLOR_CODE, "", imgFile.getAbsolutePath());
            objectBean.setObject_url(imgFile.getAbsolutePath());
            setCommitBtnEnable(true);
        }
        String code = getIntent().getStringExtra("saomaResult");
        if (!TextUtils.isEmpty(code)) {
            if (code.contains("http")) {
                //网络商城
                getPresenter().getTaobaoInfo(App.getUser(mContext).getId(), code);
            } else {
                //书本
                getPresenter().getBookInfo(App.getUser(mContext).getId(), code);
            }
        }
        location = (RoomFurnitureBean) getIntent().getSerializableExtra("locationCode");
        if (location != null) {
            commitLayout.setVisibility(View.GONE);
            editInfoCommitTv.setVisibility(View.VISIBLE);
            locationView.setVisibility(View.VISIBLE);
            locationTv.setText(StringUtils.getGoodsLoction(location));
        }
        addGoodsInfotView.setViewBackground(R.drawable.shape_white_r10dp);
        addGoodsInfotView.init(objectBean);
        addGoodsInfotView.setOnItemClickListener(this);
    }

    /**
     * 控件监听
     */
    private void initEvent() {
        mEditText.addTextChangedListener(new EditTextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                super.onTextChanged(s, start, before, count);
                if (mEditText.getText().toString().trim().length() > 0) {
                    setCommitBtnEnable(true);
                } else {
                    setCommitBtnEnable(false);
                }
            }
        });
    }

    @OnClick({R.id.back_btn, R.id.add_img_view, R.id.add_goods_sort_tv, R.id.goods_location_tv, R.id.commit_bt,
            R.id.title_commit_bg_main_color_tv, R.id.select_location_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_img_view:
                if (popup == null) {
                    popup = new PopupAddGoods(mContext);
                    popup.setBackground(Color.TRANSPARENT);
                    popup.setPopupGravity(BasePopupWindow.GravityMode.ALIGN_TO_ANCHOR_SIDE, Gravity.LEFT | Gravity.TOP);
                    popup.setPopupClickListener(this);
                }
                popup.showPopupWindow(mImageView);
                break;
            case R.id.add_goods_sort_tv:
                SelectSortActivity.start(mContext, objectBean);
                break;
            case R.id.commit_bt:
            case R.id.title_commit_bg_main_color_tv:
                onClickCommit();
                break;
            case R.id.select_location_bt:
                setGoodsLocation = true;
                onClickCommit();
                break;
            case R.id.goods_location_tv:
                if (location != null) return;
                editLocation();
                break;
        }
    }

    private void editLocation() {
        if (locationTv.getText().toString().equals("未归位")) {
            MainActivity.moveGoodsList = new ArrayList<>();
            MainActivity.moveGoodsList.clear();
            MainActivity.moveGoodsList.add(objectBean);
            EventBus.getDefault().post(new EventBusMsg.SelectHomeTab());
            setResult(RESULT_FINISH);
            finish();
        } else {
            DialogUtil.show("提示", "将原有位置清空？", "确定", "取消", this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    getPresenter().removeObjectFromFurnitrue(App.getUser(mContext).getId(), objectBean.get_id());
                }
            }, null);
        }
    }

    /**
     * 图书扫描后修改公共属性
     */
    String ISBN = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            objectBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (objectBean != null && objectBean.getCategories() != null && objectBean.getCategories().size() > 0) {
                sortNameTv.setText(objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getName());
                sortNameTv.setTextColor(getResources().getColor(R.color.color333));
            } else {
                sortNameTv.setText(R.string.add_goods_sort);
                sortNameTv.setTextColor(getResources().getColor(R.color.divider));
            }
            addGoodsInfotView.init(objectBean);
        } else {
            getPresenter().onActivityResult(mContext, requestCode, resultCode, data);
        }
        if (AppConstant.START_GOODS_INFO_CODE == requestCode && RESULT_OK == resultCode) {
            ObjectBean newBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (newBean != null) {
                objectBean = newBean;
                addGoodsInfotView.init(objectBean);
            }
        }
        if (AppConstant.START_GOODS_REMARKS_CODE == requestCode && RESULT_OK == resultCode) {
            String remarks = data.getStringExtra("remind_remarks");
            objectBean.setDetail(remarks);
            addGoodsInfotView.init(objectBean);
        }
    }


    /**
     * 跳转到NewObjectsAddActivity
     *
     * @param mContext
     */
    public static void start(Context mContext, String filePath, String saomaResult, RoomFurnitureBean locationCode) {
        Intent intent = new Intent(mContext, AddGoodsActivity.class);
        intent.putExtra("filePath", filePath);
        intent.putExtra("saomaResult", saomaResult);
        if (locationCode != null) {
            intent.putExtra("locationCode", locationCode);
        }
        mContext.startActivity(intent);
    }


    @Override
    public void editGoodsSuccess(ObjectBean data) {
        if (data != null) {
            Intent intent = new Intent();
            intent.putExtra("objectBean", data);
            setResult(RESULT_OK, intent);
            if (location != null) {
                EventBus.getDefault().post(new EventBusMsg.RefreshFurnitureLook());
            }
            finish();
        }
    }

    @Override
    public void addObjectsSuccess(List<ObjectBean> data) {
        if (setGoodsLocation) {
            MainActivity.moveGoodsList = new ArrayList<>();
            MainActivity.moveGoodsList.addAll(data);
            EventBusMsg.SelectHomeTab tab = new EventBusMsg.SelectHomeTab();
            if (data != null && data.size() > 0) {
                if (!TextUtils.isEmpty(data.get(AppConstant.DEFAULT_INDEX_OF).getExpire_date())) {
                    tab.isShowEndTimeHint = true;
                }
            }
            EventBus.getDefault().post(tab);
            setGoodsLocation = false;
        } else {
            if (data != null && data.size() > 0) {
                if (!TextUtils.isEmpty(data.get(AppConstant.DEFAULT_INDEX_OF).getExpire_date())) {
                    EventBusMsg.LookGoodsAct tab = new EventBusMsg.LookGoodsAct();
                    tab.isShowEndTimeHint = true;
                    EventBus.getDefault().post(tab);
                }
            }
        }
        finish();
    }

    @Override
    public void addMoreGoodsSuccess(List<ObjectBean> data) {

    }

    @Override
    public void getBookInfoSuccess(BookBean book) {
        getPresenter().downloadImg(mContext, book);
    }

    @Override
    public void getTaobaoInfoSuccess(BookBean book) {
        getPresenter().downloadImg(mContext, book);
    }


    @Override
    public void updateBeanWithBook(BookBean book) {
        if (book == null || book.getImageFile() == null) {
            return;
        }
        ISBN = book.getIsbnCode();
        if (book.getImageFile() != null) {
            imgOldFile = book.getImageFile();
            mImageView.setHead(AppConstant.IMG_COLOR_CODE, "", imgOldFile.getAbsolutePath());
            objectBean.setObject_url(imgOldFile.getAbsolutePath());
            setCommitBtnEnable(true);
        }
        if (!TextUtils.isEmpty(book.getTitle())) {
            mEditText.setText(book.getTitle());
        }
        if (!TextUtils.isEmpty(book.getSummary())) {
            objectBean.setDetail(book.getSummary());
        }
        if (!TextUtils.isEmpty(book.getPrice())) {
            objectBean.setPrice(book.getPrice());
        }
        if (book.getCategory() != null) {
            List<BaseBean> temp = new ArrayList<>();
            temp.add(book.getCategory());
            objectBean.setCategories(temp);
        }
//        goodsInfoView.setVisibility(View.VISIBLE);
//        goodsInfoView.init(objectBean);
    }

    @Override
    public void getCamareImg(File file) {
        imgOldFile = file;
    }

    @Override
    public void getSelectPhotoImg(File file) {
        imgOldFile = file;
        getPresenter().startCropBitmap(mContext, file);
    }

    @Override
    public void getCropBitmap(File file) {
        if (StringUtils.fileIsExists(file.getAbsolutePath())) {
            imgFile = file;
            mImageView.setHead(AppConstant.IMG_COLOR_CODE, "", imgFile.getAbsolutePath());
            objectBean.setObject_url(imgFile.getAbsolutePath());
            setCommitBtnEnable(true);
        }
    }

    @Override
    public void removeObjectFromFurnitrueSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            MainActivity.moveGoodsList = new ArrayList<>();
            MainActivity.moveGoodsList.clear();
            MainActivity.moveGoodsList.add(objectBean);
            EventBus.getDefault().post(new EventBusMsg.SelectHomeTab());
            EventBus.getDefault().postSticky(new EventBusMsg.RefreshFragment());
            setResult(RESULT_FINISH);
            finish();
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
                addGoodsInfotView.init(objectBean);
            }
        };
        belongerDialog.initData(mOneLists);
        belongerDialog.setTitle(R.string.add_belonger_tv);
    }

    @Override
    public void onUpLoadSuccess(String path) {
        objectBean.setObject_url(path);
        addOrEditGoods();
    }

    @Override
    public void getBuyFirstCategoryListSuccess(List<BaseBean> data) {
        initOneView(data);
    }

    @Override
    public void getSubCategoryListSuccess(List<BaseBean> data) {
        initOneView(data);
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
                addGoodsInfotView.init(objectBean);
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
        if (!TextUtils.isEmpty(getIntent().getStringExtra("saomaResult"))) {
            Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
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
        //调用接口
        if (location != null) {
            getPresenter().setLocation(objectBean, location);
            getPresenter().editGoods(mContext, objectBean, mEditText.getText().toString(), ISBN);
            return;
        }
        if (TextUtils.isEmpty(objectBean.get_id())) {
            getPresenter().addObjects(mContext, objectBean, mEditText.getText().toString(), ISBN);
        } else {
            getPresenter().editGoods(mContext, objectBean, mEditText.getText().toString(), ISBN);
        }
    }

    private void setCommitBtnEnable(boolean btnEnable) {
        mCommitBt.setEnabled(btnEnable);
        mSelectLocationBt.setEnabled(btnEnable);
    }

    @Override
    public void onCameraClick() {
        getPresenter().openCamare(mContext);
    }

    @Override
    public void onPhotoClick() {
        getPresenter().startImageGridActivity(mContext);
    }

    @Override
    public void onScanClick() {
        startActivityForResult(new Intent(mContext, CaptureActivity.class), BOOK_CODE);
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
}