package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;

public interface IRemindContract {
    interface IRemindModel {

        void getRemindList(String uid, String done, int page, final RequestCallback callback);
    }

    interface IRemindPresenter {
        void getRemindList(String uid, String done, int current_page);
    }

    interface IRemindView extends BaseView {

        void getRemindListSuccess(RemindListBean data);
    }
}
