package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IAddChangWangGoodsContract;
import com.gongwu.wherecollect.contract.model.AddChangWangGoodsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddChangWangGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ChangWangDetailBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangListBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;

import java.util.List;

public class AddChangWangGoodsPresenter extends BasePresenter<IAddChangWangGoodsContract.IAddChangWangGoodsView> implements IAddChangWangGoodsContract.IAddChangWangGoodsPresenter {

    private static class Inner {
        private static final AddChangWangGoodsPresenter instance = new AddChangWangGoodsPresenter();
    }

    private IAddChangWangGoodsContract.IAddChangWangGoodsModel mModel;

    private AddChangWangGoodsPresenter() {
        mModel = new AddChangWangGoodsModel();
    }

    public static AddChangWangGoodsPresenter getInstance() {
        return AddChangWangGoodsPresenter.Inner.instance;
    }

    @Override
    public void getChangWangGoodsList(String uid, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddChangWangGoodsReq req = new AddChangWangGoodsReq();
        req.setUid(uid);
        req.setCode(code);
        mModel.getChangWangGoodsList(req, new RequestCallback<ChangWangListBean>() {

            @Override
            public void onSuccess(ChangWangListBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getChangWangGoodsListSuccess(data);
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
    public void setChangWangDetail(String uid, String object_id, String option) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddChangWangGoodsReq req = new AddChangWangGoodsReq();
        req.setUid(uid);
        req.setObject_id(object_id);
        req.setOption(option);
        mModel.setChangWangDetail(req, new RequestCallback<ChangWangDetailBean>() {

            @Override
            public void onSuccess(ChangWangDetailBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().setChangWangDetailSuccess(data);
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
