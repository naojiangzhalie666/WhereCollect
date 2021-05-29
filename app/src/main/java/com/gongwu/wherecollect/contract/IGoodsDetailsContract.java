package com.gongwu.wherecollect.contract;


import android.content.Context;

import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;

import java.util.List;


public interface IGoodsDetailsContract {
    interface IGoodsDetailsModel {
        void editGoods(AddGoodsReq req, final RequestCallback callback);

        void removeObjectFromFurnitrue(GoodsDetailsReq req, final RequestCallback callback);

        void delGoods(GoodsDetailsReq req, final RequestCallback callback);

        void getGoodsRemindsById(String uid, String obj_id, final RequestCallback callback);

        void getBuyFirstCategoryList(String uid, final RequestCallback callback);

        void getSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getTwoSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getThreeSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);
    }

    interface IGoodsDetailsPresenter {
        void editGoods(Context context, ObjectBean tempBean, String names, String isbn);

        void removeObjectFromFurnitrue(String uid, String code);

        void delGoods(String uid, String object_id);

        void getGoodsRemindsById(String uid, String obj_id);

        void getBuyFirstCategoryList(String uid);

        void getSubCategoryList(String uid, String parentCode, String type);

        void getTwoSubCategoryList(String uid, String parentCode, String type);

        void getThreeSubCategoryList(String uid, String parentCode, String type);
    }

    interface IGoodsDetailsView extends BaseView {
        /**
         * 编辑物品
         */
        void editGoodsSuccess(ObjectBean data);

        void removeObjectFromFurnitrueSuccess(RequestSuccessBean data);

        void delGoodsSuccess(RequestSuccessBean data);

        void getGoodsRemindsByIdSuccess(List<RemindBean> data);

        void getBuyFirstCategoryListSuccess(List<BaseBean> data);

        void getSubCategoryListSuccess(List<BaseBean> data);

        void getTwoSubCategoryListSuccess(List<BaseBean> data);

        void getThreeSubCategoryListSuccess(List<BaseBean> data);

        /**
         * 上传图片成功
         */
        void onUpLoadSuccess(String path);
    }
}
