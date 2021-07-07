package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IBuyEnergyContract;
import com.gongwu.wherecollect.contract.presenter.BuyEnergyPresenter;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.ShareUtil;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.HintEnergyDialog;
import com.gongwu.wherecollect.view.InputPasswordDialog;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyEnergyActivity extends BaseMvpActivity<BuyEnergyActivity, BuyEnergyPresenter> implements IBuyEnergyContract.IBuyEnergyView {

    private static final String TAG = "BuyEnergyActivity";

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title_commit_tv_color_maincolor)
    TextView titleRightBtn;
    @BindView(R.id.user_energy_num_tv)
    TextView userEnergyNumTv;
    private UserBean userBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_energy;
    }

    @Override
    protected void initViews() {
        titleTv.setText("补充能量");
        titleRightBtn.setText("充值记录");
        titleRightBtn.setVisibility(View.VISIBLE);
        userBean = App.getUser(mContext);
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        StatusBarUtil.setLightStatusBar(this, true);
        userEnergyNumTv.setText(new StringBuilder("我的能量值:").append(App.getUser(mContext).getEnergy_value()).toString());
    }

    @OnClick({R.id.back_btn, R.id.title_commit_tv_color_maincolor, R.id.buy_energy_tv, R.id.input_barcode})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.title_commit_tv_color_maincolor:
                MessageListActivity.start(mContext, "ENERGY");
                break;
            case R.id.buy_vip_tv:
                if (userBean != null) {
                    if (!userBean.isIs_vip()) {
                        BuyVIPActivity.start(mContext);
                    } else {
                        ToastUtil.show(mContext, "您已是高级会员");
                    }
                }
                break;
            case R.id.buy_energy_tv:
            case R.id.vip_buy_energy_tv:
                new HintEnergyDialog(this, App.getUser(mContext).isIs_vip());
                break;
            case R.id.share_app_tv:
                ShareUtil.openShareDialog(this);
                break;
            case R.id.input_barcode:
                new InputPasswordDialog((Activity) mContext, "请输入领取码", "【如何获取领取码?】", Config.WEB_COLLECTION_NAME, Config.WEB_COLLECTION_URL);
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    @Override
    protected BuyEnergyPresenter createPresenter() {
        return BuyEnergyPresenter.getInstance();
    }

    @Override
    public void getEnergyPriceSuccess(EnergyPriceBean data) {

    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BuyEnergyActivity.class);
        context.startActivity(intent);
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

}
