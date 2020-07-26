package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.ShareReq;
import com.gongwu.wherecollect.net.entity.request.SharedRoomsReq;
import com.gongwu.wherecollect.net.entity.request.SharedUserReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;

import java.util.List;

public interface IAdministerFamilySharedContract {
    interface IAdministerFamilySharedModel {
        void getShareListUserByFamily(String uid, String familyCode, RequestCallback callback);

        void getShareRoomList(String uid, String family_code, String be_shared_user_id, final RequestCallback callback);

        void delCollaborator(String uid, String be_shared_user, final RequestCallback callback);

        void shareOrCancelShareRooms(SharedRoomsReq req, final RequestCallback callback);
    }

    interface IAdministerFamilySharedPresenter {
        void getShareListUserByFamily(String uid, String familyCode);

        void getShareRoomList(String uid, String family_code, String be_shared_user_id);

        void delCollaborator(String uid, String be_shared_user);

        void shareOrCancelShareRooms(String uid, SharedUserReq be_shared_user, String family_id, String family_code, List<String> select, List<String> unSelect);
    }

    interface IAdministerFamilySharedView extends BaseView {
        void getShareListUserByFamilySuccess(List<SharedPersonBean> bean);

        void getShareRoomListSuccess(List<BaseBean> data);

        void delCollaboratorSuccess(RequestSuccessBean data);

        void shareOrCancelShareRoomsSuccess(RequestSuccessBean data);
    }
}
