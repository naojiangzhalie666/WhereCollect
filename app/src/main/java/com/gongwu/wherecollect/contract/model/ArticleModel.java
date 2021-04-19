package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IArticleContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class ArticleModel implements IArticleContract.IArticleModel {
    @Override
    public void getArticList(RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getArticList(new ApiCallBack<List<ArticleBean>>() {
            @Override
            public void onSuccess(List<ArticleBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
