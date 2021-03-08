package com.gongwu.wherecollect.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IRegisterContract;
import com.gongwu.wherecollect.contract.presenter.RegisterPresenter;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.StatusBarUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.Loading;


import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseMvpActivity<RegisterActivity, RegisterPresenter> implements IRegisterContract.IRegisterView {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.email_edit)
    EditText emailEdit;
    @BindView(R.id.pwd_edit)
    EditText pwdEdit;
    @BindView(R.id.again_pwd_edit)
    EditText pwdAginEdit;

    private Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_registe;
    }

    @Override
    protected void initViews() {
        StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.activity_bg));
        titleTv.setText(R.string.registe_title_tx);
    }

    @Override
    protected RegisterPresenter createPresenter() {
        return RegisterPresenter.getInstance();
    }

    @OnClick({R.id.back_btn, R.id.register_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.register_tv:
                userRegister();
                break;
        }
    }

    private void userRegister() {
        if (!StringUtils.isEmail(emailEdit.getText().toString())) {
            ToastUtil.show(this, "请输入正确的邮箱格式", Toast.LENGTH_LONG);
            return;
        }
        if (!(pwdEdit.getText().toString().length() >= 6 && pwdEdit.getText().toString().length() <= 18)) {
            ToastUtil.show(this, "密码长度为6-18位", Toast.LENGTH_LONG);
            return;
        }
        if (!pwdEdit.getText().toString().equals(pwdAginEdit.getText().toString())) {
            ToastUtil.show(this, "输入的密码不一致", Toast.LENGTH_LONG);
            return;
        }
        getPresenter().register(emailEdit.getText().toString(), pwdEdit.getText().toString());
    }

    @Override
    public void registerSuccess(UserBean data) {
        Intent intent = new Intent();
        intent.putExtra("user", data);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void getCodeSuccess(RequestSuccessBean data) {

    }

    @Override
    public void forgetPWDSuccess(RequestSuccessBean data) {

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
        Toast.makeText(mContext,result,Toast.LENGTH_SHORT).show();
    }
}
