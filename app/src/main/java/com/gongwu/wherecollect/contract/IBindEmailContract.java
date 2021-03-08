package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.BindEmailReq;
import com.gongwu.wherecollect.net.entity.request.BindPhoneReq;
import com.gongwu.wherecollect.net.entity.response.BuyVIPResultBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.net.entity.response.VIPBean;


public interface IBindEmailContract {
    interface IBindEmailModel {
        void bindEmail(BindEmailReq req, final RequestCallback callback);
    }

    interface IBindEmailPresenter {
        void bindEmail(String uid, String email, String password);
    }

    interface IBindEmailView extends BaseView {
        void bindEmailSuccess(RequestSuccessBean data);
    }
}
