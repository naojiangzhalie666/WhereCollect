package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IEditMoreGoodsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.EditMoreGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class EditMoreGoodsModel implements IEditMoreGoodsContract.IEditMoreGoodsModel {
    @Override
    public void getEditMoreGoodsList(String uid, String family_code, String category_code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getEditMoreGoodsList(uid, family_code, category_code, new ApiCallBack<List<ObjectBean>>() {
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
    public void delSelectGoods(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.delSelectGoods(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void objectsAddCategory(EditMoreGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.objectsAddCategory(req, new ApiCallBack<RequestSuccessBean>() {
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
