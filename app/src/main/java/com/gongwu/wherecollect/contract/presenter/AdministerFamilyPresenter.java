package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IAdministerFamilyContract;
import com.gongwu.wherecollect.contract.model.AdministerFamilyModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;

public class AdministerFamilyPresenter extends BasePresenter<IAdministerFamilyContract.IAdministerFamilyView> implements IAdministerFamilyContract.IAdministerFamilyPresenter {

    private static final String TAG = "AdministerFamilyPresenter";


    private IAdministerFamilyContract.IAdministerFamilyModel mModel;

    private AdministerFamilyPresenter() {
        mModel = new AdministerFamilyModel();
    }

    public static AdministerFamilyPresenter getInstance() {
        return new AdministerFamilyPresenter();
    }

    @Override
    public void getFamilyList(String uid) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }

        mModel.getFamilyList(uid, new RequestCallback<MyFamilyListBean>() {
            @Override
            public void onSuccess(MyFamilyListBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getFamilyListSuccess(data);
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
