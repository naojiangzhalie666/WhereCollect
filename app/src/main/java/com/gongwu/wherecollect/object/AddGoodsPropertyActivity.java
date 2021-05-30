package com.gongwu.wherecollect.object;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.activity.BuyVIPActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IGoodsPropertyContract;
import com.gongwu.wherecollect.contract.presenter.GoodsPropertyPresenter;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.ObjectInfoEditView;
import com.gongwu.wherecollect.view.SortBelongerDialog;
import com.gongwu.wherecollect.view.SortChildDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class AddGoodsPropertyActivity extends BaseMvpActivity<AddGoodsPropertyActivity, GoodsPropertyPresenter> implements IGoodsPropertyContract.IGoodsPropertyView, ObjectInfoEditView.OnItemClickListener {

    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.goodsInfo_other_view)
    ObjectInfoEditView goodsInfoView;
    @BindView(R.id.title_commit_bg_main_color_tv)
    TextView editInfoCommitTv;

    private ObjectBean objectBean;
    private SortChildDialog sortChildDialog;
    private List<BaseBean> mOneLists = new ArrayList<>();
    private List<BaseBean> mTwoLists = new ArrayList<>();
    private List<BaseBean> mThreeLists = new ArrayList<>();
    private boolean initOne, initTwo;
    private String type = null;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_add_goods_property;
    }

    @Override
    protected void initViews() {
        editInfoCommitTv.setVisibility(View.VISIBLE);
        if (getIntent().getBooleanExtra("isAddMoreGoods", false)) {
            mTitleTv.setText(R.string.add_more_goods_property);
        } else {
            mTitleTv.setText(R.string.add_goods_property);
        }
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        goodsInfoView.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
        goodsInfoView.init(objectBean);
    }

    @OnClick({R.id.back_btn, R.id.title_commit_bg_main_color_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_commit_bg_main_color_tv:
                onClickCommit();
                break;

        }
    }

    private void onClickCommit() {
        Intent intent = new Intent();
        intent.putExtra("objectBean", objectBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (AppConstant.START_GOODS_INFO_CODE == requestCode && RESULT_OK == resultCode) {
            objectBean = (ObjectBean) data.getSerializableExtra("objectBean");
            goodsInfoView.init(objectBean);
        }
    }

    public static void start(Context mContext, ObjectBean objectBean, boolean isAddMoreGoods) {
        Intent intent = new Intent(mContext, AddGoodsPropertyActivity.class);
        intent.putExtra("objectBean", objectBean);
        intent.putExtra("isAddMoreGoods", isAddMoreGoods);
        ((Activity) mContext).startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    @Override
    protected GoodsPropertyPresenter createPresenter() {
        return GoodsPropertyPresenter.getInstance();
    }

    @Override
    public void getBelongerListSuccess(List<BaseBean> data) {
        mOneLists.clear();
        mOneLists.addAll(data);
        SortBelongerDialog belongerDialog = new SortBelongerDialog(mContext) {
            @Override
            public void addSortChildClick() {

            }

            @Override
            public void submitClick(int currentIndex) {
                objectBean.setBelonger(mOneLists.get(currentIndex).getName());
                goodsInfoView.init(objectBean);
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
                goodsInfoView.init(objectBean);
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

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {

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
