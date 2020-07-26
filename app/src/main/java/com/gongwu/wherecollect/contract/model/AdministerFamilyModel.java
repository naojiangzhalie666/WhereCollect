package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IAdministerFamilyContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;
import com.gongwu.wherecollect.util.ApiUtils;


public class AdministerFamilyModel implements IAdministerFamilyContract.IAdministerFamilyModel {
    @Override
    public void getFamilyList(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getFamilyList(uid, new ApiCallBack<MyFamilyListBean>() {
            @Override
            public void onSuccess(MyFamilyListBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
