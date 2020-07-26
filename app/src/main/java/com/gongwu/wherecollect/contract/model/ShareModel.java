package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IShareContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.ShareReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class ShareModel implements IShareContract.IShareModel {


    @Override
    public void getSharedUsersList(ShareReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getSharedUsersList(req, new ApiCallBack<List<SharedPersonBean>>() {
            @Override
            public void onSuccess(List<SharedPersonBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getSharedLocations(ShareReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getSharedLocations(req, new ApiCallBack<List<SharedLocationBean>>() {
            @Override
            public void onSuccess(List<SharedLocationBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void closeShareUser(ShareReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.closeShareUser(req, new ApiCallBack<RequestSuccessBean>() {
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
