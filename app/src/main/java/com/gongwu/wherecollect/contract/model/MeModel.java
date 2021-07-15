package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IMeContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class MeModel implements IMeContract.IMeModel {
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
