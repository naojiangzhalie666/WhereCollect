package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.ISelectSortContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.request.EditCustomizeReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class SelectSortModel implements ISelectSortContract.ISelectSortModel {
    @Override
    public void getFirstCategoryList(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getFirstCategoryList(uid, new ApiCallBack<List<BaseBean>>() {
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
    public void getCategoryDetails(String uid, String code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getCategoryDetails(uid, code, new ApiCallBack<List<ChannelBean>>() {
            @Override
            public void onSuccess(List<ChannelBean> data) {
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
    public void editCustomizeName(EditCustomizeReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.editCustomCate(req, new ApiCallBack<RequestSuccessBean>() {
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
}
