package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.UserBean;

public interface IPersonContract {
    interface IPersonModel {

        void logout(final RequestCallback callback);
    }

    interface IPersonPresenter {
        void logout();
    }

    interface IPersonView extends BaseView {

        void logoutSuccess(UserBean data);
    }
}
