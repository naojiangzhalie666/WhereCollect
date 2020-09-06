package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IConfigChangeContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.BindPhoneReq;
import com.gongwu.wherecollect.net.entity.request.EditPasswordReq;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.request.LoginReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class ConfigChangeModel implements IConfigChangeContract.IConfigChangeModel {
    @Override
    public void bindPhone(BindPhoneReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.bindAccount(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void changePassword(EditPasswordReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.changePassword(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void isRegistered(EditPersonReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.isRegistered(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void getCode(LoginReq req, RequestCallback callback) {
        if (callback == null) return;
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
