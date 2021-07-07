package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.MsgReq;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.MessageBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;

import java.util.List;


public interface IMessageContract {
    interface IMessageModel {
        void getMessagesList(String uid, int page, String content_type, final RequestCallback callback);

        void dealWithShareRequest(String uid, String url, final RequestCallback callback);
    }

    interface IMessagePresenter {
        void getMessagesList(String uid, int page, String msgType);

        void dealWithShareRequest(String uid, String url);
    }

    interface IMessageView extends BaseView {
        void getMessagesListSuccess(List<MessageBean> data);

        void dealWithShareRequestSuccess(RequestSuccessBean data);
    }
}
