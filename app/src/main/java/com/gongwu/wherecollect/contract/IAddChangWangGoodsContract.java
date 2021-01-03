package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddChangWangGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ChangWangDetailBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangListBean;


public interface IAddChangWangGoodsContract {
    interface IAddChangWangGoodsModel {
        void getChangWangGoodsList(AddChangWangGoodsReq req, final RequestCallback callback);

        void setChangWangDetail(AddChangWangGoodsReq req, final RequestCallback callback);
    }

    interface IAddChangWangGoodsPresenter {
        void getChangWangGoodsList(String uid, String code);

        void setChangWangDetail(String uid, String object_id, String option);
    }

    interface IAddChangWangGoodsView extends BaseView {
        void getChangWangGoodsListSuccess(ChangWangListBean data);

        void setChangWangDetailSuccess(ChangWangDetailBean data);
    }
}
