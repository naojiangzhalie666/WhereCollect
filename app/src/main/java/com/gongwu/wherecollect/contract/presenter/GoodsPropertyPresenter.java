package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IGoodsPropertyContract;
import com.gongwu.wherecollect.contract.ISelectSortChildNewContract;
import com.gongwu.wherecollect.contract.model.GoodsPropertyModel;
import com.gongwu.wherecollect.contract.model.SelectSortChildNewModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.request.EditCustomizeReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public class GoodsPropertyPresenter extends BasePresenter<IGoodsPropertyContract.IGoodsPropertyView> implements IGoodsPropertyContract.IGoodsPropertyPresenter {
    private IGoodsPropertyContract.IGoodsPropertyModel mModel;

    private GoodsPropertyPresenter() {
        mModel = new GoodsPropertyModel();
    }

    public static GoodsPropertyPresenter getInstance() {
        return new GoodsPropertyPresenter();
    }


    @Override
    public void getSubCategoryList(String uid, String parentCode) {
        mModel.getSubCategoryList(uid, parentCode, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getSubCategoryListSuccess(data);
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
    public void getTwoSubCategoryList(String uid, String parentCode) {
        mModel.getTwoSubCategoryList(uid, parentCode, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getTwoSubCategoryListSuccess(data);
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
    public void getThreeSubCategoryList(String uid, String parentCode) {
        mModel.getThreeSubCategoryList(uid, parentCode, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getThreeSubCategoryListSuccess(data);
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
