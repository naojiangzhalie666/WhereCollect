package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;

import java.util.List;


public interface IGoodsDetailsContract {
    interface IGoodsDetailsModel {
        void removeObjectFromFurnitrue(GoodsDetailsReq req, final RequestCallback callback);

        void delGoods(GoodsDetailsReq req, final RequestCallback callback);

        void getGoodsRemindsById(String uid, String obj_id, final RequestCallback callback);
    }

    interface IGoodsDetailsPresenter {
        void removeObjectFromFurnitrue(String uid, String code);

        void delGoods(String uid, String object_id);

        void getGoodsRemindsById(String uid, String obj_id);
    }

    interface IGoodsDetailsView extends BaseView {
        void removeObjectFromFurnitrueSuccess(RequestSuccessBean data);

        void delGoodsSuccess(RequestSuccessBean data);

        void getGoodsRemindsByIdSuccess(List<RemindBean> data);
    }
}
