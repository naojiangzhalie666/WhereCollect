package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IHomeContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class HomeModel implements IHomeContract.IHomeModel {

    @Override
    public void getUserFamily(String uid, String user_name, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getUserFamily(uid, user_name, new ApiCallBack<List<FamilyBean>>() {
            @Override
            public void onSuccess(List<FamilyBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getUserFamilyRoom(String uid, String code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getUserFamilyRoom(uid, code, new ApiCallBack<HomeFamilyRoomBean>() {
            @Override
            public void onSuccess(HomeFamilyRoomBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
