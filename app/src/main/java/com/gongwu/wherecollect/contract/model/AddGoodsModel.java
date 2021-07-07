package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.IAddGoodsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.BarcodeBean;
import com.gongwu.wherecollect.net.entity.request.AddGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.request.UserReq;
import com.gongwu.wherecollect.net.entity.response.BarcodeResultBean;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
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

    @Override
    public void getGoodsByBarcode(BarcodeBean barcodeBean, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsByBarcode(barcodeBean, new ApiCallBack<BarcodeResultBean>() {
            @Override
            public void onSuccess(BarcodeResultBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getGoodsByTBbarcode(BarcodeBean barcodeBean, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsByTBbarcode(barcodeBean, new ApiCallBack<BarcodeResultBean>() {
            @Override
            public void onSuccess(BarcodeResultBean data) {
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
