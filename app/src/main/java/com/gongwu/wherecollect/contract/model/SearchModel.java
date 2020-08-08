package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.ISearchContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class SearchModel implements ISearchContract.ISearchModel {
    @Override
    public void getSearchList(SearchReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getSearchList(req, new ApiCallBack<SerchListBean>() {
            @Override
            public void onSuccess(SerchListBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
