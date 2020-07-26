package com.gongwu.wherecollect.contract.presenter;


import android.text.TextUtils;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IAddFamilyToSelectRoomsContract;
import com.gongwu.wherecollect.contract.model.AddFamilyToSelectRoomsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddFamilyAndRoomsReq;
import com.gongwu.wherecollect.net.entity.request.AddFamilyReq;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.util.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class AddFamilyToSelectRoomsPresenter extends BasePresenter<IAddFamilyToSelectRoomsContract.IAddFamilyToSelectRoomsView> implements IAddFamilyToSelectRoomsContract.IAddFamilyToSelectRoomsPresenter {

    private static class Inner {
        private static final AddFamilyToSelectRoomsPresenter instance = new AddFamilyToSelectRoomsPresenter();
    }

    private IAddFamilyToSelectRoomsContract.IAddFamilyToSelectRoomsModel mModel;

    private AddFamilyToSelectRoomsPresenter() {
        mModel = new AddFamilyToSelectRoomsModel();
    }

    public static AddFamilyToSelectRoomsPresenter getInstance() {
        return AddFamilyToSelectRoomsPresenter.Inner.instance;
    }

    @Override
    public void getRoomsTemplate(String uid) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddFamilyReq req = new AddFamilyReq();
        req.setUid(uid);
        mModel.getRoomsTemplate(req, new RequestCallback<List<RoomFurnitureBean>>() {

            @Override
            public void onSuccess(List<RoomFurnitureBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getRoomsTemplateSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }

    @Override
    public void createFamily(String uid, String familyName, List<RoomFurnitureBean> beans) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddFamilyReq req = new AddFamilyReq();
        req.setUid(uid);
        FamilyBean bean = new FamilyBean();
        bean.setName(familyName);
        req.setFamily_info(JsonUtils.jsonFromObject(bean));
        List<AddFamilyAndRoomsReq> room_list = new ArrayList<>();
        for (RoomFurnitureBean room : beans) {
            if (room.isSelect()) {
                AddFamilyAndRoomsReq roomsReq = new AddFamilyAndRoomsReq();
                roomsReq.setName(room.getName());
                if (!TextUtils.isEmpty(room.getCode())) {
                    roomsReq.setCode(room.getCode());
                    roomsReq.setIs_user(false);
                } else {
                    roomsReq.setIs_user(true);
                }
                room_list.add(roomsReq);
            }
        }
        req.setRoom_list(room_list);
        mModel.createFamily(req, new RequestCallback<RequestSuccessBean>() {

            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().createFamilySuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }
}
