package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddRemindReq;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public interface IRemindContract {
    interface IRemindModel {

        void getRemindList(String uid, String done, int page, final RequestCallback callback);

        void setRemindDone(String uid, String remind_id, final RequestCallback callback);

        void deteleRemind(AddRemindReq req, final RequestCallback callback);
    }

    interface IRemindPresenter {
        void getRemindList(String uid, String done, int current_page);

        void deteleRemind(String uid, String remind_id);

        void setRemindDone(String uid, String remind_id);
    }

    interface IRemindView extends BaseView {

        void getRemindListSuccess(RemindListBean data);

        void deteleRemindSuccess(RequestSuccessBean data);

        void setRemindDoneSuccess(RequestSuccessBean data);

    }
}
