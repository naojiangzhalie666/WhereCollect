package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddShareReq;
import com.gongwu.wherecollect.net.entity.request.ShareReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;

import java.util.List;

public interface IAddShareContract {
    interface IAddShareModel {

        void getSharePersonOldList(ShareReq req, final RequestCallback callback);

        void getShareUserCodeInfo(ShareReq req, final RequestCallback callback);

        void setShareLocation(AddShareReq req, final RequestCallback callback);

        void getShareRoomList(String uid, String family_code, String be_shared_user_id, final RequestCallback callback);
    }

    interface IAddSharePresenter {
        void getSharePersonOldList(String uid);

        void getShareUserCodeInfo(String uid, String usid);

        void setShareLocation(String uid, String family_id, String family_code, List<String> room_codes, SharedPersonBean bean);

        void getShareRoomList(String uid, String family_code, String be_shared_user_id);
    }

    interface IAddShareView extends BaseView {

        void getSharePersonOldListSuccess(List<SharedPersonBean> data);

        void getShareUserCodeInfoSuccess(SharedPersonBean data);

        void setShareLocationSuccess(RequestSuccessBean data);

        void getShareRoomListSuccess(List<BaseBean> data);

    }
}
