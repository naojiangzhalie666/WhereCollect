package com.gongwu.wherecollect.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.ILoginContract;
import com.gongwu.wherecollect.net.Config;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.ResponseBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.contract.presenter.LoginPresenter;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.view.Loading;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginEmailActivity extends BaseMvpActivity<LoginEmailActivity, LoginPresenter> implements ILoginContract.ILoginView {

    private static final String TAG = "LoginEmailActivity";

    private static final int START_CODE = 0x641;

    @BindView(R.id.title_tv)
    TextView mTitleView;
    @BindView(R.id.email_edit)
    EditText emailEdit;
    @BindView(R.id.pwd_edit)
    EditText pwdEdit;
    private Loading loading;

    @Override
    protected LoginPresenter createPresenter() {
        return LoginPresenter.getInstance();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login_email;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));

        mTitleView.setText(R.string.login_title);
        getPresenter().setUmAuthListener(this);
    }

    @OnClick({R.id.back_btn, R.id.login_wechat_iv, R.id.tv_regist, R.id.tv_forgetPWD, R.id.login_tv, R.id.login_phone_iv, R.id.agree})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.back_btn:
            case R.id.login_phone_iv:
                finish();
                break;
            case R.id.login_wechat_iv:
                UMShareAPI.get(this).getPlatformInfo(this, SHARE_MEDIA.WEIXIN, getPresenter().getUmAuthListener());
                break;
            case R.id.tv_regist:
                intent = new Intent(LoginEmailActivity.this, RegisterActivity.class);
                startActivityForResult(intent, START_CODE);
                break;
            case R.id.tv_forgetPWD:
                intent = new Intent(LoginEmailActivity.this, ForgetPWDActivity.class);
                startActivity(intent);
                break;
            case R.id.login_tv:
                if (TextUtils.isEmpty(emailEdit.getText().toString().trim()) || TextUtils.isEmpty(pwdEdit.getText().toString().trim())) {
                    Toast.makeText(mContext, "请填写邮箱或密码", Toast.LENGTH_SHORT).show();
                    return;
                }
                getPresenter().loginEmail(emailEdit.getText().toString().trim(), pwdEdit.getText().toString().trim(), StringUtils.getCurrentVersionName(mContext));
                break;
            case R.id.agree:
                WebActivity.start(mContext, Config.WEB_SERVICE_NAME, Config.WEB_SERVICE_URL);
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }


    @Override
    public void loginbyThirdPartySuccess(UserBean data) {
        getPresenter().startMainActivity(this, data,true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (START_CODE == requestCode && RESULT_OK == resultCode) {
            UserBean userBean = (UserBean) data.getSerializableExtra("user");
            getPresenter().startMainActivity(this, userBean,true);
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


    @Override
    public void registerUserTestSuccess(UserBean data) {
        //该界面无用
    }

    @Override
    public void loginPhoneSuccess(UserBean data) {
        //该界面无用
    }

    @Override
    public void logoutTestSuccess(ResponseBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            getPresenter().logoutTestSuccess(this);
        }
    }

    @Override
    public void getCodeSuccess(RequestSuccessBean data) {
        //该界面无用
    }

}
