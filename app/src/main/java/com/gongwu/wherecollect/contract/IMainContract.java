package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;
import com.gongwu.wherecollect.net.entity.response.MsgBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public interface IMainContract {
    interface IMainModel {

        void dealWithShareRequest(String uid, String url, final RequestCallback callback);

    }

    interface IMainPresenter {

        void dealWithShareRequest(String uid, String url);

    }

    interface IMainView extends BaseView {

        void dealWithShareRequestSuccess(RequestSuccessBean data);

    }
}
