package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IAdministerFamilyDetailsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.FamilyListDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class AdministerFamilyDetailsModel implements IAdministerFamilyDetailsContract.IAdministerFamilyDetailsModel {
    @Override
    public void getFamilyDetails(String uid, String familyCode, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getFamilyDetails(uid, familyCode, new ApiCallBack<FamilyListDetailsBean>() {
            @Override
            public void onSuccess(FamilyListDetailsBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void delFamily(String uid, String familyCode, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.delFamily(uid, familyCode, new ApiCallBack<RequestSuccessBean>() {
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
    public void disShareFamily(String uid, String familyCode, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.disShareFamily(uid, familyCode, new ApiCallBack<RequestSuccessBean>() {
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
    public void editFamily(String uid, String familyCode, String familyName, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.editFamily(uid, familyCode, familyName, new ApiCallBack<RequestSuccessBean>() {
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
