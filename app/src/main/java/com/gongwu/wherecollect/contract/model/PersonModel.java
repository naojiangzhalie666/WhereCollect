package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IPersonContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.BindAppReq;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class PersonModel implements IPersonContract.IPersonModel {
    @Override
    public void getUserInfo(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getUserInfo(uid, new ApiCallBack<UserBean>() {
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
    public void editInfo(EditPersonReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.editInfo(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void bindCheck(String uid, String openid, String type, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.bindCheck(uid, openid, type, new ApiCallBack<RequestSuccessBean>() {
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
    public void bindAccount(BindAppReq req, RequestCallback callback) {
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
}
