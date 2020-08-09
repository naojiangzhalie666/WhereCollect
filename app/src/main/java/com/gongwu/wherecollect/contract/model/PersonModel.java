package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IPersonContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class PersonModel implements IPersonContract.IPersonModel {
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
}
