package com.gongwu.wherecollect.activity;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IConfigChangeContract;
import com.gongwu.wherecollect.contract.presenter.ConfigChangePresenter;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;
import com.gongwu.wherecollect.util.Lg;
import com.pixplicity.sharp.Sharp;

import butterknife.BindView;
import butterknife.OnClick;

public class ConfigChangePhoneActivity extends BaseMvpActivity<ConfigChangePhoneActivity, ConfigChangePresenter> implements IConfigChangeContract.IConfigChangeView {
    private static final String TAG = "ConfigChangePhoneActivi";
    public static final int PASSWORD_CODE = 0x1111;
    public static final int PHONE_CODE = 0x1112;

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.new_phone)
    EditText mPhoneView;
    @BindView(R.id.img_code_et)
    EditText mImgEditView;
    @BindView(R.id.number)
    EditText numberET;
    @BindView(R.id.img_code_iv)
    ImageView imgCodeIv;
    @BindView(R.id.send_bt)
    Button sendBt;
    @BindView(R.id.submit_bt)
    Button commitBt;
    @BindView(R.id.old_password)
    EditText oldPasswordEt;
    @BindView(R.id.new_password)
    EditText newPasswordEt;
    @BindView(R.id.bind_phone_layout)
    View bind_phone_layout;
    @BindView(R.id.bind_password_layout)
    View bind_password_layout;

    private boolean changePhone;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_config_change_phone;
    }

    @Override
    protected void initViews() {
        changePhone = getIntent().getBooleanExtra("changePhone", false);
        titleTv.setText(changePhone ? "绑定手机号" : "修改密码");
        bind_phone_layout.setVisibility(changePhone ? View.VISIBLE : View.GONE);
        bind_password_layout.setVisibility(changePhone ? View.GONE : View.VISIBLE);
        if (!changePhone) {
            commitBt.setText("修改密码");
        }
        initImgCode();
    }

    @OnClick({R.id.back_btn, R.id.send_bt, R.id.img_code_iv, R.id.submit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.send_bt:
                isRegistered();
                break;
            case R.id.img_code_iv:
                initImgCode();
                break;
            case R.id.submit_bt:
                if (changePhone) {
                    commit();
                } else {
                    commitPassword();
                }
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    private void commit() {
        if (mPhoneView.getText().toString().trim().length() != 11) {
            Toast.makeText(mContext, getString(R.string.login_phone_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImgEditView.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, getString(R.string.login_img_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (numberET.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, getString(R.string.login_code_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().bindPhone(App.getUser(mContext).getId(), mPhoneView.getText().toString().trim(), numberET.getText().toString().trim());
    }

    private void commitPassword() {
        if (oldPasswordEt.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, getString(R.string.old_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (newPasswordEt.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, getString(R.string.new_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (oldPasswordEt.getText().toString().trim().equals(newPasswordEt.getText().toString().trim())) {
            Toast.makeText(mContext, getString(R.string.password_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().changePassword(App.getUser(mContext).getId(), oldPasswordEt.getText().toString().trim(), newPasswordEt.getText().toString().trim());
    }

    /**
     * 发送短信验证码
     */
    private void isRegistered() {
        if (mPhoneView.getText().toString().trim().length() != 11) {
            Toast.makeText(mContext, getString(R.string.login_phone_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImgEditView.getText().toString().trim().length() == 0) {
            Toast.makeText(mContext, getString(R.string.login_img_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().isRegistered(App.getUser(mContext).getId(), mPhoneView.getText().toString().trim());
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
    public void bindPhoneSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "绑定成功", Toast.LENGTH_SHORT).show();
            setResult(PHONE_CODE);
            finish();
        }
    }

    @Override
    public void changePasswordSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
            setResult(PASSWORD_CODE);
            finish();
        }
    }

    @Override
    public void isRegisteredSuccess(RequestSuccessBean data) {
        if (!data.isRegistered()) {
            getPresenter().getCode(mPhoneView.getText().toString().trim(), mImgEditView.getText().toString().trim());
        } else {
            Toast.makeText(mContext, "该手机号已注册", Toast.LENGTH_SHORT).show();
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
        sendBt.setEnabled(false);
        new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                sendBt.setText(millisUntilFinished / 1000 + "秒后再次获取");
            }

            public void onFinish() {
                sendBt.setText("获取验证码");
                sendBt.setEnabled(true);
            }
        }.start();
    }

    @Override
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected ConfigChangePresenter createPresenter() {
        return ConfigChangePresenter.getInstance();
    }

    public static void start(Context context, boolean changePhone) {
        Intent intent = new Intent(context, ConfigChangePhoneActivity.class);
        intent.putExtra("changePhone", changePhone);
        context.startActivity(intent);
    }
}
