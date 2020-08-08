package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.ISearchContract;
import com.gongwu.wherecollect.contract.model.SearchModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;

public class SearchPresenter extends BasePresenter<ISearchContract.ISearchView> implements ISearchContract.ISearchPresenter {
    private static final String TAG = "SearchPresenter";

    private ISearchContract.ISearchModel mModel;

    private SearchPresenter() {
        mModel = new SearchModel();
    }

    public static SearchPresenter getInstance() {
        return SearchPresenter.Inner.instance;
    }

    private static class Inner {
        private static final SearchPresenter instance = new SearchPresenter();
    }

    @Override
    public void getSearchList(String uid, String keyword) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        SearchReq req = new SearchReq();
        req.setUid(uid);
        req.setKeyword(keyword);
        mModel.getSearchList(req, new RequestCallback<SerchListBean>() {
            @Override
            public void onSuccess(SerchListBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getSearchListSuccess(data);
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
