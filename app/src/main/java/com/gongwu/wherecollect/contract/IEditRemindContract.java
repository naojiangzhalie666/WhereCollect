package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddRemindReq;
import com.gongwu.wherecollect.net.entity.response.RemindDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public interface IEditRemindContract {
    interface IEditRemindModel {

        void addRemind(AddRemindReq req, final RequestCallback callback);

        void updateRemind(AddRemindReq req, final RequestCallback callback);

        void deteleRemind(AddRemindReq req, final RequestCallback callback);

        void setRemindDone(String uid, String remind_id, final RequestCallback callback);

        void getRemindDetails(String uid, String remind_id, String associatedObjectId, final RequestCallback callback);
    }

    interface IEditRemindPresenter {
        void addRemind(String uid, String title, String description,
                       String tips_time, String first, String repeat,
                       String associated_object_id, String associated_object_url,
                       String device_token);

        void updateRemind(String uid, String title, String description,
                          String tips_time, String first, String repeat,
                          String associated_object_id, String associated_object_url,
                          String device_token, String remind_id);

        void deteleRemind(String uid, String remind_id);

        void setRemindDone(String uid, String remind_id);

        void getRemindDetails(String uid, String remind_id, String associatedObjectId);
    }

    interface IEditRemindView extends BaseView {

        void addRemindSuccess(RequestSuccessBean data);

        void updateRemindSuccess(RequestSuccessBean data);

        void deteleRemindSuccess(RequestSuccessBean data);

        void setRemindDoneSuccess(RequestSuccessBean data);

        void getRemindDetailsSuccess(RemindDetailsBean data);
    }
}
