package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IDetailedListContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class DetailedListModel implements IDetailedListContract.IDetailedListModel {

    @Override
    public void getDetailedList(String uid, String family_code, String room_code, String furniture_code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getDetailedList(uid, family_code, room_code, furniture_code, new ApiCallBack<DetailedListBean>() {
            @Override
            public void onSuccess(DetailedListBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
