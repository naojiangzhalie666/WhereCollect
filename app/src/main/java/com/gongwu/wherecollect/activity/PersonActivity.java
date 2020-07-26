package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.SaveDate;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonActivity extends BaseActivity {

    private static final String TAG = "PersonActivity";

    @BindView(R.id.person_iv)
    ImageView personIv;
    @BindView(R.id.head_layout)
    LinearLayout headLayout;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.tv_birthday)
    TextView tvBirthday;
    @BindView(R.id.tv_loginOut)
    TextView tvLoginOut;
    @BindView(R.id.version_tv)
    TextView versionTv;
    @BindView(R.id.tv_wx)
    TextView tvWx;
    @BindView(R.id.wx_layout)
    RelativeLayout wxLayout;
    @BindView(R.id.tv_wb)
    TextView tvWb;
    @BindView(R.id.wb_layout)
    RelativeLayout wbLayout;
    @BindView(R.id.tv_email)
    TextView tvEmail;
    @BindView(R.id.qq_layout)
    RelativeLayout qqLayout;
    @BindView(R.id.tv_qq)
    TextView tvQq;
    @BindView(R.id.email_layout)
    RelativeLayout emailLayout;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.phone_layout)
    RelativeLayout phoneLayout;
    @BindView(R.id.tv_changePWD)
    TextView tvChangePWD;
    @BindView(R.id.tv_changePWD_view)
    View tv_changePWD_view;
    @BindView(R.id.title_tv)
    TextView mTitleView;

    private UserBean user;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void initViews() {
        mTitleView.setText(R.string.title_ac_person);
        refreshUi();
    }

    @OnClick({R.id.back_btn, R.id.tv_loginOut})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.tv_loginOut:
                logout();
                break;
            default:
                Lg.getInstance().e(TAG, "onClick default");
                break;
        }
    }

    private void logout() {
        DialogUtil.show("提示", "退出将会清空缓存数据,确定退出？", "确定", "取消", (Activity) mContext,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.setUser(null);
                        SaveDate.getInstence(mContext).setUser("");
                        Intent intent = new Intent(mContext, LoginActivity.class);
                        intent.putExtra("isFinish",true);
                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();
                        EventBus.getDefault().post(new EventBusMsg.stopService());
                    }
                }, null);
    }

    /**
     * 刷新UI
     */
    public void refreshUi() {
        user = App.getUser(mContext);
        ImageLoader.loadCircle(mContext, personIv, user.getAvatar(), R.drawable.icon_app);
        tvNick.setText(user.getNickname());
        tvSex.setText(user.getGender());
        tvBirthday.setText(user.getBirthday());
        if (user.isPassLogin()) {
            tvChangePWD.setVisibility(View.VISIBLE);
            tv_changePWD_view.setVisibility(View.VISIBLE);
        } else {
            tvChangePWD.setVisibility(View.GONE);
            tv_changePWD_view.setVisibility(View.GONE);
        }
        if (user == null)
            return;
        if (user.getQq() != null && (!TextUtils.isEmpty(user.getQq().getOpenid()))) {
            tvQq.setText(user.getQq().getNickname());
        } else {
            tvPhone.setText(R.string.unbound_text);
        }
        if (user.getWeixin() != null && (!TextUtils.isEmpty(user.getWeixin().getOpenid()))) {
            tvWx.setText(user.getWeixin().getNickname());
        } else {
            tvWx.setText(R.string.unbound_text);
        }
        if (user.getSina() != null && (!TextUtils.isEmpty(user.getSina().getOpenid()))) {
            tvWb.setText(user.getSina().getNickname());
        } else {
            tvWb.setText(R.string.unbound_text);
        }
        if (!TextUtils.isEmpty(user.getMail())) {
            tvEmail.setText(user.getMail());
        } else {
            tvEmail.setText(R.string.unbound_text);
        }
        if (!TextUtils.isEmpty(user.getMobile())) {
            tvPhone.setText(user.getMobile());
        } else {
            tvPhone.setText(R.string.unbound_text);
        }
    }

    @Override
    protected void initPresenter() {

    }

}
