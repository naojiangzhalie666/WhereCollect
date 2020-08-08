package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IGoodsDetailsContract;
import com.gongwu.wherecollect.contract.model.GoodsDetailsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;

import java.util.List;

public class GoodsDetailsPresenter extends BasePresenter<IGoodsDetailsContract.IGoodsDetailsView> implements IGoodsDetailsContract.IGoodsDetailsPresenter {

    private static final String TAG = "GoodsDetailsPresenter";

    private IGoodsDetailsContract.IGoodsDetailsModel mModel;

    private GoodsDetailsPresenter() {
        mModel = new GoodsDetailsModel();
    }

    public static GoodsDetailsPresenter getInstance() {
        return GoodsDetailsPresenter.Inner.instance;
    }

    private static class Inner {
        private static final GoodsDetailsPresenter instance = new GoodsDetailsPresenter();
    }

    @Override
    public void removeObjectFromFurnitrue(String uid, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.setUid(uid);
        req.setCode(code);
        mModel.removeObjectFromFurnitrue(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().removeObjectFromFurnitrueSuccess(data);
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
    public void delGoods(String uid, String object_id) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.setUid(uid);
        req.setObject_id(object_id);
        mModel.delGoods(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().delGoodsSuccess(data);
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
    public void getGoodsRemindsById(String uid, String obj_id) {
        mModel.getGoodsRemindsById(uid, obj_id, new RequestCallback<List<RemindBean>>() {
            @Override
            public void onSuccess(List<RemindBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getGoodsRemindsByIdSuccess(data);
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
