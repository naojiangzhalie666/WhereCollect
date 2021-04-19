package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;

import java.util.List;

public interface IArticleContract {
    interface IArticleModel {

        void getArticList(final RequestCallback callback);

    }

    interface IArticlePresenter {
        void getArticList();
    }

    interface IArticleView extends BaseView {

        void getArticListSuccess(List<ArticleBean> data);
    }
}
