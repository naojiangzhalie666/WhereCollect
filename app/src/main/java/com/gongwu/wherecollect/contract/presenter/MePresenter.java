package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IMeContract;
import com.gongwu.wherecollect.contract.model.MeModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;

public class MePresenter extends BasePresenter<IMeContract.IMeView> implements IMeContract.IMePresenter {

    private IMeContract.IMeModel mModel;

    private MePresenter() {
        mModel = new MeModel();
    }

    public static MePresenter getInstance() {
        return new MePresenter();
    }

    @Override
    public void getUserInfo(String uid) {
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
    public void getEnergyCode(String uid, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getEnergyCode(uid, code, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getEnergyCodeSuccess(data);
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
