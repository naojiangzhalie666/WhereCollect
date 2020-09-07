package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public interface IEditFurniturePatternContract {
    interface IEditFurniturePatternModel {

        void updataFurniture(EditFurnitureReq furnitureReq, final RequestCallback callback);

    }

    interface IEditFurniturePatternPresenter {

        void updataFurniture(String uid, String family_code, String code, String layers, float ratio);

    }

    interface IEditFurniturePatternView extends BaseView {

        void updataFurnitureSuccess(FurnitureBean data);

    }
}
