package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.ResponseBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;

import java.util.Map;

public interface ILoginContract {
    interface ILoginModel {

        void registerUserTest(final RequestCallback callback);

        void loginPhone(LoginReq req, final RequestCallback callback);

        void loginEmail(LoginReq req, String versioncode, final RequestCallback callback);

        void loginbyThirdParty(LoginReq req, String versoncode, final RequestCallback callback);

        void logoutTest(String uid, final RequestCallback callback);

        void getCode(String phone, String capture, final RequestCallback callback);
    }

    interface ILoginPresenter {
        void registerUserTest();

        void loginPhone(String phone, String code);

        void loginEmail(String email, String pwd, String versioncode);

        void loginbyThirdParty(Map<String, String> infoMap, String type, String versioncode);

        void logoutTest(String uid);

        void getCode(String phone, String capture);
    }

    interface ILoginView extends BaseView {
        /**
         * 注册测试账号监听
         */
        void registerUserTestSuccess(UserBean data);

        /**
         * 手机号登陆
         */
        void loginPhoneSuccess(UserBean data);

        /**
         * 第三方登录账号监听
         */
        void loginbyThirdPartySuccess(UserBean data);

        /**
         * 注销测试账号
         */
        void logoutTestSuccess(ResponseBean data);

        /**
         * 获取验证码
         */
        void getCodeSuccess(RequestSuccessBean data);
    }
}
