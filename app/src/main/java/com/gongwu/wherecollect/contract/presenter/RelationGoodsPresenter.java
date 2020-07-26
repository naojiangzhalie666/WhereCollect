package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IRelationGoodsContract;
import com.gongwu.wherecollect.contract.model.RelationGoodsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RelationGoodsBean;

import java.util.List;

public class RelationGoodsPresenter extends BasePresenter<IRelationGoodsContract.IRelationGoodsView> implements IRelationGoodsContract.IRelationGoodsPresenter {

    private IRelationGoodsContract.IRelationGoodsModel mModel;

    public RelationGoodsPresenter() {
        mModel = new RelationGoodsModel();
    }

    @Override
    public void getRelationGoodsList(String uid, String category_code, String keyword, int page) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getRelationGoodsList(uid, category_code, keyword, page, new RequestCallback<RelationGoodsBean>() {
            @Override
            public void onSuccess(RelationGoodsBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getRelationGoodsSuccess(data.items);
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
    public void getRelationCategories(String uid) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getRelationCategories(uid, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getRelationCategoriesSuccess(data);
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
