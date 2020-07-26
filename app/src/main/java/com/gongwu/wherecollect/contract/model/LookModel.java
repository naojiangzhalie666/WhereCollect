package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.ILookContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.GoodsListReq;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class LookModel implements ILookContract.ILookModel {

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
    public void getObjectBean(String uid, String family_code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getUserGoddsList(uid,family_code, new ApiCallBack<List<MainGoodsBean>>() {
            @Override
            public void onSuccess(List<MainGoodsBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

}