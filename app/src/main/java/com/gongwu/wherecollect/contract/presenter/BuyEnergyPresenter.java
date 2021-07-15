package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IBuyEnergyContract;
import com.gongwu.wherecollect.contract.model.BuyEnergyModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;

public class BuyEnergyPresenter extends BasePresenter<IBuyEnergyContract.IBuyEnergyView> implements IBuyEnergyContract.IBuyEnergyPresenter {

    private IBuyEnergyContract.IBuyEnergyModel mModel;

    private BuyEnergyPresenter() {
        mModel = new BuyEnergyModel();
    }

    public static BuyEnergyPresenter getInstance() {
        return BuyEnergyPresenter.Inner.instance;
    }

    private static class Inner {
        private static final BuyEnergyPresenter instance = new BuyEnergyPresenter();
    }

    @Override
    public void getEnergyPrice(String uid) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getEnergyPrice(uid, new RequestCallback<EnergyPriceBean>() {
            @Override
            public void onSuccess(EnergyPriceBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getEnergyPriceSuccess(data);
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
