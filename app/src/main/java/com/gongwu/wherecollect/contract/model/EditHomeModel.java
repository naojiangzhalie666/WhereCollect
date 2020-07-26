package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IEditHomeContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class EditHomeModel implements IEditHomeContract.IEditHomeModel {
    @Override
    public void getFamilyRoomList(String uid, String code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getFamilyRoomList(uid, code, new ApiCallBack<List<RoomBean>>() {
            @Override
            public void onSuccess(List<RoomBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void deleteFurniture(EditFurnitureReq furnitureReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.deleteFurniture(furnitureReq, new ApiCallBack<RequestSuccessBean>() {
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
    public void topFurniture(EditFurnitureReq furnitureReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.topFurniture(furnitureReq, new ApiCallBack<RequestSuccessBean>() {
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
    public void moveFurniture(EditFurnitureReq furnitureReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.moveFurniture(furnitureReq, new ApiCallBack<RequestSuccessBean>() {
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
    public void updataFurniture(EditFurnitureReq furnitureReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.updataFurniture(furnitureReq, new ApiCallBack<FurnitureBean>() {
            @Override
            public void onSuccess(FurnitureBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
