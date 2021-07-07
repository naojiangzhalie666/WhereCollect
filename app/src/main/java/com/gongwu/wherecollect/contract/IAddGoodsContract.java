package com.gongwu.wherecollect.contract;


import android.content.Context;

import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.BarcodeBean;
import com.gongwu.wherecollect.net.entity.request.AddGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.request.UserReq;
import com.gongwu.wherecollect.net.entity.response.BarcodeResultBean;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;

import java.io.File;
import java.util.List;
import java.util.Map;

public interface IAddGoodsContract {
    interface IAddGoodsModel {

        void editGoods(AddGoodsReq req, final RequestCallback callback);

        void addObjects(AddGoodsReq req, final RequestCallback callback);

        void addMoreGoods(AddGoodsReq req, final RequestCallback callback);

        void getBookInfo(AddGoodsReq req, final RequestCallback callback);

        void getTaobaoInfo(AddGoodsReq req, final RequestCallback callback);

        void getGoodsByBarcode(BarcodeBean barcodeBean, final RequestCallback callback);

        void getGoodsByTBbarcode(BarcodeBean barcodeBean, final RequestCallback callback);

        void removeObjectFromFurnitrue(GoodsDetailsReq req, final RequestCallback callback);

        void getBelongerList(String uid, final RequestCallback callback);

        void getBuyFirstCategoryList(String uid, final RequestCallback callback);

        void getSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getTwoSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);

        void getThreeSubCategoryList(String uid, String parentCode, String type, final RequestCallback callback);
    }

    interface IAddGoodsPresenter {
        void editGoods(Context context, ObjectBean tempBean, String names, String isbn);

        void addObjects(Context context, ObjectBean tempBean, String names, String isbn);

        void addMoreGoods(Context mContext, List<ObjectBean> mlist, ObjectBean common, RoomFurnitureBean location);

        void getBookInfo(String uid, String isbn);

        void getTaobaoInfo(String uid, String key);

        void getGoodsByBarcode(String uid, String key, String type);

        void getGoodsByTBbarcode(String uid, String tkey);

        void removeObjectFromFurnitrue(String uid, String code);

        void getBelongerList(String uid);

        void getBuyFirstCategoryList(String uid);

        void getSubCategoryList(String uid, String parentCode, String type);

        void getTwoSubCategoryList(String uid, String parentCode, String type);

        void getThreeSubCategoryList(String uid, String parentCode, String type);
    }

    interface IAddGoodsView extends BaseView {
        /**
         * 编辑物品
         */
        void editGoodsSuccess(ObjectBean data);

        /**
         * 添加物品
         */
        void addObjectsSuccess(List<ObjectBean> data);

        /**
         * 添加多个物品
         */
        void addMoreGoodsSuccess(List<ObjectBean> data);

        /**
         * 扫码获取物品信息
         */
        void getBookInfoSuccess(BookBean data);

        /**
         * 扫码获取物品信息
         */
        void getTaobaoInfoSuccess(BookBean data);

        /**
         * 物品及图书扫码
         */
        void getGoodsByBarcodeSuccess(BarcodeResultBean data);

        /**
         * 物淘口令
         */
        void getGoodsByTBbarcodeSuccess(BarcodeResultBean data);

        /**
         * 上传图片成功
         */
        void onUpLoadSuccess(String path);

        /**
         * 网络获取扫码物品信息，下载图片成功后回调
         */
        void updateBeanWithBook(BookBean bookBean);

        void removeObjectFromFurnitrueSuccess(RequestSuccessBean data);

        void getBelongerListSuccess(List<BaseBean> data);

        void getBuyFirstCategoryListSuccess(List<BaseBean> data);

        void getSubCategoryListSuccess(List<BaseBean> data);

        void getTwoSubCategoryListSuccess(List<BaseBean> data);

        void getThreeSubCategoryListSuccess(List<BaseBean> data);
    }
}
