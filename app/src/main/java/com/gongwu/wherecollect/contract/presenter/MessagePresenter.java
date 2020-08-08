package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IMessageContract;
import com.gongwu.wherecollect.contract.model.MessageModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.MsgReq;
import com.gongwu.wherecollect.net.entity.response.MessagePostBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public class MessagePresenter extends BasePresenter<IMessageContract.IMessageView> implements IMessageContract.IMessagePresenter {

    private static final String TAG = "SearchPresenter";

    private IMessageContract.IMessageModel mModel;

    private MessagePresenter() {
        mModel = new MessageModel();
    }

    public static MessagePresenter getInstance() {
        return MessagePresenter.Inner.instance;
    }

    private static class Inner {
        private static final MessagePresenter instance = new MessagePresenter();
    }

    @Override
    public void getMessagesList(String uid, int page) {
        mModel.getMessagesList(new MsgReq(uid, page), new RequestCallback<MessagePostBean>() {
            @Override
            public void onSuccess(MessagePostBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getMessagesListSuccess(data.getItems() != null ? data.getItems() : null);
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

    @Override
    public void dealWithShareRequest(String uid, String url) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.dealWithShareRequest(uid, url, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().dealWithShareRequestSuccess(data);
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
