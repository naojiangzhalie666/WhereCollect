package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;

import java.util.List;

public interface IHomeContract {
    interface IHomeModel {

        void getUserFamily(String uid, String user_name, final RequestCallback callback);

        void getUserFamilyRoom(String uid, String code, final RequestCallback callback);
    }

    interface IHomePresenter {
        void getUserFamily(String uid, String user_name);

        void getUserFamilyRoom(String uid, String code);
    }

    interface IHomeView extends BaseView {

        void getUserFamilySuccess(List<FamilyBean> data);

        void getUserFamilyRoomSuccess(HomeFamilyRoomBean data);
    }
}
