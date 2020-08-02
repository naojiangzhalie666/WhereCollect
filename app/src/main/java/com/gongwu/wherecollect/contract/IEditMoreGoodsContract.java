package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.EditMoreGoodsReq;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public interface IEditMoreGoodsContract {
    interface IEditMoreGoodsModel {
        void getEditMoreGoodsList(String uid, String family_code, String category_code, final RequestCallback callback);

        void delSelectGoods(EditGoodsReq req, final RequestCallback callback);

        void objectsAddCategory(EditMoreGoodsReq req, final RequestCallback callback);
    }

    interface IEditMoreGoodsPresenter {
        void getEditMoreGoodsList(String uid, String family_code);

        void delSelectGoods(String uid, String ids);

        void objectsAddCategory(String uid, List<String> object_ids, List<String> category_codes);
    }

    interface IEditMoreGoodsView extends BaseView {
        void getEditMoreGoodsListSuccess(List<ObjectBean> data);

        void delSelectGoodsSuccess(RequestSuccessBean bean);

        void objectsAddCategorySuccess(RequestSuccessBean bean);
    }
}
