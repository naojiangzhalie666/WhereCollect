package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.ForgetPWDReq;
import com.gongwu.wherecollect.net.entity.request.RegisterReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;

public interface IRegisterContract {
    interface IRegisterModel {

        void register(RegisterReq req, final RequestCallback callback);

        void getCode(String phone, String capture, final RequestCallback callback);

        void forgetPWD(ForgetPWDReq req, final RequestCallback callback);
    }

    interface IRegisterPresenter {

        void register(String mail, String password);

        void getCode(String phone, String capture);

        void forgetPWD(String phone, String password, String code);
    }

    interface IRegisterView extends BaseView {

        void registerSuccess(UserBean data);

        void getCodeSuccess(RequestSuccessBean data);

        void forgetPWDSuccess(RequestSuccessBean data);
    }
}
