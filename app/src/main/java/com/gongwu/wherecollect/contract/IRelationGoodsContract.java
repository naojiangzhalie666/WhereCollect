package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RelationGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;

import java.util.List;

public interface IRelationGoodsContract {
    interface IRelationGoodsModel {

        void getRelationGoodsList(String uid, String category_code, String keyword, int page, final RequestCallback callback);

        void getRelationCategories(String uid, final RequestCallback callback);
    }

    interface IRelationGoodsPresenter {
        void getRelationGoodsList(String uid, String category_code, String keyword, int page);

        void getRelationCategories(String uid);
    }

    interface IRelationGoodsView extends BaseView {

        void getRelationGoodsSuccess(List<ObjectBean> data);

        void getRelationCategoriesSuccess(List<BaseBean> data);
    }
}
