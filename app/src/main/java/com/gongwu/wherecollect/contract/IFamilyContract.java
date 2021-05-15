package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.FurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;

import java.util.List;

public interface IFamilyContract {
    interface IFamilyModel {
        void getFurnitureList(FurnitureReq req, final RequestCallback callback);
    }

    interface IFamilyPresenter {
        void getFurnitureList(String uid, String location_code, String familyCode, boolean isShowLoading);
    }

    interface IFamilyView extends BaseView {

        void getFurnitureListSuccess(List<FurnitureBean> data);
    }
}
