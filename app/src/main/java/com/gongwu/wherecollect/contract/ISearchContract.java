package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;

import java.util.List;

public interface ISearchContract {
    interface ILookModel {
        void searchObject(String uid, String keyword, final RequestCallback callback);
    }

    interface ILookPresenter {
        void searchObject(String uid, String keyword);
    }

    interface ILookView extends BaseView {
        void searchObjectSuccess(List<FamilyBean> data);
    }
}
