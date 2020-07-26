package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.IFamilyContract;
import com.gongwu.wherecollect.contract.IRemindContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.FurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class FamilyModel implements IFamilyContract.IFamilyModel {


    @Override
    public void getFurnitureList(FurnitureReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getFurnitureList(req, new ApiCallBack<List<FurnitureBean>>() {
            @Override
            public void onSuccess(List<FurnitureBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
