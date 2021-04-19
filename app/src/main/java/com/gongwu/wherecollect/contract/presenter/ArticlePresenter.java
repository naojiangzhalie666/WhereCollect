package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IArticleContract;
import com.gongwu.wherecollect.contract.model.ArticleModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;

import java.util.List;

public class ArticlePresenter extends BasePresenter<IArticleContract.IArticleView> implements IArticleContract.IArticlePresenter {
    private IArticleContract.IArticleModel mModel;

    private ArticlePresenter() {
        mModel = new ArticleModel();
    }

    public static ArticlePresenter getInstance() {
        return new ArticlePresenter();
    }

    @Override
    public void getArticList() {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getArticList(new RequestCallback<List<ArticleBean>>() {
            @Override
            public void onSuccess(List<ArticleBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getArticListSuccess(data);
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
