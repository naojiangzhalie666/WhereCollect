package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;

public interface IImportMoreGoodsContract {
    interface IImportMoreGoodsModel {
        void getImportGoodsList(EditGoodsReq req, final RequestCallback callback);
    }

    interface IImportMoreGoodsPresenter {
        void getImportGoodsList(String uid, String location_code);
    }

    interface IImportMoreGoodsView extends BaseView {

        void getImportGoodsListSuccess(ImportGoodsBean bean);
    }
}
