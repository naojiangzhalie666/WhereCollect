package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IRelationGoodsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RelationGoodsBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class RelationGoodsModel implements IRelationGoodsContract.IRelationGoodsModel {
    @Override
    public void getRelationGoodsList(String uid, String category_code, String keyword, int page, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getRelationGoodsList(uid, category_code, keyword, page, new ApiCallBack<RelationGoodsBean>() {
            @Override
            public void onSuccess(RelationGoodsBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getRelationCategories(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getRelationCategories(uid, new ApiCallBack<List<BaseBean>>() {
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
