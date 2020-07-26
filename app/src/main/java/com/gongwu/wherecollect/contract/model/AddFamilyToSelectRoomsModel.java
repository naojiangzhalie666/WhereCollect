package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IAddFamilyToSelectRoomsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddFamilyReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class AddFamilyToSelectRoomsModel implements IAddFamilyToSelectRoomsContract.IAddFamilyToSelectRoomsModel {
    @Override
    public void getRoomsTemplate(AddFamilyReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getRoomsTemplate(req, new ApiCallBack<List<RoomFurnitureBean>>() {
            @Override
            public void onSuccess(List<RoomFurnitureBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void createFamily(AddFamilyReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.createFamily(req, new ApiCallBack<RequestSuccessBean>() {
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
