package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.ILayerTemplateContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class LayerTemplateModel implements ILayerTemplateContract.ILayerTemplateModel {
    @Override
    public void getTemplateLayerList(String uid, String system_furniture_code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getTemplateLayerList(uid, system_furniture_code, new ApiCallBack<LayerTemplateBean>() {
            @Override
            public void onSuccess(LayerTemplateBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void updataFurniture(EditFurnitureReq furnitureReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.updataFurniture(furnitureReq, new ApiCallBack<FurnitureBean>() {
            @Override
            public void onSuccess(FurnitureBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
