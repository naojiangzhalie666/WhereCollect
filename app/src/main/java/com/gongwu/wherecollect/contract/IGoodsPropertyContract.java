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

        void getSubCategoryList(String uid, String parentCode, final RequestCallback callback);

        void getTwoSubCategoryList(String uid, String parentCode, final RequestCallback callback);

        void getThreeSubCategoryList(String uid, String parentCode, final RequestCallback callback);
    }

    interface IGoodsPropertyPresenter {

        void getSubCategoryList(String uid, String parentCode);

        void getTwoSubCategoryList(String uid, String parentCode);

        void getThreeSubCategoryList(String uid, String parentCode);
    }

    interface IGoodsPropertyView extends BaseView {

        void getSubCategoryListSuccess(List<BaseBean> data);

        void getTwoSubCategoryListSuccess(List<BaseBean> data);

        void getThreeSubCategoryListSuccess(List<BaseBean> data);
    }
}
