package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IConfigChangeContract;
import com.gongwu.wherecollect.contract.model.ConfigChangeModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public class ConfigChangePresenter extends BasePresenter<IConfigChangeContract.IConfigChangeView> implements IConfigChangeContract.IConfigChangePresenter {

    private IConfigChangeContract.IConfigChangeModel mModel;

    private ConfigChangePresenter() {
        mModel = new ConfigChangeModel();
    }

    public static ConfigChangePresenter getInstance() {
        return ConfigChangePresenter.Inner.instance;
    }

    private static class Inner {
        private static final ConfigChangePresenter instance = new ConfigChangePresenter();
    }

    @Override
    public void isRegistered(String uid, String phone) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditPersonReq req = new EditPersonReq();
        req.setUid(uid);
        req.setPhone(phone);
        mModel.isRegistered(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().isRegisteredSuccess(data);
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
        LoginReq req = new LoginReq();
        req.setPhone(phone);
        req.setCapture(capture);
        mModel.getCode(req, new RequestCallback<RequestSuccessBean>() {
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
}
