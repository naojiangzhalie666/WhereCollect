package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;


public interface ISearchContract {
    interface ISearchModel {
        void getSearchList(SearchReq req, final RequestCallback callback);
    }

    interface ISearchPresenter {
        void getSearchList(String uid, String keyword);
    }

    interface ISearchView extends BaseView {
        void getSearchListSuccess(SerchListBean data);
    }
}
