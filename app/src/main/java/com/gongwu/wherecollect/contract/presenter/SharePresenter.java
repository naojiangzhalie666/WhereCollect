package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IShareContract;
import com.gongwu.wherecollect.contract.model.ShareModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.ShareReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;

import java.util.List;

public class SharePresenter extends BasePresenter<IShareContract.IShareView> implements IShareContract.ISharePresenter {

    private static final String TAG = "SharePresenter";


    private IShareContract.IShareModel mModel;

    private SharePresenter() {
        mModel = new ShareModel();
    }

    public static SharePresenter getInstance() {
        return new SharePresenter();
    }

    @Override
    public void getSharedUsersList(String uid) {
        ShareReq shareReq = new ShareReq();
        shareReq.setUid(uid);
        mModel.getSharedUsersList(shareReq, new RequestCallback<List<SharedPersonBean>>() {
            @Override
            public void onSuccess(List<SharedPersonBean> data) {
                if (getUIView() != null) {
                    getUIView().getSharedUsersListSuccess(data);
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
    public void getSharedLocations(String uid) {
        ShareReq shareReq = new ShareReq();
        shareReq.setUid(uid);
        mModel.getSharedLocations(shareReq, new RequestCallback<List<SharedLocationBean>>() {
            @Override
            public void onSuccess(List<SharedLocationBean> data) {
                if (getUIView() != null) {
                    getUIView().getSharedLocationsSuccess(data);
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
    public void closeShareUser(String uid, String location_id, String be_shared_user_id, int type) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        ShareReq req = new ShareReq();
        req.setUid(uid);
        req.setLocation_id(location_id);
        req.setBe_shared_user_id(be_shared_user_id);
        req.setType(type);
        mModel.closeShareUser(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().closeShareUserSuccess(data);
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

