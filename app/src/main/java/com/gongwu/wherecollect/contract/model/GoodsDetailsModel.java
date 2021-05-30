package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IGoodsDetailsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class GoodsDetailsModel implements IGoodsDetailsContract.IGoodsDetailsModel {
    @Override
    public void goodsArchive(GoodsDetailsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.goodsArchive(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void removeObjectFromFurnitrue(GoodsDetailsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.removeObjectFromFurnitrue(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void delGoods(GoodsDetailsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.delGoods(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void getGoodsRemindsById(String uid, String obj_id, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsRemindsById(uid, obj_id, new ApiCallBack<List<RemindBean>>() {
            @Override
            public void onSuccess(List<RemindBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

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
