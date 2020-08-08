package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.IAddGoodsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddGoodsReq;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;
import java.util.Map;

public class AddGoodsModel implements IAddGoodsContract.IAddGoodsModel {

    @Override
    public void editGoods(AddGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.editGoods(req, new ApiCallBack<ObjectBean>() {
            @Override
            public void onSuccess(ObjectBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void addObjects(AddGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.addGoods(req, new ApiCallBack<List<ObjectBean>>() {
            @Override
            public void onSuccess(List<ObjectBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void addMoreGoods(AddGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.addMoreGoods(req, new ApiCallBack<List<ObjectBean>>() {
            @Override
            public void onSuccess(List<ObjectBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getBookInfo(AddGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getBookInfo(req, new ApiCallBack<BookBean>() {
            @Override
            public void onSuccess(BookBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getTaobaoInfo(AddGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getTaobaoInfo(req, new ApiCallBack<BookBean>() {
            @Override
            public void onSuccess(BookBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
