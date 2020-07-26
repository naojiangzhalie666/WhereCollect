package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;

import java.util.List;

public interface IEditHomeContract {
    interface IEditHomeModel {

        void getFamilyRoomList(String uid, String code, final RequestCallback callback);

        void deleteFurniture(EditFurnitureReq furnitureReq, final RequestCallback callback);

        void topFurniture(EditFurnitureReq furnitureReq, final RequestCallback callback);

        void moveFurniture(EditFurnitureReq furnitureReq, final RequestCallback callback);

        void updataFurniture(EditFurnitureReq furnitureReq, final RequestCallback callback);
    }

    interface IEditHomePresenter {

        void getFamilyRoomList(String uid, String code);

        void deleteFurniture(String uid, String roomCode, List<String> furnitureCodes);

        void topFurniture(String uid, List<String> furnitureCodes);

        void moveFurniture(String uid, String family_code, String room_code, String target_family_code, List<String> codes);

        void updataFurniture(String uid, String family_code, FurnitureBean bean);
    }

    interface IEditHomeView extends BaseView {

        void getFamilyRoomListSuccess(List<RoomBean> rooms);

        void deleteFurnitureSuccess(RequestSuccessBean bean);

        void topFurnitureSuccess(RequestSuccessBean bean);

        void moveFurnitureSuccess(RequestSuccessBean bean);

        void updataFurnitureSuccess(FurnitureBean bean);

        void onUpLoadSuccess(String path);
    }
}
