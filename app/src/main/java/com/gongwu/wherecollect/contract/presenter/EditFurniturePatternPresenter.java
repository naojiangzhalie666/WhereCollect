package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IEditFurniturePatternContract;
import com.gongwu.wherecollect.contract.model.EditFurniturePatternModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;

public class EditFurniturePatternPresenter extends BasePresenter<IEditFurniturePatternContract.IEditFurniturePatternView> implements IEditFurniturePatternContract.IEditFurniturePatternPresenter {

    private IEditFurniturePatternContract.IEditFurniturePatternModel mModel;

    private EditFurniturePatternPresenter() {
        mModel = new EditFurniturePatternModel();
    }

    public static EditFurniturePatternPresenter getInstance() {
        return new EditFurniturePatternPresenter();
    }

    @Override
    public void updataFurniture(String uid, String code, String layers, float ratio) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditFurnitureReq furnitureReq = new EditFurnitureReq();
        furnitureReq.setUid(uid);
        furnitureReq.setCode(code);
        furnitureReq.setLayers(layers);
        furnitureReq.setRatio(ratio);
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
}
