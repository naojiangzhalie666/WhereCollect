package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.widget.RelativeLayout;


import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IBuyVIPContract;
import com.gongwu.wherecollect.contract.presenter.BuyVIPPresenter;
import com.gongwu.wherecollect.net.entity.WxPayBean;
import com.gongwu.wherecollect.net.entity.response.BuyVIPResultBean;
import com.gongwu.wherecollect.net.entity.response.VIPBean;
import com.gongwu.wherecollect.util.StringUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购买vip
 */
public class BuyVIPActivity extends BaseMvpActivity<BuyVIPActivity, BuyVIPPresenter> implements IBuyVIPContract.IBuyVIPView {

    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;

    private static final String WECHAT = "wechat";

    private static final String ALIPAY = "alipay";
    private VIPBean vipBean;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_vip;
    }

    @Override
    protected void initViews() {
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
        lp.setMargins(0, StringUtils.getStatusBarHeight(mContext), 0, 0);

        getPresenter().getVIPPrice(App.getUser(mContext).getId());
    }


    @OnClick({R.id.back_btn, R.id.commit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn://返回
                finish();
                break;
            case R.id.commit_bt:
                if (vipBean == null) {
                    getPresenter().getVIPPrice(App.getUser(mContext).getId());
                } else {
                    getPresenter().buyVipWXOrAli(App.getUser(mContext).getId(), 1, WECHAT, null);
                }
                break;
            default:
                break;
        }
    }

    /**
     * 调支付的方法
     * <p>
     * 注意： 每次调用微信支付的时候都会校验 appid 、包名 和 应用签名的。 这三个必须保持一致才能够成功调起微信
     *
     * @param wxPayBean
     */
    private void startWechatPay(WxPayBean wxPayBean) {
//        tv_paylist.setText(tv_paylist.getText() + "\n\n调起微信支付....");
        //这里的appid，替换成自己的即可
        IWXAPI api = WXAPIFactory.createWXAPI(this, AppConstant.WX_APP_ID);
        api.registerApp(AppConstant.WX_APP_ID);
        //这里的bean，是服务器返回的json生成的bean
        PayReq payRequest = new PayReq();
        payRequest.appId = AppConstant.WX_APP_ID;
        payRequest.partnerId = wxPayBean.getPartnerid();
        payRequest.prepayId = wxPayBean.getPrepayid();
        payRequest.packageValue = "Sign=WXPay";//固定值
        payRequest.nonceStr = wxPayBean.getNoncestr();
        payRequest.timeStamp = wxPayBean.getTimestamp();
        payRequest.sign = wxPayBean.getSign();
        //发起请求，调起微信前去支付
        api.sendReq(payRequest);
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BuyVIPActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void getVIPPriceSuccess(VIPBean data) {
        if (data != null) {
            vipBean = data;
        }
    }

    @Override
    public void buyVipWXOrAliSuccess(BuyVIPResultBean data) {
        if (data != null && data.getWeichat() != null) {
            startWechatPay(data.getWeichat());
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
    protected BuyVIPPresenter createPresenter() {
        return BuyVIPPresenter.getInstance();
    }
}
