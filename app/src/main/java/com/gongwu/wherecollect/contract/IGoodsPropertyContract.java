package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.request.EditCustomizeReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public interface IGoodsPropertyContract {
    interface IGoodsPropertyModel {
        void getBuyFirstCategoryList(String uid, final RequestCallback callback);

        void getSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getTwoSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getThreeSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);
    }

    interface IGoodsPropertyPresenter {
        void getBuyFirstCategoryList(String uid);

        void getSubCategoryList(String uid, String parentCode, String type);

        void getTwoSubCategoryList(String uid, String parentCode, String type);

        void getThreeSubCategoryList(String uid, String parentCode, String type);
    }

    interface IGoodsPropertyView extends BaseView {

        void getBuyFirstCategoryListSuccess(List<BaseBean> data);

        void getSubCategoryListSuccess(List<BaseBean> data);

        void getTwoSubCategoryListSuccess(List<BaseBean> data);

        void getThreeSubCategoryListSuccess(List<BaseBean> data);
    }
}
