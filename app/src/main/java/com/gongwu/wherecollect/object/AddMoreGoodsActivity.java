package com.gongwu.wherecollect.object;

import android.app.Activity;
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
import com.gongwu.wherecollect.adapter.AddMoreGoodsAdapter;
import com.gongwu.wherecollect.adapter.MyOnItemClickListener;
import com.gongwu.wherecollect.adapter.StackAdapter;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddGoodsContract;
import com.gongwu.wherecollect.contract.presenter.AddGoodsPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.swipecardview.SwipeFlingAdapterView;
import com.gongwu.wherecollect.util.SelectImgDialog;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.AddGoodsDialog;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.ObjectInfoLookView;
import com.zsitech.oncon.barcode.core.CaptureActivity;

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
    @BindView(R.id.goodsInfo_view)
    ObjectInfoLookView goodsInfoView;
    @BindView(R.id.content_layout)
    RelativeLayout contentLayout;

    private SwipeFlingAdapterView mSwipeView;
    private StackAdapter mStackAdapter;
    private File imgOldFile;
    private List<ObjectBean> selectImgs = new ArrayList<>();

    private AddMoreGoodsAdapter mAdapter;
    private AddGoodsDialog mDialog;
    //需要上传图片的bean类
    private ObjectBean uploadBean;
    //属性bean
    private ObjectBean objectBean;
    private List<ObjectBean> mlist;
    private Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_more_goods;
    }

    @Override
    protected void initViews() {
        mTitleView.setText(R.string.add_more_text);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));

        initData();
        mlistView.setLayoutManager(new GridLayoutManager(mContext, spanCount, LinearLayoutManager.VERTICAL, false));
        mAdapter = new AddMoreGoodsAdapter(mContext, mlist);
        mlistView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this);
    }

    private void initData() {
        mlist = new ArrayList<>();
        //属性bean
        objectBean = new ObjectBean();
        //添加goods的item
        ObjectBean bean = new ObjectBean();
        bean.set__v(ADD_GOODS_CODE);
        mlist.add(bean);

        List<String> list = getIntent().getStringArrayListExtra("imgList");
        if (list != null && list.size() > 0) {
            // 为AdapterViewFlipper设置Adapter
            initSwipeViewAdapter();
            initSwipeView();
            upLoadSelectImgs(list);
        } else {
            startDialog(null);
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

    @OnClick({R.id.back_btn, R.id.add_goods_list_sort, R.id.add_other_content_tv, R.id.commit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.add_goods_list_sort:
                SelectSortActivity.start(mContext, objectBean);
                break;
            case R.id.add_other_content_tv:
                AddGoodsPropertyActivity.start(mContext, objectBean, true);
                break;
            case R.id.commit_bt:
                getPresenter().addMoreGoods(mContext, mlist, objectBean);
                break;
        }
    }

    @Override
    public void onItemClick(int positions, View view) {
        ObjectBean objectBean = mlist.get(positions);

        startDialog(ADD_GOODS_CODE == objectBean.get__v() ? null : objectBean);
    }

    /**
     * 添加物品的dialog
     */
    private void startDialog(ObjectBean objectBean) {
        if (objectBean != null) {
            objectBean.setSelect(true);
        }
        if (mDialog == null) {
            //添加
            mDialog = new AddGoodsDialog(mContext, mlist.size()) {
                @Override
                public void result(ObjectBean bean) {
                    //上传
                    if (!TextUtils.isEmpty(bean.getObject_url()) && !bean.getObject_url().contains("7xroa4") && !bean.getObject_url().contains("#") && !bean.getObject_url().contains("cdn.shouner.com/object/image")) {
                        uploadBean = bean;
                        getPresenter().uploadImg(mContext, new File(bean.getObject_url()));
                        return;
                    }
                    if (!bean.isSelect()) {
                        bean.setSelect(false);
                        mlist.add(AppConstant.DEFAULT_INDEX_OF, bean);
                    }
                    mAdapter.notifyDataSetChanged();
                    setCommitBtnEnable(mlist.size() > 1);
                }

                @Override
                public void scanCode() {
                    startActivityForResult(new Intent(mContext, CaptureActivity.class), BOOK_CODE);
                }
            };
        }
        mDialog.show();
        mDialog.setObjectBean(objectBean);
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
    public void addObjectsSuccess(List<ObjectBean> data) {

    }

    @Override
    public void addMoreGoodsSuccess(List<ObjectBean> data) {
        Toast.makeText(mContext, "添加完成", Toast.LENGTH_SHORT).show();
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
            objectBean = (ObjectBean) data.getSerializableExtra("objectBean");
            if (objectBean.getCategories() != null && objectBean.getCategories().size() > 0) {
                sortNameTv.setText(objectBean.getCategories().get(AppConstant.DEFAULT_INDEX_OF).getName());
            } else {
                sortNameTv.setText(R.string.add_goods_sort);
            }
            goodsInfoView.init(objectBean);
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

    @Override
    public void showProgressDialog() {
        loading = Loading.show(null, mContext);
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


    public static void start(Context context, ObjectBean objectBean) {
        Intent intent = new Intent(context, AddMoreGoodsActivity.class);
        if (objectBean != null) {
            intent.putExtra("bean", objectBean);
        }
        context.startActivity(intent);
    }

    public static void start(Context context, ArrayList<String> lists) {
        Intent intent = new Intent(context, AddMoreGoodsActivity.class);
        if (lists != null) {
            intent.putStringArrayListExtra("imgList", lists);
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
    public void getCamareImg(File file) {

    }

    @Override
    public void getSelectPhotoImg(File file) {

    }

    @Override
    public void getCropBitmap(File file) {

    }
}