package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;
import com.gongwu.wherecollect.net.entity.response.MsgBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.net.entity.response.VersionBean;

import java.util.List;

public interface IMainContract {
    interface IMainModel {

        void dealWithShareRequest(String uid, String url, final RequestCallback callback);

        void getVersion(String app_version, final RequestCallback callback);

        void getUserInfo(String uid, final RequestCallback callback);

        void getEnergyCode(String uid, String code, final RequestCallback callback);
    }

    interface IMainPresenter {

        void dealWithShareRequest(String uid, String url);

        void getVersion(String app_version);

        void getUserInfo(String uid);

        void getEnergyCode(String uid, String code);
    }

    interface IMainView extends BaseView {
        void getUserInfoSuccess(UserBean data);

        void getVersionSuccess(VersionBean bean);

        void dealWithShareRequestSuccess(RequestSuccessBean data);

        void getEnergyCodeSuccess(RequestSuccessBean data);
    }
}
