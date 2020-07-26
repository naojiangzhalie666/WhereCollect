package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IAdministerFamilySharedContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.ShareReq;
import com.gongwu.wherecollect.net.entity.request.SharedRoomsReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class AdministerFamilySharedModel implements IAdministerFamilySharedContract.IAdministerFamilySharedModel {
    @Override
    public void getShareListUserByFamily(String uid, String familyCode, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getShareListUserByFamily(uid, familyCode, new ApiCallBack<List<SharedPersonBean>>() {
            @Override
            public void onSuccess(List<SharedPersonBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getShareRoomList(String uid, String family_code, String be_shared_user_id, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getShareRoomList(uid, family_code, be_shared_user_id, new ApiCallBack<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void delCollaborator(String uid, String be_shared_user, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.delCollaborator(uid, be_shared_user, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }


    @Override
    public void shareOrCancelShareRooms(SharedRoomsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.shareOrCancelShareRooms(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
