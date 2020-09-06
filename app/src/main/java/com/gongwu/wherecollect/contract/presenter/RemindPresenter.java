package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IRemindContract;
import com.gongwu.wherecollect.contract.model.RemindModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddRemindReq;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public class RemindPresenter extends BasePresenter<IRemindContract.IRemindView> implements IRemindContract.IRemindPresenter {

    private static final String TAG = "RemindPresenter";


    private IRemindContract.IRemindModel mModel;

    private RemindPresenter() {
        mModel = new RemindModel();
    }

    public static RemindPresenter getInstance() {
        return Inner.instance;
    }

    @Override
    public void getRemindList(String uid, String done, int page) {
        mModel.getRemindList(uid, done, page, new RequestCallback<RemindListBean>() {
            @Override
            public void onSuccess(RemindListBean data) {
                if (getUIView() != null) {
                    getUIView().getRemindListSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void deteleRemind(String uid, String remind_id) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddRemindReq req = new AddRemindReq();
        req.uid = uid;
        req.remind_id = remind_id;
        mModel.deteleRemind(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().deteleRemindSuccess(data);
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
    public void setRemindDone(String uid, String remind_id) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.setRemindDone(uid, remind_id, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().setRemindDoneSuccess(data);
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

    private static class Inner {
        private static final RemindPresenter instance = new RemindPresenter();
    }

}

