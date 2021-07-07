package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IBuyEnergyContract;
import com.gongwu.wherecollect.contract.model.BuyEnergyModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;

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
}
