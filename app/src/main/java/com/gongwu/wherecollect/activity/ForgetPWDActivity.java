package com.gongwu.wherecollect.activity;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IRegisterContract;
import com.gongwu.wherecollect.contract.presenter.RegisterPresenter;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ApiUtils;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.ToastUtil;
import com.pixplicity.sharp.Sharp;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPWDActivity extends BaseMvpActivity<ForgetPWDActivity, RegisterPresenter> implements IRegisterContract.IRegisterView {

    private static final String TAG = "ForgetPWDActivity";

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.phone_et)
    EditText mPhoneView;
    @BindView(R.id.img_code_et)
    EditText mImgEditView;
    @BindView(R.id.code_et)
    EditText mCodeView;
    @BindView(R.id.new_password)
    EditText mPasswordView;
    @BindView(R.id.img_code_iv)
    ImageView imgCodeIv;
    @BindView(R.id.send_msg_tv)
    TextView sendMsgTv;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        titleTv.setText(R.string.forget_pwd_title);
        initImgCode();
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return RegisterPresenter.getInstance();
    }

    @OnClick({R.id.back_btn, R.id.send_msg_tv, R.id.img_code_iv, R.id.commit_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.send_msg_tv:
                sendMsg();
                break;
            case R.id.img_code_iv:
                initImgCode();
                break;
            case R.id.commit_tv:
                commit();
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
        if (mImgEditView.getText().toString().trim().length() != 4) {
            Toast.makeText(mContext, getString(R.string.login_img_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mCodeView.getText().toString().trim().length() != 6) {
            Toast.makeText(mContext, getString(R.string.login_code_error_text), Toast.LENGTH_SHORT).show();
            return;
        }

        if (mPasswordView.getText().length() < 6 || mPasswordView.getText().length() > 16) {
            ToastUtil.show(this, "请输入6-16位的密码", Toast.LENGTH_LONG);
            return;
        }
        getPresenter().forgetPWD(mPhoneView.getText().toString(), mPasswordView.getText().toString(), mCodeView.getText().toString());
    }

    /**
     * 发送短信验证码
     */
    private void sendMsg() {
        if (mPhoneView.getText().toString().trim().length() != 11) {
            Toast.makeText(mContext, getString(R.string.login_phone_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        if (mImgEditView.getText().toString().trim().length() != 4) {
            Toast.makeText(mContext, getString(R.string.login_img_error_text), Toast.LENGTH_SHORT).show();
            return;
        }
        getPresenter().getCode(mPhoneView.getText().toString().trim(), mImgEditView.getText().toString().trim());
    }


    @Override
    public void getCodeSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
            setBtDisEnble();
        }
    }

    @Override
    public void forgetPWDSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "密码修改成功", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void registerSuccess(UserBean data) {

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
    public void showProgressDialog() {

    }

    @Override
    public void hideProgressDialog() {

    }

    @Override
    public void onError(String result) {
        initImgCode();
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }
}
