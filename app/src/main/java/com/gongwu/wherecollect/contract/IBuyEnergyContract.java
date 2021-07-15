package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.BindEmailReq;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;


public interface IBuyEnergyContract {
    interface IBuyEnergyModel {
        void getEnergyPrice(String uid, final RequestCallback callback);

        void getUserInfo(String uid, final RequestCallback callback);

        void getEnergyCode(String uid, String code, final RequestCallback callback);
    }

    interface IBuyEnergyPresenter {
        void getEnergyPrice(String uid);

        void getUserInfo(String uid);

        void getEnergyCode(String uid, String code);
    }

    interface IBuyEnergyView extends BaseView {
        void getEnergyPriceSuccess(EnergyPriceBean data);

        void getUserInfoSuccess(UserBean data);

        void getEnergyCodeSuccess(RequestSuccessBean data);
    }
}
