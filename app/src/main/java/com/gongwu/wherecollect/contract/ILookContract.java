package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public interface ILookContract {
    interface ILookModel {
        void getUserFamily(String uid, String user_name, final RequestCallback callback);

        void getChangWangList(String uid, final RequestCallback callback);

        void getObjectBean(String uid, String family_code, boolean darklayer, final RequestCallback callback);

        void delSelectGoods(EditGoodsReq req, final RequestCallback callback);

        void setGoodsWeight(EditGoodsReq req, final RequestCallback callback);

        void goodsArchive(GoodsDetailsReq req, final RequestCallback callback);

        void removeArchiveObjects(GoodsDetailsReq req, final RequestCallback callback);
    }

    interface ILookPresenter {
        void getUserFamily(String uid, String user_name);

        void getChangWangList(String uid);

        void getObjectBean(String uid, String family_code, boolean darklayer);

        void delSelectGoods(String uid, String ids);

        void setGoodsWeight(String uid, String goodsId);

        void goodsArchive(String uid, String goodsId);

        void removeArchiveObjects(String uid);
    }

    interface ILookView extends BaseView {
        void getUserFamilySuccess(List<FamilyBean> data);

        void getChangWangListSuccess(List<ChangWangBean> data);

        void getGoodsListSuccess(List<MainGoodsBean> data);

        void delSelectGoodsSuccess(RequestSuccessBean bean);

        void setGoodsWeightSuccess(RequestSuccessBean bean);

        void goodsArchiveSuccess(RequestSuccessBean bean);

        void removeArchiveObjectsSuccess(RequestSuccessBean bean);
    }
}
