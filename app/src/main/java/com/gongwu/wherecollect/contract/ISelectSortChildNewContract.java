package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.request.EditCustomizeReq;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public interface ISelectSortChildNewContract {
    interface ISelectSortChildNewModel {

        void getSubCategoryList(String uid, String parentCode, final RequestCallback callback);

        void getTwoSubCategoryList(String uid, String parentCode, final RequestCallback callback);

        void getThreeSubCategoryList(String uid, String parentCode, final RequestCallback callback);

        void saveCustomSubCate(CustomSubCateReq req, final RequestCallback callback);

        void deleteCustomize(EditCustomizeReq req, final RequestCallback callback);
    }

    interface ISelectSortChildNewPresenter {

        void getSubCategoryList(String uid, String parentCode);

        void getTwoSubCategoryList(String uid, String parentCode);

        void getThreeSubCategoryList(String uid, String parentCode);

        void saveCustomSubCate(String uid, String name, String parentCode);

        void deleteCustomize(String uid, String id, String code);
    }

    interface ISelectSortChildNewView extends BaseView {

        void getSubCategoryListSuccess(List<BaseBean> data);

        void getTwoSubCategoryListSuccess(List<BaseBean> data);

        void getThreeSubCategoryListSuccess(List<BaseBean> data);

        void saveCustomSubCateSuccess(BaseBean bean);

        void deleteCustomizeSuccess(RequestSuccessBean bean);
    }
}
