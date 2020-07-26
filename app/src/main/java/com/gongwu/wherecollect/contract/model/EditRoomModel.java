package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IEditRoomContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditRoomReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class EditRoomModel implements IEditRoomContract.IEditRoomModel {
    @Override
    public void updataRoomPosition(EditRoomReq roomReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.updataRoomPosition(roomReq, new ApiCallBack<List<RoomBean>>() {
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
    public void addRoom(EditRoomReq roomReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.addRoom(roomReq, new ApiCallBack<RoomBean>() {
            @Override
            public void onSuccess(RoomBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void delRoom(EditRoomReq roomReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.delRoom(roomReq, new ApiCallBack<RequestSuccessBean>() {
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
    public void editRoom(EditRoomReq roomReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.editRoom(roomReq, new ApiCallBack<RequestSuccessBean>() {
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
    public void moveRoom(EditRoomReq roomReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.moveRoom(roomReq, new ApiCallBack<RequestSuccessBean>() {
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
