package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IEditRoomContract;
import com.gongwu.wherecollect.contract.model.EditRoomModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditRoomReq;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;

import java.util.List;

public class EditRoomPresenter extends BasePresenter<IEditRoomContract.IEditRoomView> implements IEditRoomContract.IEditRoomPresenter {

    private IEditRoomContract.IEditRoomModel mModel;

    private EditRoomPresenter() {
        mModel = new EditRoomModel();
    }

    public static EditRoomPresenter getInstance() {
        return EditRoomPresenter.Inner.instance;
    }

    private static class Inner {
        private static final EditRoomPresenter instance = new EditRoomPresenter();
    }

    @Override
    public void updataRoomPosition(String uid, String location_codes) {
        EditRoomReq roomReq = new EditRoomReq();
        roomReq.setUid(uid);
        roomReq.setLocation_codes(location_codes);
        mModel.updataRoomPosition(roomReq, new RequestCallback<List<RoomBean>>() {
            @Override
            public void onSuccess(List<RoomBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().updataRoomPositionSuccess(data);
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
    public void addRoom(String uid, String location_code, String location_name) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditRoomReq roomReq = new EditRoomReq();
        roomReq.setUid(uid);
        roomReq.setLocation_code(location_code);
        roomReq.setLocation_name(location_name);
        mModel.addRoom(roomReq, new RequestCallback<RoomBean>() {
            @Override
            public void onSuccess(RoomBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().addRoomSuccess(data);
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
    public void delRoom(String uid, String location_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditRoomReq roomReq = new EditRoomReq();
        roomReq.setUid(uid);
        roomReq.setLocation_code(location_code);
        mModel.delRoom(roomReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().delRoomSuccess(data);
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
    public void editRoom(String uid, String location_code, String location_name) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditRoomReq roomReq = new EditRoomReq();
        roomReq.setUid(uid);
        roomReq.setName(location_name);
        roomReq.setLocation_code(location_code);
        mModel.editRoom(roomReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().editRoomSuccess(data);
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
    public void moveRoom(String uid, String old_family_code, String new_family_code, String new_family_name, List<String> rooms_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditRoomReq roomReq = new EditRoomReq();
        roomReq.setUid(uid);
        roomReq.setOld_family_code(old_family_code);
        roomReq.setNew_family_code(new_family_code);
        roomReq.setNew_family_name(new_family_name);
        roomReq.setRooms_code(rooms_code);
        mModel.moveRoom(roomReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().moveRoomSuccess(data);
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
