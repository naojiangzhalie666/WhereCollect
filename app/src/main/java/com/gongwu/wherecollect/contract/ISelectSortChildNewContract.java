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
        void getBelongerList(String uid, final RequestCallback callback);

        void getBuyFirstCategoryList(String uid, final RequestCallback callback);

        void getSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getTwoSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getThreeSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void saveCustomSubCate(CustomSubCateReq req, final RequestCallback callback);

        void saveCustomCate(CustomSubCateReq req, final RequestCallback callback);

        void saveBelonger(CustomSubCateReq req, final RequestCallback callback);

        void deleteCustomize(EditCustomizeReq req, final RequestCallback callback);

        void deleteBelonger(EditCustomizeReq req, final RequestCallback callback);
    }

    interface ISelectSortChildNewPresenter {
        void getBelongerList(String uid);

        void getBuyFirstCategoryList(String uid);

        void getSubCategoryList(String uid, String parentCode, String type);

        void getTwoSubCategoryList(String uid, String parentCode, String type);

        void getThreeSubCategoryList(String uid, String parentCode, String type);

        void saveCustomSubCate(String uid, String name, String parentCode, String type);

        void saveCustomCate(String uid, String name, String type);

        void saveBelonger(String uid, String name);

        void deleteCustomize(String uid, String id, String code, String type);

        void deleteBelonger(String uid, String id);
    }

    interface ISelectSortChildNewView extends BaseView {
        void getBelongerListSuccess(List<BaseBean> data);

        void getBuyFirstCategoryListSuccess(List<BaseBean> data);

        void getSubCategoryListSuccess(List<BaseBean> data);

        void getTwoSubCategoryListSuccess(List<BaseBean> data);

        void getThreeSubCategoryListSuccess(List<BaseBean> data);

        void saveCustomSubCateSuccess(BaseBean bean);

        void saveBelongerSuccess(BaseBean bean);

        void deleteCustomizeSuccess(RequestSuccessBean bean);

        void deleteBelongerSuccess(RequestSuccessBean bean);
    }
}
