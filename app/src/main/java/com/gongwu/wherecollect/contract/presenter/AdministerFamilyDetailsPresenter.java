package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IAdministerFamilyDetailsContract;
import com.gongwu.wherecollect.contract.model.AdministerFamilyDetailsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.FamilyListDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public class AdministerFamilyDetailsPresenter extends BasePresenter<IAdministerFamilyDetailsContract.IAdministerFamilyDetailsView> implements IAdministerFamilyDetailsContract.IAdministerFamilyDetailsPresenter {

    private static final String TAG = "AdministerFamilyDetailsPresenter";


    private IAdministerFamilyDetailsContract.IAdministerFamilyDetailsModel mModel;

    private AdministerFamilyDetailsPresenter() {
        mModel = new AdministerFamilyDetailsModel();
    }

    public static AdministerFamilyDetailsPresenter getInstance() {
        return new AdministerFamilyDetailsPresenter();
    }

    @Override
    public void getFamilyDetails(String uid, String familyCode) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }

        mModel.getFamilyDetails(uid, familyCode, new RequestCallback<FamilyListDetailsBean>() {
            @Override
            public void onSuccess(FamilyListDetailsBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getFamilyDetailsSuccess(data);
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
    public void delFamily(String uid, String familyCode) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.delFamily(uid, familyCode, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().delFamilySuccess(data);
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
    public void disShareFamily(String uid, String familyCode) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.disShareFamily(uid, familyCode, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().disShareFamilySuccess(data);
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
    public void editFamily(String uid, String familyCode, String familyName) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }

        mModel.editFamily(uid, familyCode, familyName, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().editFamilySuccess(data);
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
