package com.gongwu.wherecollect.object;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.view.ObjectInfoEditView;

import butterknife.BindView;
import butterknife.OnClick;

public class AddGoodsPropertyActivity extends BaseActivity {

    public static final int REQUEST_CODE = 0x0010;

    @BindView(R.id.title_tv)
    TextView mTitleTv;
    @BindView(R.id.goodsInfo_other_view)
    ObjectInfoEditView goodsInfoView;
    @BindView(R.id.title_commit_bg_main_color_tv)
    TextView editInfoCommitTv;

    private ObjectBean objectBean;

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
        if (REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
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
    protected void initPresenter() {

    }
}
