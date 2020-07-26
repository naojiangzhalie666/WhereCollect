package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IEditRemindContract;
import com.gongwu.wherecollect.contract.model.EditRemindModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddRemindReq;
import com.gongwu.wherecollect.net.entity.response.RemindDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;


public class EditRemindPresenter extends BasePresenter<IEditRemindContract.IEditRemindView> implements IEditRemindContract.IEditRemindPresenter {

    @Override
    public void addRemind(String uid, String title, String description, String tips_time, String first,
                          String repeat, String associated_object_id, String associated_object_url, String device_token) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddRemindReq req = new AddRemindReq();
        req.uid = uid;
        req.title = title;
        req.description = description;
        req.tips_time = tips_time;
        req.first = first;
        req.repeat = repeat;
        req.associated_object_id = associated_object_id;
        req.associated_object_url = associated_object_url;
        req.device_token = device_token;
        mModel.addRemind(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().addRemindSuccess(data);
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
    public void updateRemind(String uid, String title, String description, String tips_time, String first, String repeat, String associated_object_id, String associated_object_url, String device_token, String remind_id) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddRemindReq req = new AddRemindReq();
        req.uid = uid;
        req.title = title;
        req.description = description;
        req.tips_time = tips_time;
        req.first = first;
        req.repeat = repeat;
        req.associated_object_id = associated_object_id;
        req.associated_object_url = associated_object_url;
        req.device_token = device_token;
        req.remind_id = remind_id;
        mModel.updateRemind(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().updateRemindSuccess(data);
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

    @Override
    public void getRemindDetails(String uid, String remind_id, String associatedObjectId) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getRemindDetails(uid, remind_id, associatedObjectId, new RequestCallback<RemindDetailsBean>() {
            @Override
            public void onSuccess(RemindDetailsBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getRemindDetailsSuccess(data);
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

    private IEditRemindContract.IEditRemindModel mModel;

    private EditRemindPresenter() {
        mModel = new EditRemindModel();
    }

    public static EditRemindPresenter getInstance() {
        return Inner.instance;
    }

    private static class Inner {
        private static final EditRemindPresenter instance = new EditRemindPresenter();
    }
}
