package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.ILayerTemplateContract;
import com.gongwu.wherecollect.contract.model.LayerTemplateModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.request.LayerTemplateReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;

public class LayerTemplatePresenter extends BasePresenter<ILayerTemplateContract.ILayerTemplateView> implements ILayerTemplateContract.ILayerTemplatePresenter {

    private static final String TAG = "LayerTemplatePresenter";


    private ILayerTemplateContract.ILayerTemplateModel mModel;

    private LayerTemplatePresenter() {
        mModel = new LayerTemplateModel();
    }

    public static LayerTemplatePresenter getInstance() {
        return LayerTemplatePresenter.Inner.instance;
    }

    private static class Inner {
        private static final LayerTemplatePresenter instance = new LayerTemplatePresenter();
    }

    @Override
    public void getTemplateLayerList(String uid, String system_furniture_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getTemplateLayerList(uid, system_furniture_code, new RequestCallback<LayerTemplateBean>() {
            @Override
            public void onSuccess(LayerTemplateBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getTemplateLayerListSuccess(data);
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
    public void updataFurniture(String uid, String familyCode, String code, String layers) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditFurnitureReq furnitureReq = new EditFurnitureReq();
        furnitureReq.setUid(uid);
        furnitureReq.setCode(code);
        furnitureReq.setLayers(layers);
        furnitureReq.setFamily_code(familyCode);
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
