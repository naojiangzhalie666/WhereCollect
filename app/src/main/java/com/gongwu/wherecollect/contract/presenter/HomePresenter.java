package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IHomeContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.contract.model.HomeModel;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;

import java.util.List;

public class HomePresenter extends BasePresenter<IHomeContract.IHomeView> implements IHomeContract.IHomePresenter {

    private static final String TAG = "HomePresenter";


    private IHomeContract.IHomeModel mModel;

    private HomePresenter() {
        mModel = new HomeModel();
    }

    public static HomePresenter getInstance() {
        return Inner.instance;
    }

    @Override
    public void getUserFamily(String uid, String user_name) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getUserFamily(uid, user_name, new RequestCallback<List<FamilyBean>>() {
            @Override
            public void onSuccess(List<FamilyBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getUserFamilySuccess(data);
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
    public void getUserFamilyRoom(String uid, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getUserFamilyRoom(uid, code, new RequestCallback<HomeFamilyRoomBean>() {
            @Override
            public void onSuccess(HomeFamilyRoomBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getUserFamilyRoomSuccess(data);
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
        private static final HomePresenter instance = new HomePresenter();
    }

}

