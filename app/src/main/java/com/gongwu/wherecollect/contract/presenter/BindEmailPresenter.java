package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IBindEmailContract;
import com.gongwu.wherecollect.contract.model.BindEmailModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.BindEmailReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public class BindEmailPresenter extends BasePresenter<IBindEmailContract.IBindEmailView> implements IBindEmailContract.IBindEmailPresenter {

    private IBindEmailContract.IBindEmailModel mModel;

    private BindEmailPresenter() {
        mModel = new BindEmailModel();
    }

    public static BindEmailPresenter getInstance() {
        return BindEmailPresenter.Inner.instance;
    }

    @Override
    public void bindEmail(String uid, String email, String password) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        BindEmailReq req = new BindEmailReq();
        req.setUid(uid);
        req.setMail(email);
        req.setPassword(password);
        req.setType("MAIL");
        mModel.bindEmail(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().bindEmailSuccess(data);
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
        private static final BindEmailPresenter instance = new BindEmailPresenter();
    }


}
