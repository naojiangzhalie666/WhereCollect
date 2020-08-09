package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;


public interface IPersonContract {
    interface IPersonModel {
        void editInfo(EditPersonReq req, final RequestCallback callback);
    }

    interface IPersonPresenter {
        void editInfo(String uid, int keyword, String value);
    }

    interface IPersonView extends BaseView {
        void editInfoSuccess(RequestSuccessBean data);
    }
}
