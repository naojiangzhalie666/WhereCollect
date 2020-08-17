package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IBuyVIPContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.BuyVIPResultBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.net.entity.response.VIPBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class BuyVIPModel implements IBuyVIPContract.IBuyVIPModel {
    @Override
    public void getVIPPrice(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getVIPPrice(uid, new ApiCallBack<VIPBean>() {
            @Override
            public void onSuccess(VIPBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

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
    public void sharedApp(String uid, String share_type, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.sharedApp(uid, share_type, new ApiCallBack<RequestSuccessBean>() {
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
    public void notificationServer(String uid, String pay_type, String order_no, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.notificationServer(uid, pay_type, order_no, new ApiCallBack<RequestSuccessBean>() {
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
    public void buyVipWXOrAli(String uid, int price, String type, String couponId, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.buyVipWXOrAli(uid, price, type, couponId, new ApiCallBack<BuyVIPResultBean>() {
            @Override
            public void onSuccess(BuyVIPResultBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
