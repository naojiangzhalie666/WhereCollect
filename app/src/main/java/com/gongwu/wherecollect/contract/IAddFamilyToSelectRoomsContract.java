package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddFamilyReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;

import java.util.List;

public interface IAddFamilyToSelectRoomsContract {
    interface IAddFamilyToSelectRoomsModel {

        void getRoomsTemplate(AddFamilyReq req, RequestCallback callback);

        void createFamily(AddFamilyReq req, RequestCallback callback);
    }

    interface IAddFamilyToSelectRoomsPresenter {
        void getRoomsTemplate(String uid);

        void createFamily(String uid, String familyName, List<RoomFurnitureBean> beans);
    }

    interface IAddFamilyToSelectRoomsView extends BaseView {

        void getRoomsTemplateSuccess(List<RoomFurnitureBean> mlist);

        void createFamilySuccess(RequestSuccessBean mlist);
    }
}
