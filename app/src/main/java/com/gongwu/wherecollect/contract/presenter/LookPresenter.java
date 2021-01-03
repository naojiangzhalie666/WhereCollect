package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.ILookContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.contract.model.LookModel;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;

import java.util.List;

public class LookPresenter extends BasePresenter<ILookContract.ILookView> implements ILookContract.ILookPresenter {

    private static final String TAG = "LookPresenter";


    private ILookContract.ILookModel mModel;

    private LookPresenter() {
        mModel = new LookModel();
    }

    public static LookPresenter getInstance() {
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
    public void getChangWangList(String uid) {
        mModel.getChangWangList(uid, new RequestCallback<List<ChangWangBean>>() {
            @Override
            public void onSuccess(List<ChangWangBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getChangWangListSuccess(data);
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
    public void getObjectBean(String uid, String family_code) {
        mModel.getObjectBean(uid, family_code, new RequestCallback<List<MainGoodsBean>>() {
            @Override
            public void onSuccess(List<MainGoodsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsListSuccess(data);
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
        private static final LookPresenter instance = new LookPresenter();
    }

}

