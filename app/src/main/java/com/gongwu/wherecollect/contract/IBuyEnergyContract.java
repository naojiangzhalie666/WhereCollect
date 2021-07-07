package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.BindEmailReq;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;


public interface IBuyEnergyContract {
    interface IBuyEnergyModel {
        void getEnergyPrice(String uid, final RequestCallback callback);
    }

    interface IBuyEnergyPresenter {
        void getEnergyPrice(String uid);
    }

    interface IBuyEnergyView extends BaseView {
        void getEnergyPriceSuccess(EnergyPriceBean data);
    }
}
