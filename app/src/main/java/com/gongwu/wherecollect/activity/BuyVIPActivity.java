package com.gongwu.wherecollect.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.core.content.ContextCompat;

import com.alipay.sdk.app.EnvUtils;
import com.alipay.sdk.app.PayTask;
import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IBuyVIPContract;
import com.gongwu.wherecollect.contract.presenter.BuyVIPPresenter;
import com.gongwu.wherecollect.net.entity.AuthResult;
import com.gongwu.wherecollect.net.entity.PayResult;
import com.gongwu.wherecollect.net.entity.WxPayBean;
import com.gongwu.wherecollect.net.entity.response.BuyVIPResultBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.net.entity.response.VIPBean;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.ShareUtil;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.Loading;
import com.gongwu.wherecollect.view.SelectVIPChannelDialog;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 购买vip界面
 */
public class BuyVIPActivity extends BaseMvpActivity<BuyVIPActivity, BuyVIPPresenter> implements IBuyVIPContract.IBuyVIPView {
    private static final String TAG = "BuyVIPActivity";

    @BindView(R.id.title_layout)
    RelativeLayout titleLayout;
    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.commit_bt)
    Button commitBt;
    @BindView(R.id.buy_vip_bg_iv)
    View bgView;
    @BindView(R.id.vip_content)
    View contentView;
    @BindView(R.id.back_btn)
    ImageView backView;

    private boolean isWechatCk;

    private VIPBean vipBean;
    private Loading loading;
    private String orderId;
    private String buyType;
    private int energyPrice;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_buy_vip;
    }

    @Override
    protected void initViews() {
        //支付宝正式环境要去掉
//        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);
        titleTv.setText(R.string.act_vip_buy_title);
        EventBus.getDefault().register(this);
        if (Build.VERSION.SDK_INT >= 21) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) titleLayout.getLayoutParams();
        lp.setMargins(0, StringUtils.getStatusBarHeight(mContext), 0, 0);
        getPresenter().getVIPPrice(App.getUser(mContext).getId());
        buyType = getIntent().getStringExtra("buy_type");
        energyPrice = getIntent().getIntExtra("price", 0);
        //判断是否为能量值购买
        if (AppConstant.ENERGY_TYPE.equals(buyType)) {
            if (energyPrice > 0) {
                bgView.setVisibility(View.GONE);
                contentView.setVisibility(View.GONE);
                titleTv.setText("支付");
                titleTv.setTextColor(ContextCompat.getColor(mContext, R.color.black_txt));
                backView.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.icon_card_act_finish));
                showBuyVipSelectChannel();
            } else {
                finish();
            }
        }
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
                    showBuyVipSelectChannel();
                }
                break;
            default:
                break;
        }
    }

    private void showBuyVipSelectChannel() {
        new SelectVIPChannelDialog(this) {
            @Override
            public void WECHATClick() {
                isWechatCk = true;
                if (AppConstant.ENERGY_TYPE.equals(buyType)) {
                    //能量
                    getPresenter().buyEnergy(App.getUser(mContext).getId(), energyPrice, AppConstant.WECHAT);
                } else {
                    //会员
                    getPresenter().buyVipWXOrAli(App.getUser(mContext).getId(), (int) (vipBean.getPrice() * 100), AppConstant.WECHAT, !TextUtils.isEmpty(vipBean.getCouponId()) ? vipBean.getCouponId() : null);
                }
            }

            @Override
            public void ALIPAYClick() {
                isWechatCk = false;
                if (AppConstant.ENERGY_TYPE.equals(buyType)) {
                    //能量
                    getPresenter().buyEnergy(App.getUser(mContext).getId(), energyPrice, AppConstant.ALIPAY);
                } else {
                    getPresenter().buyVipWXOrAli(App.getUser(mContext).getId(), (int) (vipBean.getPrice() * 100), AppConstant.ALIPAY, !TextUtils.isEmpty(vipBean.getCouponId()) ? vipBean.getCouponId() : null);
                }
            }

            @Override
            public void finish() {
                finishByEnergy();
            }
        };
    }

    private void sharedAPP() {
        ShareUtil.openShareVIPDialog(this, App.getUser(mContext).getId(), new UMShareListener() {
            @Override
            public void onStart(SHARE_MEDIA share_media) {

            }

            @Override
            public void onResult(SHARE_MEDIA share_media) {
//                getPresenter().sharedApp(App.getUser(mContext).getId(), "WECHAT");
            }

            @Override
            public void onError(SHARE_MEDIA share_media, Throwable throwable) {

            }

            @Override
            public void onCancel(SHARE_MEDIA share_media) {

            }
        });
    }

    /**
     * 调支付的方法
     * <p>
     * 注意： 每次调用微信支付的时候都会校验 appid 、包名 和 应用签名的。 这三个必须保持一致才能够成功调起微信
     *
     * @param wxPayBean
     */
    private boolean initwechat;

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
        boolean send = api.sendReq(payRequest);
        if (send) {
            orderId = wxPayBean.getOrder_no();
        } else {
            if (!initwechat) {
                initwechat = true;
                if (AppConstant.ENERGY_TYPE.equals(buyType)) {
                    if (energyPrice <= 0) return;
                    //能量
                    getPresenter().buyEnergy(App.getUser(mContext).getId(), energyPrice, AppConstant.WECHAT);
                } else {
                    if (vipBean != null && vipBean.getPrice() > 0) {
                        //会员
                        getPresenter().buyVipWXOrAli(App.getUser(mContext).getId(), (int) (vipBean.getPrice() * 100), AppConstant.WECHAT, !TextUtils.isEmpty(vipBean.getCouponId()) ? vipBean.getCouponId() : null);
                    }
                }
            }
        }
    }

    /**
     * 支付（加签过程不允许在客户端进行，必须在服务端，否则有极大的安全隐患）
     *
     * @param orderInfo 加签后的支付请求参数字符串（主要包含商户的订单信息，key=value形式，以&连接）。
     */
    private void pay(final String orderInfo) {
        final Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(BuyVIPActivity.this);
                //第二个参数设置为true，将会在调用pay接口的时候直接唤起一个loading
                Map<String, String> result = alipay.payV2(orderInfo, true);
                Message msg = new Message();
                msg.what = SDK_PAY_FLAG;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG: {
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        if (TextUtils.isEmpty(orderId)) return;
                        getPresenter().notificationServer(App.getUser(mContext).getId(), isWechatCk ? AppConstant.WECHAT : AppConstant.ALIPAY, orderId);
                        Toast.makeText(mContext, "支付成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        Toast.makeText(mContext, "支付失败", Toast.LENGTH_SHORT).show();
                        finishByEnergy();
                    }
                    break;
                }
                case SDK_AUTH_FLAG: {
                    @SuppressWarnings("unchecked")
                    AuthResult authResult = new AuthResult((Map<String, String>) msg.obj, true);
                    String resultStatus = authResult.getResultStatus();

                    // 判断resultStatus 为“9000”且result_code
                    // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                    if (TextUtils.equals(resultStatus, "9000") && TextUtils.equals(authResult.getResultCode(), "200")) {
                        // 获取alipay_open_id，调支付时作为参数extern_token 的value
                        // 传入，则支付账户为该授权账户
                        Lg.getInstance().d(TAG, "授权成功");
                        Toast.makeText(mContext, "授权成功", Toast.LENGTH_SHORT).show();
                    } else {
                        // 其他状态值则为授权失败
                        Lg.getInstance().d(TAG, "授权失败");
                        Toast.makeText(mContext, "授权失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                }
                default:
                    break;
            }
        }

    };

    private void finishByEnergy() {
        if (AppConstant.ENERGY_TYPE.equals(buyType)) {
            finish();
        }
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, BuyVIPActivity.class);
        context.startActivity(intent);
    }

    public static void start(Context context, String type, int price) {
        Intent intent = new Intent(context, BuyVIPActivity.class);
        if (!TextUtils.isEmpty(type)) {
            intent.putExtra("buy_type", type);
        }
        if (price > 0) {
            intent.putExtra("price", price);
        }
        context.startActivity(intent);
    }

    @Override
    public void getVIPPriceSuccess(VIPBean data) {
        if (data != null) {
            vipBean = data;
        }
    }

    @Override
    public void getUserInfoSuccess(UserBean data) {
        if (data != null) {
            SaveDate.getInstence(mContext).setUser(JsonUtils.jsonFromObject(data));
            App.setUser(data);
            setResult(RESULT_OK);
            finish();
        }
    }

    @Override
    public void sharedAppSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            commitBt.setVisibility(View.GONE);
            getPresenter().getVIPPrice(App.getUser(mContext).getId());
        }
    }

    @Override
    public void notificationServerSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            getPresenter().getUserInfo(App.getUser(mContext).getId());
        } else {
            Toast.makeText(mContext, "请稍后刷新尝试", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void buyVipWXOrAliSuccess(BuyVIPResultBean data) {
        if (data != null && data.getWeichat() != null) {
            startWechatPay(data.getWeichat());
        } else if (data != null && data.getAlipay() != null && !TextUtils.isEmpty(data.getAlipay().getPayUrl())) {
            pay(data.getAlipay().getPayUrl());
            if (!TextUtils.isEmpty(data.getAlipay().getOrder_no())) {
                orderId = data.getAlipay().getOrder_no();
            }
        }
    }

    @Override
    public void buyEnergySuccess(BuyVIPResultBean data) {
        if (data != null && data.getWeichat() != null) {
            startWechatPay(data.getWeichat());
        } else if (data != null && data.getAlipay() != null && !TextUtils.isEmpty(data.getAlipay().getPayUrl())) {
            pay(data.getAlipay().getPayUrl());
            if (!TextUtils.isEmpty(data.getAlipay().getOrder_no())) {
                orderId = data.getAlipay().getOrder_no();
            }
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
    protected BuyVIPPresenter createPresenter() {
        return BuyVIPPresenter.getInstance();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.BuyVipSuccess msg) {
        getPresenter().notificationServer(App.getUser(mContext).getId(), isWechatCk ? AppConstant.WECHAT : AppConstant.ALIPAY, orderId);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventBusMsg.WXPayMessage msg) {
        switch (msg.code) {
            case -2://用户取消，无需处理。发生场景：用户不支付了，点击取消，返回APP。
                finish();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


}
