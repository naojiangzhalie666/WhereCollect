package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IBuyEnergyContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class BuyEnergyModel implements IBuyEnergyContract.IBuyEnergyModel {
    @Override
    public void getEnergyPrice(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getEnergyPrice(uid, new ApiCallBack<EnergyPriceBean>() {
            @Override
            public void onSuccess(EnergyPriceBean data) {
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
    public void getEnergyCode(String uid, String code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getEnergyCode(uid, code, new ApiCallBack<RequestSuccessBean>() {
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
