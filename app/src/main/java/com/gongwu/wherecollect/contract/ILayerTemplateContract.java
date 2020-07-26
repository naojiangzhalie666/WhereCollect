package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;

public interface ILayerTemplateContract {
    interface ILayerTemplateModel {

        void getTemplateLayerList(String uid, String system_furniture_code, final RequestCallback callback);

        void updataFurniture(EditFurnitureReq furnitureReq, final RequestCallback callback);

    }

    interface ILayerTemplatePresenter {

        void getTemplateLayerList(String uid, String system_furniture_code);

        void updataFurniture(String uid, String familyCode, String code, String layers);

    }

    interface ILayerTemplateView extends BaseView {

        void getTemplateLayerListSuccess(LayerTemplateBean bean);

        void updataFurnitureSuccess(FurnitureBean bean);

    }
}
