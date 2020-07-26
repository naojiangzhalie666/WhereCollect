package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IMainContract;
import com.gongwu.wherecollect.contract.model.MainModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;


public class MainPresenter extends BasePresenter<IMainContract.IMainView> implements IMainContract.IMainPresenter {

    private IMainContract.IMainModel mModel;

    private MainPresenter() {
        mModel = new MainModel();
    }

    public static MainPresenter getInstance() {
        return MainPresenter.Inner.instance;
    }

    private static class Inner {
        private static final MainPresenter instance = new MainPresenter();
    }

    @Override
    public void dealWithShareRequest(String uid, String url) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.dealWithShareRequest(uid, url, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().dealWithShareRequestSuccess(data);
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
