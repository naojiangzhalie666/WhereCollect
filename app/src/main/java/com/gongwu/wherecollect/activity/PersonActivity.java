package com.gongwu.wherecollect.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gongwu.wherecollect.R;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseMvpActivity;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IPersonContract;
import com.gongwu.wherecollect.contract.presenter.PersonPresenter;
import com.gongwu.wherecollect.net.entity.request.BindAppReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.ImageLoader;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.Lg;
import com.gongwu.wherecollect.util.QiNiuUploadUtil;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.StringUtils;
import com.gongwu.wherecollect.util.ToastUtil;
import com.gongwu.wherecollect.view.ChangeHeaderImgDialog;
import com.gongwu.wherecollect.view.ChangeSexDialog;
import com.gongwu.wherecollect.view.DateBirthdayDialog;
import com.gongwu.wherecollect.view.EditTextDialog;
import com.gongwu.wherecollect.view.Loading;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;

public class PersonActivity extends BaseMvpActivity<PersonActivity, PersonPresenter> implements IPersonContract.IPersonView {

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
    private int key;
    private String value;

    private Loading loading;
    private ChangeHeaderImgDialog changeHeaderdialog;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_person;
    }

    @Override
    protected void initViews() {
        mTitleView.setText(R.string.title_ac_person);
        getPresenter().getUserInfo(App.getUser(mContext).getId());
    }

    @OnClick({R.id.back_btn, R.id.tv_loginOut, R.id.nick_layout, R.id.sex_layout, R.id.birth_layout,
            R.id.head_layout, R.id.phone_layout, R.id.wx_layout, R.id.wb_layout, R.id.qq_layout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                finish();
                break;
            case R.id.head_layout:
                changeHeaderdialog = new ChangeHeaderImgDialog(this, null, personIv) {
                    @Override
                    public void getResult(File file) {
                        upLoadImg(file);
                    }
                };
                break;
            case R.id.nick_layout:
                EditTextDialog nameDialog = new EditTextDialog(mContext, "昵称", "请输入新昵称",
                        EditTextDialog.TYPE_TEXT) {
                    @Override
                    public void result(String result) {
                        key = PersonPresenter.NICKNAME;
                        value = result;
                        getPresenter().editInfo(App.getUser(mContext).getId(), PersonPresenter.NICKNAME, result);
                    }
                };
                nameDialog.show();
                break;

            case R.id.sex_layout:
                new ChangeSexDialog(this) {
                    @Override
                    public void getResult(String str) {
                        super.getResult(str);
                        key = PersonPresenter.GENDER;
                        value = str;
                        getPresenter().editInfo(App.getUser(mContext).getId(), PersonPresenter.GENDER, str);
                    }
                };
                break;
            case R.id.birth_layout:
                selectDateDialog();
                break;
            case R.id.tv_loginOut:
                logout();
                break;
            case R.id.phone_layout:
                ConfigChangePhoneActivity.start(mContext);
                break;
            case R.id.wx_layout:
                getPresenter().otherLogin(mContext, SHARE_MEDIA.WEIXIN);
                break;
            case R.id.wb_layout:
                getPresenter().otherLogin(mContext, SHARE_MEDIA.SINA);
                break;
            case R.id.qq_layout:
                getPresenter().otherLogin(mContext, SHARE_MEDIA.QQ);
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
                        intent.putExtra("isFinish", true);
                        startActivity(intent);
                        setResult(RESULT_OK);
                        finish();
                        EventBus.getDefault().post(new EventBusMsg.StopService());
                    }
                }, null);
    }

    /**
     * 刷新UI
     */
    public void refreshUi() {
        if (user == null) return;
        ImageLoader.loadCircle(mContext, personIv, user.getAvatar(), R.drawable.ic_user_error);
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
            qqLayout.setEnabled(false);
        } else {
            tvPhone.setText(R.string.unbound_text);
        }
        if (user.getWeixin() != null && (!TextUtils.isEmpty(user.getWeixin().getOpenid()))) {
            tvWx.setText(user.getWeixin().getNickname());
            wxLayout.setEnabled(false);
        } else {
            tvWx.setText(R.string.unbound_text);
        }
        if (user.getSina() != null && (!TextUtils.isEmpty(user.getSina().getOpenid()))) {
            tvWb.setText(user.getSina().getNickname());
            wbLayout.setEnabled(false);
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
    public void getUserInfoSuccess(UserBean data) {
        if (data != null) {
            data.setPassLogin(App.getUser(mContext).isPassLogin());
            user = data;
            SaveDate.getInstence(this).setUser(JsonUtils.jsonFromObject(user));
            App.setUser(user);
            refreshUi();
        }
    }

    @Override
    public void editInfoSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            Toast.makeText(mContext, "修改成功", Toast.LENGTH_SHORT).show();
            switch (key) {
                case PersonPresenter.AVATAR:
                    user.setAvatar(value);
                    break;
                case PersonPresenter.NICKNAME:
                    tvNick.setText(value);
                    user.setNickname(value);
                    break;
                case PersonPresenter.GENDER:
                    tvSex.setText(value);
                    user.setGender(value);
                    break;
                case PersonPresenter.BIRTHDAY:
                    tvBirthday.setText(value);
                    user.setBirthday(value);
                    break;

            }
            key = -1;
            value = null;
            refreshUi();
            String stringUser = JsonUtils.jsonFromObject(user);
            SaveDate.getInstence(mContext).setUser(stringUser);
        }
    }

    @Override
    public void bindCheckSuccess(RequestSuccessBean data, BindAppReq req) {
        //判断第三方账号是否绑定
        if (data.getBound() == AppConstant.BIND_APP) {
            DialogUtil.show("提示", "已绑定其他收哪儿账号,是否继续绑定?继续绑定将删除以前绑定账号", "继续", "取消", this, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    getPresenter().bindAccount(req);
                }
            }, null);
        } else {
            getPresenter().bindAccount(req);
        }
    }

    @Override
    public void bindAccountSuccess(RequestSuccessBean data) {
        if (data.getOk() == AppConstant.REQUEST_SUCCESS) {
            getPresenter().getUserInfo(App.getUser(mContext).getId());
        }
    }

    /**
     * 上传图片
     */
    private void upLoadImg(final File file) {
        QiNiuUploadUtil uploadUtil = QiNiuUploadUtil.getInstance(mContext);
        uploadUtil.setUpLoadListener(new QiNiuUploadUtil.UpLoadListener() {
            @Override
            public void onUpLoadSuccess(String path) {
                key = PersonPresenter.AVATAR;
                value = path;
                getPresenter().editInfo(App.getUser(mContext).getId(), PersonPresenter.AVATAR, path);
            }

            @Override
            public void onUpLoadError(String msg) {

            }
        });
        uploadUtil.start(AppConstant.UPLOAD_GOODS_IMG_PATH, file);
    }

    public void selectDateDialog() {
        String birthday = "";
        if (TextUtils.isEmpty(App.getUser(mContext).getBirthday())) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            birthday = formatter.format(curDate);
        } else {
            birthday = App.getUser(mContext).getBirthday();
        }
        DateBirthdayDialog dialog = new DateBirthdayDialog(mContext, birthday) {
            @Override
            public void result(final int year, final int month, final int day) {
                final String bd = year + "-" + StringUtils.formatIntTime(month) + "-" +
                        StringUtils.formatIntTime(day);
                if (!TextUtils.isEmpty(App.getUser(mContext).getBirthday())) {
                    if (App.getUser(mContext).getBirthday().equals(bd)) {
                        return;
                    }
                }
                key = PersonPresenter.BIRTHDAY;
                value = bd;
                getPresenter().editInfo(App.getUser(mContext).getId(), PersonPresenter.BIRTHDAY, bd);
            }
        };
        dialog.setCancelBtnText(true);
        dialog.show();
        dialog.setDateMax();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (changeHeaderdialog != null) {
            changeHeaderdialog.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void showProgressDialog() {
        loading = Loading.show(null, mContext, "");
    }

    @Override
    public void hideProgressDialog() {
        if (loading != null) {
            loading.dismiss();
        }
    }

    @Override
    public void onError(String result) {
        ToastUtil.show(mContext, result, Toast.LENGTH_SHORT);
    }

    @Override
    protected PersonPresenter createPresenter() {
        return PersonPresenter.getInstance();
    }
}
