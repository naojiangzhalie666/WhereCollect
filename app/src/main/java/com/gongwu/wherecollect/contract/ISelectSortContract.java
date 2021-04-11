package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.request.EditCustomizeReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public interface ISelectSortContract {
    interface ISelectSortModel {
        void getFirstCategoryList(String uid, final RequestCallback callback);

        void getCategoryDetails(String uid, String code, final RequestCallback callback);

        void saveCustomCate(CustomSubCateReq req, final RequestCallback callback);

        void editCustomizeName(EditCustomizeReq req, final RequestCallback callback);

        void deleteCustomize(EditCustomizeReq req, final RequestCallback callback);
    }

    interface ISelectSortPresenter {
        void getFirstCategoryList(String uid);

        void getCategoryDetails(String uid, String code);

        void saveCustomCate(String uid, String name);

        void editCustomizeName(String uid, String id, String code, String name);

        void deleteCustomize(String uid, String id, String code);
    }

    interface ISelectSortView extends BaseView {

        void getFirstCategoryListSuccess(List<BaseBean> data);

        void getCategoryDetailsSuccess(List<ChannelBean> data);

        void saveCustomCateSuccess(BaseBean bean);

        void editCustomizeNameSuccess(RequestSuccessBean bean);

        void deleteCustomizeSuccess(RequestSuccessBean bean);

    }
}
