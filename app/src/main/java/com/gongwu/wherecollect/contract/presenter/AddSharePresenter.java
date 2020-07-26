package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IAddShareContract;
import com.gongwu.wherecollect.contract.model.AddShareModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddShareReq;
import com.gongwu.wherecollect.net.entity.request.ShareReq;
import com.gongwu.wherecollect.net.entity.request.SharedUserReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;

import java.util.ArrayList;
import java.util.List;

public class AddSharePresenter extends BasePresenter<IAddShareContract.IAddShareView> implements IAddShareContract.IAddSharePresenter {

    private static final String TAG = "SharePresenter";


    private IAddShareContract.IAddShareModel mModel;

    private AddSharePresenter() {
        mModel = new AddShareModel();
    }

    public static AddSharePresenter getInstance() {
        return new AddSharePresenter();
    }

    @Override
    public void getSharePersonOldList(String uid) {
        ShareReq shareReq = new ShareReq();
        shareReq.setUid(uid);
        mModel.getSharePersonOldList(shareReq, new RequestCallback<List<SharedPersonBean>>() {
            @Override
            public void onSuccess(List<SharedPersonBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getSharePersonOldListSuccess(data);
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
    public void getShareUserCodeInfo(String uid, String usid) {
        ShareReq shareReq = new ShareReq();
        shareReq.setUid(uid);
        shareReq.setUsid(usid);
        mModel.getShareUserCodeInfo(shareReq, new RequestCallback<SharedPersonBean>() {
            @Override
            public void onSuccess(SharedPersonBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getShareUserCodeInfoSuccess(data);
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
    public void setShareLocation(String uid, String family_id, String family_code, List<String> room_codes, SharedPersonBean bean) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddShareReq req = new AddShareReq();
        req.setUid(uid);
        req.setFamily_id(family_id);
        req.setFamily_code(family_code);
        req.setRoom_codes(room_codes);
        List<SharedUserReq> mlist = new ArrayList<>();
        SharedUserReq userReq = new SharedUserReq();
        userReq.setUser_id(bean.getId());
        userReq.setValid(bean.isValid());
        mlist.add(userReq);
        req.setBe_shared_users(mlist);
        mModel.setShareLocation(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().setShareLocationSuccess(data);
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
    public void getShareRoomList(String uid, String family_code, String be_shared_user_id) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getShareRoomList(uid, family_code, be_shared_user_id, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getShareRoomListSuccess(data);
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

