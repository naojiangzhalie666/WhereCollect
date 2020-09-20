package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.BindAppReq;
import com.gongwu.wherecollect.net.entity.request.BindPhoneReq;
import com.gongwu.wherecollect.net.entity.request.EditPasswordReq;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;


public interface IConfigChangeContract {
    interface IConfigChangeModel {
        void bindPhone(BindPhoneReq req, final RequestCallback callback);

        void changePassword(EditPasswordReq req, final RequestCallback callback);

        void isRegistered(EditPersonReq req, final RequestCallback callback);

        void getCode(LoginReq req, final RequestCallback callback);

    }

    interface IConfigChangePresenter {
        void bindPhone(String uid, String mobile, String code);


        void changePassword(String uid, String original_password, String password);

        void isRegistered(String uid, String phone);

        void getCode(String phone, String capture);
    }

    interface IConfigChangeView extends BaseView {
        void bindPhoneSuccess(RequestSuccessBean data);

        void changePasswordSuccess(RequestSuccessBean data);

        void isRegisteredSuccess(RequestSuccessBean data);

        void getCodeSuccess(RequestSuccessBean data);
    }
}
