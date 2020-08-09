package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;


public interface IConfigChangeContract {
    interface IConfigChangeModel {
//        void changePhone(EditPersonReq req, final RequestCallback callback);

        void isRegistered(EditPersonReq req, final RequestCallback callback);

        void getCode(LoginReq req, final RequestCallback callback);
    }

    interface IConfigChangePresenter {
//        void changePhone(String uid, int keyword, String value);

        void isRegistered(String uid, String phone);

        void getCode(String phone, String capture);
    }

    interface IConfigChangeView extends BaseView {
//        void changePhoneSuccess(RequestSuccessBean data);

        void isRegisteredSuccess(RequestSuccessBean data);

        void getCodeSuccess(RequestSuccessBean data);
    }
}
