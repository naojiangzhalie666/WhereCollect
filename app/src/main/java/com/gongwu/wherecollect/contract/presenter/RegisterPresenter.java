package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IRegisterContract;
import com.gongwu.wherecollect.contract.model.RegisterModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.ForgetPWDReq;
import com.gongwu.wherecollect.net.entity.request.RegisterReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;

public class RegisterPresenter extends BasePresenter<IRegisterContract.IRegisterView> implements IRegisterContract.IRegisterPresenter {
    private IRegisterContract.IRegisterModel mModel;

    private RegisterPresenter() {
        mModel = new RegisterModel();
    }

    public static RegisterPresenter getInstance() {
        return RegisterPresenter.Inner.instance;
    }

    @Override
    public void register(String mail, String password) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        RegisterReq req = new RegisterReq();
        req.setMail(mail);
        req.setPassword(password);
        mModel.register(req, new RequestCallback<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().registerSuccess(data);
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

    @Override
    public void forgetPWD(String phone, String password, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        ForgetPWDReq req = new ForgetPWDReq();
        req.setCode(code);
        req.setPassword(password);
        req.setPhone(phone);
        mModel.forgetPWD(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().forgetPWDSuccess(data);
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

    private static class Inner {
        private static final RegisterPresenter instance = new RegisterPresenter();
    }
}
