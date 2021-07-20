package com.gongwu.wherecollect.object;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.adapter.AddMoreGoodsAdapter;
import com.gongwu.wherecollect.adapter.GoodsInfoViewAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.adapter.StackAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddGoodsContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPresenter;
import com.gongwu.wherecollect.net.entity.GoodsInfoBean;
import com.gongwu.wherecollect.net.entity.response.BarcodeResultBean;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.swipecardview.SwipeFlingAdapterView;
import com.gongwu.wherecollect.util.AesUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.AddGoodsDialog;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.ObjectInfoLookView;
import com.zsitech.oncon.barcode.core.CaptureActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddMoreGoodsActivity extends BaseMvpActivity<AddGoodsActivity, AddGoodsPresenter> implements IAddGoodsContract.IAddGoodsView, MyOnItemClickListener, SwipeFlingAdapterView.onFlingListener {

    private final int BOOK_CODE = 0x132;
    private static final int spanCount = 3;
    public static final int ADD_GOODS_CODE = -0x125;

    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.add_more_goods_list_view)
    RecyclerView mlistView;
    @BindView(R.id.commit_bt)
    Button mCommitBt;
    @BindView(R.id.select_location_bt)
    Button mSelectLocationBt;
    @BindView(R.id.add_goods_list_sort)
    TextView sortNameTv;
    @BindView(R.id.goods_info_view)
    RecyclerView goodsInfoListView;
    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;
    @BindView(R.id.more_goods_info_empty_view)
    View addInfoView;
    @BindView(R.id.goods_info_edit_tv)
    TextView editInfoTv;
    @BindView(R.id.title_commit_bg_main_color_tv)
    TextView titleCommitBt;
    @BindView(R.id.bottom_commit_layout)
    View bottomCommitBt;

    private SwipeFlingAdapterView mSwipeView;
    private StackAdapter mStackAdapter;
    private File imgOldFile;
    private List<ObjectBean> selectImgs = new ArrayList<>();

    private AddMoreGoodsAdapter mAdapter;
    private AddGoodsDialog mDialog;
    //需要上传图片的bean类
    private ObjectBean uploadBean;
    //属性bean
    private ObjectBean sortBean;
    private List<ObjectBean> mlist;
    private Loading loading;
    private boolean setGoodsLocation;

    private GoodsInfoViewAdapter mInfoAdapter;
    private List<GoodsInfoBean> mGoodsInfos = new ArrayList<>();
    private RoomFurnitureBean location;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_more_goods;
    }

    @Override
    protected void initViews() {
        mTitleView.setText(R.string.add_more_text);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        mlist = new ArrayList<>();
        initData();
        mlistView.setLayoutManager(new GridLayoutManager(mContext, spanCount, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AddMoreGoodsAdapter(mContext, mlist);
        mlistView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        //属性bean
        sortBean = new ObjectBean();
        location = (RoomFurnitureBean) getIntent().getSerializableExtra("locationCode");
        bottomCommitBt.setVisibility(location != null ? View.GONE : View.VISIBLE);
        titleCommitBt.setVisibility(location != null ? View.VISIBLE : View.GONE);
        //添加goods的item
        ObjectBean bean = new ObjectBean();
        bean.set__v(ADD_GOODS_CODE);
        mlist.add(bean);
        mInfoAdapter = new GoodsInfoViewAdapter(mContext, mGoodsInfos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 3) {
            @Override
            public boolean canScrollVertically() {
                //禁止上下滑动
                return false;
            }
        };
        goodsInfoListView.setLayoutManager(gridLayoutManager);
        goodsInfoListView.setAdapter(mInfoAdapter);

        List<String> list = getIntent().getStringArrayListExtra("imgList");
        if (list != null && list.size() > 0) {
            // 为AdapterViewFlipper设置Adapter
            initSwipeViewAdapter();
            initSwipeView();
            upLoadSelectImgs(list);
        } else {
            startDialog(null, true);
        }
    }

    private void upLoadSelectImgs(List<String> list) {
        for (String s : list) {
            ObjectBean objectBean = new ObjectBean();
            objectBean.setObject_url(s);
            selectImgs.add(objectBean);
        }
        mStackAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.back_btn, R.id.add_goods_list_sort, R.id.more_goods_info_empty_view, R.id.commit_bt,
            R.id.select_location_bt, R.id.goods_info_edit_tv, R.id.title_commit_bg_main_color_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_goods_list_sort:
                SelectSortActivity.start(mContext, sortBean);
                break;
            case R.id.goods_info_edit_tv:
            case R.id.more_goods_info_empty_view:
                AddGoodsPropertyActivity.start(mContext, sortBean, true);
                break;
            case R.id.commit_bt:
            case R.id.title_commit_bg_main_color_tv:
                getPresenter().addMoreGoods(mContext, mlist, sortBean, location);
                break;
            case R.id.select_location_bt:
                setGoodsLocation = true;
                getPresenter().addMoreGoods(mContext, mlist, sortBean, location);
                break;
        }
    }

    @Override
    public void onItemClick(int positions, View view) {
        if (mlist.size() >= 10) {
            ToastUtil.show(mContext, "一次最多添加9个物品", Toast.LENGTH_SHORT);
            return;
        }
        ObjectBean objectBean = mlist.get(positions);
        startDialog(ADD_GOODS_CODE == objectBean.get__v() ? null : objectBean, false);
    }

    /**
     * 添加物品的dialog
     */
    private void startDialog(ObjectBean objectBean, boolean isShowImgDailog) {
        if (objectBean != null) {
            objectBean.setSelect(true);
        }
        //添加
        mDialog = new AddGoodsDialog(this, mlist.size()) {
            @Override
            public void result(ObjectBean bean) {
                //上传
                if (!TextUtils.isEmpty(bean.getObject_url()) && !bean.getObject_url().contains("http")
                        && !bean.getObject_url().contains("7xroa4") && !bean.getObject_url().contains("#")
                        && !bean.getObject_url().contains("cdn.shouner.com/object/image")) {
                    uploadBean = bean;
                    getPresenter().uploadImg(mContext, new File(bean.getObject_url()));
                    return;
                }
                if (!bean.isSelect()) {
                    bean.setSelect(false);
                    mlist.add(AppConstant.DEFAULT_INDEX_OF, bean);
                }
                if (sortBean != null && (sortBean.getCategories() == null || sortBean.getCategories().size() == 0)
                        && bean.getCategories() != null && bean.getCategories().size() > 0) {
                    if ("图书".equals(bean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getName())) {
                        sortBean.setCategories(bean.getCategories());
                        sortNameTv.setText(sortBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getName());
                        initInfoData();
                    }
                }
                mAdapter.notifyDataSetChanged();
                setCommitBtnEnable(mlist.size() > 1);
            }

            @Override
            public void scanCode(String code, String barcodeType) {
                getPresenter().getGoodsByBarcode(App.getUser(mContext).getId(), AesUtil.AES256Encode(mContext, App.getUser(mContext).getId(), code), barcodeType);
            }
        };
        mDialog.show();
        mDialog.setObjectBean(objectBean);
        if (isShowImgDailog) {
            mDialog.showSelectDialog();
        }
    }

    private void initSwipeView() {
        if (mSwipeView != null) {
            contentLayout.removeView(mSwipeView);
        }
        mSwipeView = new SwipeFlingAdapterView(mContext);
        mSwipeView.setMaxVisible(4);
        mSwipeView.setMinStackInAdapter(4);
        contentLayout.addView(mSwipeView);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSwipeView.getLayoutParams();
        int topDip = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        lp.setMargins(0, topDip, 0, 0);
        lp.height = 1600;
        mSwipeView.setLayoutParams(lp);
        mSwipeView.setAdapter(mStackAdapter);
        mSwipeView.setIsNeedSwipe(true);
        mSwipeView.setFlingListener(this);
    }

    private void initSwipeViewAdapter() {
        //批量添加,外面传进来图片
        mStackAdapter = new StackAdapter(mContext, selectImgs) {
            @Override
            public void selectItem(boolean select, String name, String url) {
                //点击确定设置缓解文件为null
                imgOldFile = null;
                if (select) {
                    ObjectBean objectBean = new ObjectBean();
                    objectBean.setObject_url(url);
                    objectBean.setName(name);
                    uploadBean = objectBean;
                    getPresenter().uploadImg(mContext, new File(objectBean.getObject_url()));
                }
                mSwipeView.swipeRight();
            }

            @Override
            public void onClickCamera() {
//                qrBook();
            }

            @Override
            public void selectImage(int position) {
                //编辑图片判断文件是否为null
                if (imgOldFile == null) {
                    imgOldFile = new File(selectImgs.get(position).getObject_url());
                }
                showSelectDialog();
            }
        };
    }

    /**
     * 图片选择
     */
    private SelectImgDialog selectImgDialog;

    private void showSelectDialog() {
        selectImgDialog = new SelectImgDialog(this, null, 1, imgOldFile) {
            @Override
            public void getResult(List<File> list) {
                super.getResult(list);
                //相册获取的图片
                imgOldFile = list.get(0);
                selectImgDialog.cropBitmap(imgOldFile);
            }

            @Override
            protected void resultFile(File file) {
                super.resultFile(file);
                //剪切后的图片
                selectImgs.get(0).setObject_url(file.getAbsolutePath());
                initSwipeView();
            }
        };
        selectImgDialog.hintLayout();
        //编辑选择是否隐藏的 根据imgOldFile来判断
        selectImgDialog.showEditIV(imgOldFile == null ? View.GONE : View.VISIBLE);
    }

    @Override
    protected AddGoodsPresenter createPresenter() {
        return AddGoodsPresenter.getInstance();
    }

    @Override
    public void editGoodsSuccess(ObjectBean data) {

    }

    @Override
    public void addObjectsSuccess(List<ObjectBean> data) {

    }

    @Override
    public void addMoreGoodsSuccess(List<ObjectBean> data) {
        if (setGoodsLocation) {
            MainActivity.moveGoodsList = new ArrayList<>();
            MainActivity.moveGoodsList.addAll(data);
            EventBus.getDefault().post(new EventBusMsg.SelectHomeTab());
            setGoodsLocation = false;
        }
        if (location != null) {
            EventBus.getDefault().post(new EventBusMsg.RefreshFurnitureLook());
            EventBus.getDefault().post(new EventBusMsg.RefreshRoomsFragment());
        }
        EventBus.getDefault().post(new EventBusMsg.FinishActivity());
        finish();
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
    public void getGoodsByBarcodeSuccess(BarcodeResultBean data) {
        App.getUser(mContext).setEnergy_value(App.getUser(mContext).getEnergy_value() - 1);
        refreshUIByBarcode(data);
    }

    @Override
    public void getGoodsByTBbarcodeSuccess(BarcodeResultBean data) {
        App.getUser(mContext).setEnergy_value(App.getUser(mContext).getEnergy_value() - 1);
        refreshUIByBarcode(data);
    }

    private void refreshUIByBarcode(BarcodeResultBean data) {
        if (mDialog.getGoodsBean() != null) {
            getPresenter().initBarCodeData(mContext, mDialog.getGoodsBean(), data);
            mDialog.setObjectBean(mDialog.getGoodsBean());
        }
    }

    @Override
    public void onUpLoadSuccess(String path) {
        uploadBean.setObject_url(path);
        if (!uploadBean.isSelect()) {
            uploadBean.setSelect(false);
            mlist.add(AppConstant.DEFAULT_INDEX_OF, uploadBean);
        }
        uploadBean = null;
        mAdapter.notifyDataSetChanged();
        setCommitBtnEnable(mlist.size() > 1);
    }


    private void setCommitBtnEnable(boolean btnEnable) {
        mCommitBt.setEnabled(btnEnable);
        mSelectLocationBt.setEnabled(btnEnable);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mDialog != null) {
            mDialog.onActivityResult(requestCode, resultCode, data);
        }
        if (selectImgDialog != null) {
            selectImgDialog.onActivityResult(requestCode, resultCode, data);
        }
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            sortBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (sortBean.getCategories() != null && sortBean.getCategories().size() > 0) {
                sortNameTv.setText(sortBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getName());
            } else {
                sortNameTv.setText(R.string.add_goods_sort);
            }
            initInfoData();
//            goodsInfoView.init(sortBean);
        }
        if (requestCode == BOOK_CODE && resultCode == CaptureActivity.result) {//扫描的到结果
            String code = data.getStringExtra("result");
            if (code.contains("http")) {
                //网络商城
                getPresenter().getTaobaoInfo(App.getUser(mContext).getId(), code);
            } else {
                //书本
                getPresenter().getBookInfo(App.getUser(mContext).getId(), code);
            }
        }
    }

    private void initInfoData() {
        mGoodsInfos.clear();
        mGoodsInfos.addAll(StringUtils.getGoodsInfos(sortBean));
        goodsInfoListView.setVisibility(mGoodsInfos.size() > 0 ? View.VISIBLE : View.GONE);
        mInfoAdapter.notifyDataSetChanged();
        addInfoView.setVisibility(mGoodsInfos.size() > 0 ? View.GONE : View.VISIBLE);
        editInfoTv.setVisibility(mGoodsInfos.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(loading, mContext);
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


    public static void start(Context context) {
        Intent intent = new Intent(context, AddMoreGoodsActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, RoomFurnitureBean roomBean) {
        Intent intent = new Intent(context, AddMoreGoodsActivity.class);
        if (roomBean != null) {
            intent.putExtra("locationCode", roomBean);
        }
        context.startActivity(intent);
    }

    public static void start(Context context, ArrayList<String> lists, RoomFurnitureBean locationCode) {
        Intent intent = new Intent(context, AddMoreGoodsActivity.class);
        if (lists != null) {
            intent.putStringArrayListExtra("imgList", lists);
        }
        if (locationCode != null) {
            intent.putExtra("locationCode", locationCode);
        }
        context.startActivity(intent);
    }

    @Override
    public void removeFirstObjectInAdapter() {

    }

    @Override
    public void onLeftCardExit(Object dataObject) {

    }

    @Override
    public void onRightCardExit(Object dataObject) {
        mStackAdapter.remove(0);
        if (selectImgs.size() == 0) {
            mSwipeView.setVisibility(View.GONE);
            selectImgDialog = null;
        }
    }

    @Override
    public void onAdapterAboutToEmpty(int itemsInAdapter) {

    }

    @Override
    public void onScroll(float progress, float scrollXProgress) {

    }

    String ISBN = "";

    @Override
    public void updateBeanWithBook(BookBean book) {
        if (book == null || book.getImageFile() == null) {
            return;
        }
        ISBN = book.getIsbnCode();
        ObjectBean tempBean = new ObjectBean();
        if (book.getImageFile() != null) {
            tempBean.setObject_url(book.getImageFile().getAbsolutePath());
        }
        if (!TextUtils.isEmpty(book.getTitle())) {
            tempBean.setName(book.getTitle());
        }
        if (!TextUtils.isEmpty(book.getSummary())) {
            tempBean.setDetail(book.getSummary());
        }
        if (!TextUtils.isEmpty(book.getPrice())) {
            tempBean.setPrice(book.getPrice());
        }
        if (book.getCategory() != null) {
            List<BaseBean> temp = new ArrayList<>();
            temp.add(book.getCategory());
            tempBean.setCategories(temp);
        }
        if (mDialog != null) {
            mDialog.setObjectBean(tempBean);
            mDialog.show();
        }
    }

    @Override
    public void removeObjectFromFurnitrueSuccess(RequestSuccessBean data) {

    }

    @Override
    public void getBelongerListSuccess(List<BaseBean> data) {
        //无用
    }

    @Override
    public void getBuyFirstCategoryListSuccess(List<BaseBean> data) {
        //无用
    }

    @Override
    public void getSubCategoryListSuccess(List<BaseBean> data) {
        //无用
    }

    @Override
    public void getTwoSubCategoryListSuccess(List<BaseBean> data) {
        //无用
    }

    @Override
    public void getThreeSubCategoryListSuccess(List<BaseBean> data) {
        //无用
    }
}
