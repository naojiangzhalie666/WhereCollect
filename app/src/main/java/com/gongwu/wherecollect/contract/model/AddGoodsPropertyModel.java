package com.gongwu.wherecollect.contract.model;


import com.gongwu.wherecollect.contract.IAddGoodsPropertyContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddGoodsPropertyReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class AddGoodsPropertyModel implements IAddGoodsPropertyContract.IAddGoodsPropertyModel {

    @Override
    public void getColors(String uid, RequestCallback callback) {
        if (callback == null) return;
        AddGoodsPropertyReq base = new AddGoodsPropertyReq();
        base.setUid(uid);
        ApiUtils.getColors(base, new ApiCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getChannel(String uid, RequestCallback callback) {
        if (callback == null) return;
        AddGoodsPropertyReq base = new AddGoodsPropertyReq();
        base.setUid(uid);
        ApiUtils.getChannel(base, new ApiCallBack<List<ChannelBean>>() {
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
    public void getChannelList(String uid, String keyword, RequestCallback callback) {
        if (callback == null) return;
        AddGoodsPropertyReq base = new AddGoodsPropertyReq();
        base.setUid(uid);
        base.setKeyword(keyword);
        ApiUtils.getChannelList(base, new ApiCallBack<List<ChannelBean>>() {
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
    public void getSearchSort(String uid, String category_code, String keyword, RequestCallback callback) {
        if (callback == null) return;
        AddGoodsPropertyReq base = new AddGoodsPropertyReq();
        base.setUid(uid);
        base.setKeyword(keyword);
        base.setCategory_code(category_code);
        ApiUtils.getSearchSort(base, new ApiCallBack<List<ChannelBean>>() {
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
    public void addChannel(String uid, String name, String code, RequestCallback callback) {
        if (callback == null) return;
        AddGoodsPropertyReq base = new AddGoodsPropertyReq();
        base.setUid(uid);
        base.setName(name);
        base.setCode(code);
        ApiUtils.addChannel(base, new ApiCallBack<ChannelBean>() {
            @Override
            public void onSuccess(ChannelBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
