package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;

import java.util.List;

public interface ISelectSortContract {
    interface ISelectSortModel {
        void getFirstCategoryList(String uid, final RequestCallback callback);

        void getCategoryDetails(String uid, String code, final RequestCallback callback);
    }

    interface ISelectSortPresenter {
        void getFirstCategoryList(String uid);

        void getCategoryDetails(String uid, String code);
    }

    interface ISelectSortView extends BaseView {

        void getFirstCategoryListSuccess(List<BaseBean> data);

        void getCategoryDetailsSuccess(List<ChannelBean> data);

    }
}
