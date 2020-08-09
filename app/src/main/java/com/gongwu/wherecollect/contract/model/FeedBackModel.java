package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IFeedBackContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.FeedBackReq;
import com.gongwu.wherecollect.net.entity.response.FeedbackBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class FeedBackModel implements IFeedBackContract.IFeedBackModel {
    @Override
    public void feedBack(FeedBackReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.feedBack(req, new ApiCallBack<FeedbackBean>() {
            @Override
            public void onSuccess(FeedbackBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
