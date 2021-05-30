package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.ISelectSortChildNewContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.request.EditCustomizeReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class SelectSortChildNewModel implements ISelectSortChildNewContract.ISelectSortChildNewModel {

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

    @Override
    public void saveCustomSubCate(CustomSubCateReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.saveCustomSubCate(req, new ApiCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void saveCustomCate(CustomSubCateReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.saveCustomCate(req, new ApiCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void saveBelonger(CustomSubCateReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.saveBelonger(req, new ApiCallBack<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void deleteCustomize(EditCustomizeReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.deleteCustomCate(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void deleteBelonger(EditCustomizeReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.deleteBelonger(req, new ApiCallBack<RequestSuccessBean>() {
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
