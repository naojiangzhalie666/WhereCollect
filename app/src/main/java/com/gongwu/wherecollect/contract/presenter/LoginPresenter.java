package com.gongwu.wherecollect.contract.presenter;

import android.app.Activity;
import android.content.Intent;

import com.gongwu.wherecollect.activity.LoginActivity;
import com.gongwu.wherecollect.activity.MainActivity;
import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BaseActivity;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.ILoginContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.contract.model.LoginModel;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.ResponseBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.EventBusMsg;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.SaveDate;
import com.gongwu.wherecollect.util.StringUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;
import java.util.Map;

public class LoginPresenter extends BasePresenter<ILoginContract.ILoginView> implements ILoginContract.ILoginPresenter {

    private static final String TAG = "LoginPresenter";


    private ILoginContract.ILoginModel mModel;
    private UmAuthListener umAuthListener;

    private LoginPresenter() {
        mModel = new LoginModel();
    }

    public void setUmAuthListener(BaseActivity activity) {
        umAuthListener = new UmAuthListener(activity);
    }

    public UmAuthListener getUmAuthListener() {
        return umAuthListener;
    }


    public static LoginPresenter getInstance() {
        return new LoginPresenter();
    }

    class UmAuthListener implements UMAuthListener {
        private WeakReference<BaseActivity> reference;

        public UmAuthListener(BaseActivity activity) {
            reference = new WeakReference<>(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            switch (share_media) {
                case QQ:
                    loginbyThirdParty(map, "qq", StringUtils.getCurrentVersionName(reference.get()));
                    break;
                case WEIXIN:
                    loginbyThirdParty(map, "wechat", StringUtils.getCurrentVersionName(reference.get()));
                    break;
                case SINA:
                    loginbyThirdParty(map, "sina", StringUtils.getCurrentVersionName(reference.get()));
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            DialogUtil.show("提示", "授权失败,请检查是否装有第三方应用", "好的", "", reference.get(), null, null);
//            ToastUtil.show(context, "授权失败:" + throwable.toString(), Toast.LENGTH_LONG);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
        }
    }

    @Override
    public void registerUserTest() {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.registerUserTest(new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().registerUserTestSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void loginPhone(String phone, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.loginPhone(new LoginReq(phone, code), new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().loginPhoneSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void loginEmail(String email, String pwd, String versioncode) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        LoginReq req = new LoginReq();
        req.setMail(email);
        req.setPassword(pwd);
        mModel.loginbyThirdParty(req, versioncode, new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().loginbyThirdPartySuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void loginbyThirdParty(Map<String, String> infoMap, String type, String versioncode) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        LoginReq req = new LoginReq();
        req.setAvatar(infoMap.get("iconurl"));
        req.setGender(infoMap.get("gender"));
        req.setNickname(infoMap.get("name"));
        req.setOpenid(infoMap.get("uid"));
        req.setPassword(infoMap.get("uid"));
        req.setUid(infoMap.get("uid"));
        req.setUnionid(infoMap.get("uid"));
        req.setLoginway(type);
        mModel.loginbyThirdParty(req, versioncode, new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().loginbyThirdPartySuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }


    @Override
    public void logoutTest(String uid) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.logoutTest(uid, new RequestCallback<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().logoutTestSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void getCode(String phone, String capture) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getCode(phone, capture, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getCodeSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    public void startActivityForResult(Activity ac, Class<?> cls) {
        Intent intent = new Intent(ac, cls);
        intent.putExtra(LoginActivity.TYPE_SPLASH, ac.getIntent().getBooleanExtra(LoginActivity.TYPE_SPLASH, false));
        ac.startActivityForResult(intent, AppConstant.REQUEST_CODE);
    }

    public void startMainActivity(Activity ac, UserBean user) {
        if (App.getUser(ac) != null && App.getUser(ac).isTest()) {
            user.setId(user.getId());
            user.setTestId("");
            logoutTest(App.getUser(ac).getId());
            SaveDate.getInstence(ac).setUser(JsonUtils.jsonFromObject(user));
            App.setUser(user);
            return;
        }
        SaveDate.getInstence(ac).setUser(JsonUtils.jsonFromObject(user));
        App.setUser(user);
        logoutTestSuccess(ac);
    }

    public void logoutTestSuccess(Activity ac) {
        if (ac.getIntent().getBooleanExtra(LoginActivity.TYPE_SPLASH, false)) {
            Intent intent = new Intent(ac, MainActivity.class);
            ac.startActivity(intent);
        } else {
            EventBus.getDefault().post(new EventBusMsg.MainTabMessage(AppConstant.DEFAULT_INDEX_OF));
        }
        ac.setResult(Activity.RESULT_OK);
        ac.finish();
    }

}

