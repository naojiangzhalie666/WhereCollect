package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;

import java.util.List;

public interface IAddFurnitureContract {
    interface IAddFurnitureModel {

        void getFurnitureList(AddFurnitureReq req, final RequestCallback callback);

        void addFurniture(AddFurnitureReq req, final RequestCallback callback);

    }

    interface IAddFurniturePresenter {

        void getFurnitureList(String uid, String type, String keyword);

        void addFurniture(String uid, String familyCode, String locationCode, FurnitureBean bean);

    }

    interface IAddFurnitureView extends BaseView {

        void getFurnitureListSuccess(List<FurnitureTemplateBean> bean);

        void addFurnitureSuccess(FurnitureBean bean);

        void onUpLoadSuccess(String path);
    }
}
