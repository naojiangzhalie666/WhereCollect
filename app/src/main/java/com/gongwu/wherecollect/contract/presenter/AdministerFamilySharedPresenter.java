package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IAdministerFamilySharedContract;
import com.gongwu.wherecollect.contract.model.AdministerFamilySharedModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.SharedRoomsReq;
import com.gongwu.wherecollect.net.entity.request.SharedUserReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;

import java.util.List;

public class AdministerFamilySharedPresenter extends BasePresenter<IAdministerFamilySharedContract.IAdministerFamilySharedView> implements IAdministerFamilySharedContract.IAdministerFamilySharedPresenter {


    private IAdministerFamilySharedContract.IAdministerFamilySharedModel mModel;

    private AdministerFamilySharedPresenter() {
        mModel = new AdministerFamilySharedModel();
    }

    public static AdministerFamilySharedPresenter getInstance() {
        return new AdministerFamilySharedPresenter();
    }

    @Override
    public void getShareListUserByFamily(String uid, String familyCode) {
        mModel.getShareListUserByFamily(uid, familyCode, new RequestCallback<List<SharedPersonBean>>() {
            @Override
            public void onSuccess(List<SharedPersonBean> bean) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getShareListUserByFamilySuccess(bean);
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
    public void getShareRoomList(String uid, String family_code, String be_shared_user) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getShareRoomList(uid, family_code, be_shared_user, new RequestCallback<List<BaseBean>>() {
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

    @Override
    public void delCollaborator(String uid, String be_shared_user) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.delCollaborator(uid, be_shared_user, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().delCollaboratorSuccess(data);
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
    public void shareOrCancelShareRooms(String uid, SharedUserReq be_shared_user, String family_id, String family_code, List<String> select, List<String> unSelect) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        SharedRoomsReq roomsReq = new SharedRoomsReq();
        roomsReq.setUid(uid);
        roomsReq.setBe_shared_user(be_shared_user);
        roomsReq.setFamily_id(family_id);
        roomsReq.setFamily_code(family_code);
        roomsReq.setShared_rooms(select);
        roomsReq.setUn_shared_rooms(unSelect);
        mModel.shareOrCancelShareRooms(roomsReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().shareOrCancelShareRoomsSuccess(data);
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
