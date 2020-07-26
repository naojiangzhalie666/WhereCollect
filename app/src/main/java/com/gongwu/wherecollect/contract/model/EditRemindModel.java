package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IEditRemindContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddRemindReq;
import com.gongwu.wherecollect.net.entity.response.RemindDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class EditRemindModel implements IEditRemindContract.IEditRemindModel {
    @Override
    public void addRemind(AddRemindReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.addRemind(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void updateRemind(AddRemindReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.updateRemind(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void deteleRemind(AddRemindReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.deteleRemind(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void setRemindDone(String uid, String remind_id, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.setRemindDone(uid, remind_id, new ApiCallBack<RequestSuccessBean>() {
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
    public void getRemindDetails(String uid, String remind_id, String associatedObjectId, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getRemindDetails(uid, remind_id, associatedObjectId, new ApiCallBack<RemindDetailsBean>() {
            @Override
            public void onSuccess(RemindDetailsBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
