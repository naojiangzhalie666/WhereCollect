package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.BindAppReq;
import com.gongwu.wherecollect.net.entity.request.EditPersonReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;


public interface IPersonContract {
    interface IPersonModel {
        void getUserInfo(String uid, final RequestCallback callback);

        void editInfo(EditPersonReq req, final RequestCallback callback);

        void bindCheck(String uid, String openid, String type, final RequestCallback callback);

        void bindAccount(BindAppReq req, final RequestCallback callback);
    }

    interface IPersonPresenter {
        void getUserInfo(String uid);

        void editInfo(String uid, int keyword, String value);

        void bindCheck(String uid, String openid, String type, BindAppReq req);

        void bindAccount(BindAppReq req);
    }

    interface IPersonView extends BaseView {
        void getUserInfoSuccess(UserBean data);

        void editInfoSuccess(RequestSuccessBean data);

        void bindCheckSuccess(RequestSuccessBean data, BindAppReq req);

        void bindAccountSuccess(RequestSuccessBean data);
    }
}
