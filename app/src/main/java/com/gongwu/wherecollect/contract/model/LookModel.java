package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.ILookContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsListReq;
import com.gongwu.wherecollect.net.entity.request.UserReq;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
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
    public void getChangWangList(String uid, RequestCallback callback) {
        if (callback == null) return;
        UserReq req = new UserReq();
        req.setUid(uid);
        ApiUtils.getCangWangList(req, new ApiCallBack<List<ChangWangBean>>() {
            @Override
            public void onSuccess(List<ChangWangBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getObjectBean(String uid, String family_code, boolean darklayer, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getUserGoddsList(uid, family_code, darklayer, new ApiCallBack<List<MainGoodsBean>>() {
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
    public void setGoodsWeight(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.setGoodsWeight(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void setGoodsNoWeight(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.setGoodsNoWeight(req, new ApiCallBack<RequestSuccessBean>() {
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
    public void removeArchiveObjects(GoodsDetailsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.removeArchiveObjects(req, new ApiCallBack<RequestSuccessBean>() {
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
