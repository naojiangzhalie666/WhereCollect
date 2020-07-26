package com.gongwu.wherecollect.object;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.GoodsImageView;
import com.gongwu.wherecollect.view.ObjectInfoLookView;

import butterknife.BindView;
import butterknife.OnClick;

public class GoodsDetailsActivity extends BaseActivity {
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

    private ObjectBean objectBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_goods_details;
    }

    @Override
    protected void initViews() {
        mTitleView.setText(R.string.goods_text);
        imageBtn.setVisibility(View.VISIBLE);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        initData();
    }

    private void initData() {
        objectBean = (ObjectBean) getIntent().getSerializableExtra("objectBean");
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
        }
    }

    @OnClick({R.id.back_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
        }
    }

    @Override
    protected void initPresenter() {

    }

    public static void start(Context mContext, ObjectBean objectBean) {
        Intent intent = new Intent(mContext, GoodsDetailsActivity.class);
        intent.putExtra("objectBean", objectBean);
        mContext.startActivity(intent);
    }
}
