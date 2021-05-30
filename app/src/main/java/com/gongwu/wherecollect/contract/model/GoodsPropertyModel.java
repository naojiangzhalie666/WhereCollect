package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IGoodsPropertyContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class GoodsPropertyModel implements IGoodsPropertyContract.IGoodsPropertyModel {
    @Override
    public void getBelongerList(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getBelongerList(uid, new ApiCallBack<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getBuyFirstCategoryList(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getBuyFirstCategoryList(uid, new ApiCallBack<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getSubCategoryList(String uid, String parentCode, String type, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getSubCategoryList(uid, parentCode, type, new ApiCallBack<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getTwoSubCategoryList(String uid, String parentCode, String type, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getSubCategoryList(uid, parentCode, type, new ApiCallBack<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getThreeSubCategoryList(String uid, String parentCode, String type, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getSubCategoryList(uid, parentCode, type, new ApiCallBack<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
