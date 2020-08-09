package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.FeedBackReq;
import com.gongwu.wherecollect.net.entity.response.FeedbackBean;


public interface IFeedBackContract {
    interface IFeedBackModel {
        void feedBack(FeedBackReq req, final RequestCallback callback);
    }

    interface IFeedBackPresenter {
        void feedBack(String uid, String title, String content);
    }

    interface IFeedBackView extends BaseView {
        void feedBackSuccess(FeedbackBean data);
    }
}
