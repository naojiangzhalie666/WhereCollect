package com.gongwu.wherecollect.contract.presenter;

import android.app.Activity;
import android.content.Context;

import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IPersonContract;
import com.gongwu.wherecollect.contract.model.PersonModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.BindAppBean;
import com.gongwu.wherecollect.net.entity.request.BindAppReq;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.DialogUtil;
import com.gongwu.wherecollect.util.JsonUtils;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;

public class PersonPresenter extends BasePresenter<IPersonContract.IPersonView> implements IPersonContract.IPersonPresenter {
    private static final String TAG = "PersonPresenter";

    public static final int AVATAR = 0;
    public static final int NICKNAME = 1;
    public static final int GENDER = 2;
    public static final int BIRTHDAY = 3;


    private IPersonContract.IPersonModel mModel;

    private PersonPresenter() {
        mModel = new PersonModel();
    }

    public static PersonPresenter getInstance() {
        return PersonPresenter.Inner.instance;
    }

    private static class Inner {
        private static final PersonPresenter instance = new PersonPresenter();
    }

    @Override
    public void getUserInfo(String uid) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getUserInfo(uid, new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getUserInfoSuccess(data);
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
    public void editInfo(String uid, int keyword, String value) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditPersonReq req = new EditPersonReq();
        req.setUid(uid);
        switch (keyword) {
            case AVATAR:
                req.setAvatar(value);
                break;
            case NICKNAME:
                req.setNickname(value);
                break;
            case GENDER:
                req.setGender(value);
                break;
            case BIRTHDAY:
                req.setBirthday(value);
                break;
        }
        mModel.editInfo(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().editInfoSuccess(data);
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
    public void bindCheck(String uid, String openid, String type, BindAppReq req) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.bindCheck(uid, openid, type, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().bindCheckSuccess(data, req);
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
    public void bindAccount(BindAppReq req) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.bindAccount(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().bindAccountSuccess(data);
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

    UmAuthListener listener;

    public void otherLogin(Context mContext, SHARE_MEDIA sm) {
        if (listener == null) {
            listener = new UmAuthListener(mContext);
        }
        UMShareAPI.get(mContext).getPlatformInfo((Activity) mContext, sm, listener);
    }

    class UmAuthListener implements UMAuthListener {
        Context context;

        public UmAuthListener(Context context) {
            this.context = context;
        }

        @Override
        public void onStart(SHARE_MEDIA share_media) {
        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            BindAppReq req = new BindAppReq();
            BindAppBean bean = new BindAppBean();
            bean.setNickname(map.get("name"));
            bean.setOpenid(map.get("uid"));
            req.setUid(App.getUser(context).getId());
            switch (share_media) {
                case QQ:
                    req.setQq(JsonUtils.jsonFromObject(bean));
                    req.setType("QQ");
                    bindCheck(App.getUser(context).getId(), map.get("uid"), "QQ", req);
                    break;
                case WEIXIN:
                    req.setWeixin(JsonUtils.jsonFromObject(bean));
                    req.setType("WECHAT");
                    bindCheck(App.getUser(context).getId(), map.get("uid"), "WECHAT", req);
                    break;
                case SINA:
                    req.setSina(JsonUtils.jsonFromObject(bean));
                    req.setType("SINA");
                    bindCheck(App.getUser(context).getId(), map.get("uid"), "SINA", req);
                    break;
            }
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
            DialogUtil.show("提示", "授权失败,请检查是否装有第三方应用", "好的", "", (Activity) context, null, null);
        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {
        }
    }
}
