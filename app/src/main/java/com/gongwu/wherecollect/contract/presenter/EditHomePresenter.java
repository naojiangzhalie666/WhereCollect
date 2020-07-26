package com.gongwu.wherecollect.contract.presenter;

import android.content.Context;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IEditHomeContract;
import com.gongwu.wherecollect.contract.model.EditHomeModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.util.QiNiuUploadUtil;

import java.io.File;
import java.util.List;

public class EditHomePresenter extends BasePresenter<IEditHomeContract.IEditHomeView> implements IEditHomeContract.IEditHomePresenter {

    /**
     * 上传图片
     *
     * @param mContext 上下文
     * @param file     图片
     */
    public void uploadImg(Context mContext, File file) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        QiNiuUploadUtil uploadUtil = QiNiuUploadUtil.getInstance(mContext);
        uploadUtil.setUpLoadListener(new QiNiuUploadUtil.UpLoadListener() {
            @Override
            public void onUpLoadSuccess(String path) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onUpLoadSuccess(path);
                }
            }

            @Override
            public void onUpLoadError(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
        uploadUtil.start(AppConstant.UPLOAD_GOODS_IMG_PATH, file);
    }

    @Override
    public void getFamilyRoomList(String uid, String code) {
        mModel.getFamilyRoomList(uid, code, new RequestCallback<List<RoomBean>>() {
            @Override
            public void onSuccess(List<RoomBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getFamilyRoomListSuccess(data);
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
    public void deleteFurniture(String uid, String roomCode, List<String> furnitureCodes) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditFurnitureReq furnitureReq = new EditFurnitureReq();
        furnitureReq.setUid(uid);
        furnitureReq.setRoomCode(roomCode);
        furnitureReq.setFurnitureCodes(furnitureCodes);
        mModel.deleteFurniture(furnitureReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().deleteFurnitureSuccess(data);
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
    public void topFurniture(String uid, List<String> furnitureCodes) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditFurnitureReq furnitureReq = new EditFurnitureReq();
        furnitureReq.setUid(uid);
        furnitureReq.setFurnitureCodes(furnitureCodes);
        mModel.topFurniture(furnitureReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().topFurnitureSuccess(data);
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
    public void moveFurniture(String uid, String family_code, String room_code, String target_family_code, List<String> codes) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditFurnitureReq furnitureReq = new EditFurnitureReq();
        furnitureReq.setUid(uid);
        furnitureReq.setFamily_code(family_code);
        furnitureReq.setRoom_code(room_code);
        furnitureReq.setTarget_family_code(target_family_code);
        furnitureReq.setCodes(codes);
        mModel.moveFurniture(furnitureReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().moveFurnitureSuccess(data);
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
    public void updataFurniture(String uid, String family_code, FurnitureBean bean) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditFurnitureReq furnitureReq = new EditFurnitureReq();
        furnitureReq.setUid(uid);
        furnitureReq.setFamily_code(family_code);
        furnitureReq.setBackground_url(bean.getBackground_url());
        furnitureReq.setImage_url(bean.getBackground_url());
        furnitureReq.setName(bean.getName());
        furnitureReq.setRatio(bean.getRatio());
        furnitureReq.setCode(bean.getCode());
        mModel.updataFurniture(furnitureReq, new RequestCallback<FurnitureBean>() {
            @Override
            public void onSuccess(FurnitureBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().updataFurnitureSuccess(data);
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

    private IEditHomeContract.IEditHomeModel mModel;

    private EditHomePresenter() {
        mModel = new EditHomeModel();
    }

    public static EditHomePresenter getInstance() {
        return EditHomePresenter.Inner.instance;
    }

    private static class Inner {
        private static final EditHomePresenter instance = new EditHomePresenter();
    }
}
