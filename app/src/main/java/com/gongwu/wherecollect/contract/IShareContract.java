package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.ShareReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;

import java.util.List;

public interface IShareContract {
    interface IShareModel {

        void getSharedUsersList(ShareReq req, final RequestCallback callback);

        void getSharedLocations(ShareReq req, final RequestCallback callback);

        void closeShareUser(ShareReq req, final RequestCallback callback);
    }

    interface ISharePresenter {
        void getSharedUsersList(String uid);

        void getSharedLocations(String uid);

        void closeShareUser(String uid, String location_id, String be_shared_user_id, int type);
    }

    interface IShareView extends BaseView {

        void getSharedUsersListSuccess(List<SharedPersonBean> data);

        void getSharedLocationsSuccess(List<SharedLocationBean> data);

        void closeShareUserSuccess(RequestSuccessBean data);
    }
}
