package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IRemindContract;
import com.gongwu.wherecollect.contract.model.RemindModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;

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

    private static class Inner {
        private static final RemindPresenter instance = new RemindPresenter();
    }

}

