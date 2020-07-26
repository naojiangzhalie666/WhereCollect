package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IFamilyContract;
import com.gongwu.wherecollect.contract.IRemindContract;
import com.gongwu.wherecollect.contract.model.FamilyModel;
import com.gongwu.wherecollect.contract.model.RemindModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.FurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.Lg;

import java.util.List;

public class FamilyPresenter extends BasePresenter<IFamilyContract.IFamilyView> implements IFamilyContract.IFamilyPresenter {

    private static final String TAG = "FamilyPresenter";


    private IFamilyContract.IFamilyModel mModel;

    private FamilyPresenter() {
        mModel = new FamilyModel();
    }

    public static FamilyPresenter getInstance() {
        return new FamilyPresenter();
    }


    @Override
    public void getFurnitureList(String uid, String location_code, String familyCode) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        FurnitureReq req = new FurnitureReq();
        req.setUid(uid);
        req.setLocation_code(location_code);
        req.setFamily_code(familyCode);
        mModel.getFurnitureList(req, new RequestCallback<List<FurnitureBean>>() {
            @Override
            public void onSuccess(List<FurnitureBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getFurnitureListSuccess(data);
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
        private static final FamilyPresenter instance = new FamilyPresenter();
    }

}

