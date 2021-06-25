package com.gongwu.wherecollect.activity;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.IFeedBackContract;
import com.gongwu.wherecollect.contract.presenter.FeedBackPresenter;
import com.gongwu.wherecollect.net.entity.response.FeedbackBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.Loading;

import butterknife.BindView;
import butterknife.OnClick;

public class FeedBackActivity extends BaseMvpActivity<FeedBackActivity, FeedBackPresenter> implements IFeedBackContract.IFeedBackView {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.title)
    EditText title;
    @BindView(R.id.info)
    EditText info;
    @BindView(R.id.phone)
    EditText phone;

    private Loading loading;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_feed_back;
    }

    @Override
    protected void initViews() {
        titleTv.setText("意见反馈");
        if (!TextUtils.isEmpty(App.getUser(this).getMail())) {
            phone.setText(App.getUser(this).getMail());
        }
    }

    @OnClick({R.id.back_btn, R.id.commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.commit:
                if (TextUtils.isEmpty(title.getText())) {
                    ToastUtil.show(this, "请填写标题", Toast.LENGTH_LONG);
                    return;
                }
                if (TextUtils.isEmpty(info.getText())) {
                    ToastUtil.show(this, "请填写反馈内容", Toast.LENGTH_LONG);
                    return;
                }
                if (StringUtils.isEmail(phone.getText().toString())) {
                    feedBack();
                } else {
                    DialogUtil.show("提醒", "如果需要反馈，请添加联系邮箱，我们每一条都会回复。", "直接提交", "填邮箱", this, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            feedBack();
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            phone.setFocusable(true);
                            phone.setSelection(phone.getText().length());
                            phone.requestFocus();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    InputMethodManager imanager = (InputMethodManager) mContext
                                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imanager.showSoftInput(phone, 0);
                                }
                            }, 100);
                        }
                    });
                }
                break;
        }
    }

    private void feedBack() {
        getPresenter().feedBack(App.getUser(mContext).getId(), title.getText().toString(),
                String.format("%s\n%s", info.getText().toString(), "联系方式：" + phone.getText().toString()), mContext);
    }

    @Override
    public void feedBackSuccess(FeedbackBean data) {
        if (data != null && App.getUser(mContext).getId().equals(data.get_id())) {
            ToastUtil.show(FeedBackActivity.this, "反馈成功", Toast.LENGTH_LONG);
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
        Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected FeedBackPresenter createPresenter() {
        return FeedBackPresenter.getInstance();
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, FeedBackActivity.class);
        context.startActivity(intent);
    }
}
