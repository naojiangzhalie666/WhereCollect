package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.ILoginContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.base.RequestBase;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.request.LogoutTestReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.ResponseBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class LoginModel implements ILoginContract.ILoginModel {
    @Override
    public void registerUserTest(RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.registerUserTest(new RequestBase(), new ApiCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void loginPhone(LoginReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.login(req, new ApiCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void loginEmail(LoginReq req, String versoncode, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.loginbyThirdParty(req, versoncode, new ApiCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void loginbyThirdParty(LoginReq req, String versoncode, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.loginbyThirdParty(req, versoncode, new ApiCallBack<UserBean>() {
            @Override
            public void onSuccess(UserBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void logoutTest(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.logoutTest(new LogoutTestReq(uid), new ApiCallBack<ResponseBean>() {
            @Override
            public void onSuccess(ResponseBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getCode(String phone, String capture, RequestCallback callback) {
        if (callback == null) return;
        LoginReq req = new LoginReq();
        req.setPhone(phone);
        req.setCapture(capture);
        ApiUtils.getCode(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
