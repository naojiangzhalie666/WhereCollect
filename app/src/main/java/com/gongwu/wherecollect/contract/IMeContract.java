package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;


public interface IMeContract {
    interface IMeModel {
        void getUserInfo(String uid, final RequestCallback callback);

        void getEnergyCode(String uid, String code, final RequestCallback callback);
    }

    interface IMePresenter {
        void getUserInfo(String uid);

        void getEnergyCode(String uid, String code);
    }

    interface IMeView extends BaseView {
        void getUserInfoSuccess(UserBean data);

        void getEnergyCodeSuccess(RequestSuccessBean data);
    }
}
