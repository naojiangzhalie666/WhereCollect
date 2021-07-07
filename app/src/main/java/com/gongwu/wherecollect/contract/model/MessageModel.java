package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IMessageContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.MsgReq;
import com.gongwu.wherecollect.net.entity.response.MessagePostBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.util.ApiUtils;


public class MessageModel implements IMessageContract.IMessageModel {
    @Override
    public void getMessagesList(String uid, int page, String content_type, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getMessagesList(uid, page, content_type, new ApiCallBack<MessagePostBean>() {
            @Override
            public void onSuccess(MessagePostBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void dealWithShareRequest(String uid, String url, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.dealWithShareRequest(uid, url, new ApiCallBack<RequestSuccessBean>() {
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
