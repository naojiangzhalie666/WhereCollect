package com.gongwu.wherecollect.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.ILoginContract;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.ResponseBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.contract.presenter.LoginPresenter;
import com.gongwu.wherecollect.util.ApiUtils;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.ActivityTaskManager;
import com.gongwu.wherecollect.view.Loading;
import com.pixplicity.sharp.Sharp;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseMvpActivity<LoginActivity, LoginPresenter> implements ILoginContract.ILoginView {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.back_btn)
    ImageButton backView;
    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.title_commit_maincolor_tv)
    TextView mCommitTv;
    @BindView(R.id.phone_et)
    EditText mPhoneView;
    @BindView(R.id.img_code_et)
    EditText mImgEditView;
    @BindView(R.id.code_et)
    EditText mCodeView;
    @BindView(R.id.img_code_iv)
    ImageView imgCodeIv;
    @BindView(R.id.send_msg_tv)
    TextView sendMsgTv;

    private Loading loading;

    public static final String TYPE_SPLASH = "type_splash";

    @Override
    protected LoginPresenter createPresenter() {
        return LoginPresenter.getInstance();
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        backView.setVisibility(View.GONE);
//        mCommitTv.setVisibility(View.VISIBLE);
//        mCommitTv.setText(R.string.login_trial);
        mTitleView.setText(R.string.login_title);
        getPresenter().setUmAuthListener(this);
        initImgCode();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick({R.id.login_tv, R.id.send_msg_tv, R.id.img_code_iv, R.id.login_wechat_iv, R.id.title_commit_maincolor_tv, R.id.agree, R.id.login_mail_iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login_tv:
                loginPhone();
                break;
            case R.id.send_msg_tv:
                sendMsg();
                break;
            case R.id.img_code_iv:
                initImgCode();
                break;
            case R.id.login_wechat_iv:
                otherLogin(SHARE_MEDIA.WEIXIN);
                break;
            case R.id.login_mail_iv:
                getPresenter().startActivityForResult(this, LoginEmailActivity.class);
                break;
            case R.id.title_commit_maincolor_tv:
                if (TextUtils.isEmpty(SaveDate.getInstence(mContext).getUser())) {
                    getPresenter().registerUserTest();
                } else {
                    finish();
                }
                break;
            case R.id.agree:
                WebActivity.start(mContext, Config.WEB_SERVICE_NAME, Config.WEB_SERVICE_URL);
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    /**
     * 发送短信验证码
     */
    private void sendMsg() {
        if (mPhoneView.getText().toString().trim().length() != 11) {
            Toast.makeText(mContext, getString(R.string.login_phone_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImgEditView.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, getString(R.string.login_img_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().getCode(mPhoneView.getText().toString().trim(), mImgEditView.getText().toString().trim());
    }

    /**
     * 手机登录
     */
    private void loginPhone() {
        if (mPhoneView.getText().toString().trim().length() != 11) {
            Toast.makeText(mContext, getString(R.string.login_phone_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCodeView.getText().toString().trim().length() != 6) {
            ToastUtil.show(this, getString(R.string.login_code_error_text), Toast.LENGTH_SHORT);
            return;
        }
        getPresenter().loginPhone(mPhoneView.getText().toString(), mCodeView.getText().toString());
    }

    @Override
    public void registerUserTestSuccess(UserBean data) {
        if (data != null) {
            //设置为试用用户
            data.setTest(true);
            getPresenter().startMainActivity(this, data,false);
        }
    }


    @Override
    public void loginbyThirdPartySuccess(UserBean data) {
        getPresenter().startMainActivity(this, data,true);
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
        initImgCode();
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    public void otherLogin(SHARE_MEDIA sm) {
        UMShareAPI.get(this).getPlatformInfo(this, sm, getPresenter().getUmAuthListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (AppConstant.REQUEST_CODE == requestCode && RESULT_OK == resultCode) {
            finish();
        }
    }

    @Override
    public void logoutTestSuccess(ResponseBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            getPresenter().logoutTestSuccess(this);
        }
    }

    @Override
    public void loginPhoneSuccess(UserBean data) {
        if (TextUtils.isEmpty(data.getMax_version())) {
            getPresenter().startMainActivity(this, data,true);
        } else {
            DialogUtil.show("提示", TextUtils.isEmpty(data.getLogin_messag()) ? "您的帐号已经在高版本使用过,请使用IOS版" : data.getLogin_messag(), "继续", "取消", LoginActivity.this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getPresenter().startMainActivity(LoginActivity.this, data,true);
                }
            }, null);
        }
    }

    @Override
    public void getCodeSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
            setBtDisEnble();
        }
    }

    private void setBtDisEnble() {
        sendMsgTv.setEnabled(false);
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                sendMsgTv.setText(millisUntilFinished / 1000 + "秒后再次获取");
            }

            public void onFinish() {
                sendMsgTv.setText("获取验证码");
                sendMsgTv.setEnabled(true);
            }
        }.start();
    }

    private void initImgCode() {
        ApiUtils.getCatpure(new ApiCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                data = data.replaceAll("viewBox=\"0,0,150,50\"", "");
                Sharp.loadString(data).into(imgCodeIv);
            }

            @Override
            public void onFailed(String msg) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getIntent().getBooleanExtra("isFinish", false)) {
            ActivityTaskManager.getInstance().finishAllActivity();
        }
    }
}
