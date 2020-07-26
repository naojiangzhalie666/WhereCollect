package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IRegisterContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.ForgetPWDReq;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.request.RegisterReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class RegisterModel implements IRegisterContract.IRegisterModel {

    @Override
    public void register(RegisterReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.register(req, new ApiCallBack<UserBean>() {
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

    @Override
    public void forgetPWD(ForgetPWDReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.forgetPWD(req, new ApiCallBack<RequestSuccessBean>() {
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
