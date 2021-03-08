package com.gongwu.wherecollect.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IBindEmailContract;
import com.gongwu.wherecollect.contract.presenter.BindEmailPresenter;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.Loading;

import butterknife.BindView;
import butterknife.OnClick;

public class BindEmailActivity extends BaseMvpActivity<BindEmailActivity, BindEmailPresenter> implements IBindEmailContract.IBindEmailView {
    private static final String TAG = "BindEmailActivity";

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.email_name)
    EditText mEmailView;
    @BindView(R.id.email_password)
    EditText mPasswordView;

    private Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_bind_email;
    }

    @Override
    protected void initViews() {
        titleTv.setText("绑定邮箱");
    }

    @OnClick({R.id.back_btn, R.id.submit_bt})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.submit_bt:
                if (TextUtils.isEmpty(mEmailView.getText().toString())) {
                    ToastUtil.show(mContext, "请输入邮箱账号");
                    return;
                }
                if (TextUtils.isEmpty(mPasswordView.getText().toString())) {
                    ToastUtil.show(mContext, "密码");
                    return;
                }
                getPresenter().bindEmail(App.getUser(mContext).getId(), mEmailView.getText().toString(), mPasswordView.getText().toString());
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    @Override
    public void bindEmailSuccess(RequestSuccessBean data) {
        if (data != null && data.getOk() == AppConstant.REQUEST_SUCCESS) {
            ToastUtil.show(mContext, "绑定成功");
            setResult(RESULT_OK);
            finish();
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

    }

    @Override
    protected BindEmailPresenter createPresenter() {
        return BindEmailPresenter.getInstance();
    }


}
