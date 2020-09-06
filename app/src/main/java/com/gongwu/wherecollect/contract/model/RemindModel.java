package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.IRemindContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddRemindReq;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class RemindModel implements IRemindContract.IRemindModel {

    @Override
    public void getRemindList(String uid, String done, int page, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getRemindList(uid, done, page, new ApiCallBack<RemindListBean>() {
            @Override
            public void onSuccess(RemindListBean data) {
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

}
