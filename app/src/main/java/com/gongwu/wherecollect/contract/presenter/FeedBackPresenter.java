package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IFeedBackContract;
import com.gongwu.wherecollect.contract.model.FeedBackModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.FeedBackReq;
import com.gongwu.wherecollect.net.entity.response.FeedbackBean;

public class FeedBackPresenter extends BasePresenter<IFeedBackContract.IFeedBackView> implements IFeedBackContract.IFeedBackPresenter {
    private static final String TAG = "FeedBackPresenter";

    private IFeedBackContract.IFeedBackModel mModel;

    private FeedBackPresenter() {
        mModel = new FeedBackModel();
    }

    public static FeedBackPresenter getInstance() {
        return FeedBackPresenter.Inner.instance;
    }

    private static class Inner {
        private static final FeedBackPresenter instance = new FeedBackPresenter();
    }


    @Override
    public void feedBack(String uid, String title, String content) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        FeedBackReq req = new FeedBackReq();
        req.setUid(uid);
        req.setTitle(title);
        req.setContent(content);
        mModel.feedBack(req, new RequestCallback<FeedbackBean>() {
            @Override
            public void onSuccess(FeedbackBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().feedBackSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }
}
