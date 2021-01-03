package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IAddChangWangGoodsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddChangWangGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangDetailBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangListBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class AddChangWangGoodsModel implements IAddChangWangGoodsContract.IAddChangWangGoodsModel {


    @Override
    public void getChangWangGoodsList(AddChangWangGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getCangWangGoodsList(req, new ApiCallBack<ChangWangListBean>() {
            @Override
            public void onSuccess(ChangWangListBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void setChangWangDetail(AddChangWangGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.setCangWangDetail(req, new ApiCallBack<ChangWangDetailBean>() {
            @Override
            public void onSuccess(ChangWangDetailBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
