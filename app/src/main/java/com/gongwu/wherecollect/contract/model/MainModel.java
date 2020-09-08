package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.IMainContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.VersionBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class MainModel implements IMainContract.IMainModel {


    @Override
    public void dealWithShareRequest(String uid, String url, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.dealWithShareRequest(uid, url, new ApiCallBack<RequestSuccessBean>() {
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
    public void getVersion(String app_version, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getVersion(app_version, new ApiCallBack<VersionBean>() {
            @Override
            public void onSuccess(VersionBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
