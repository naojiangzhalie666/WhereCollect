package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.request.EditRoomReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;

import java.util.List;

public interface IEditRoomContract {
    interface IEditRoomModel {

        void updataRoomPosition(EditRoomReq roomReq, final RequestCallback callback);

        void addRoom(EditRoomReq roomReq, final RequestCallback callback);

        void delRoom(EditRoomReq roomReq, final RequestCallback callback);

        void editRoom(EditRoomReq roomReq, final RequestCallback callback);

        void moveRoom(EditRoomReq roomReq, final RequestCallback callback);
    }

    interface IEditRoomPresenter {

        void updataRoomPosition(String uid, String location_codes);

        void addRoom(String uid, String location_code, String location_name);

        void delRoom(String uid, String location_code);

        void editRoom(String uid, String location_code, String location_name);

        void moveRoom(String uid, String old_family_code, String new_family_code, String new_family_name, List<String> rooms_code);
    }

    interface IEditRoomView extends BaseView {

        void updataRoomPositionSuccess(List<RoomBean> rooms);

        void addRoomSuccess(RoomBean rooms);

        void delRoomSuccess(RequestSuccessBean rooms);

        void editRoomSuccess(RequestSuccessBean rooms);

        void moveRoomSuccess(RequestSuccessBean rooms);


    }
}
