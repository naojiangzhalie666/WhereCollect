package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IEditFurniturePatternContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.util.ApiUtils;


public class EditFurniturePatternModel implements IEditFurniturePatternContract.IEditFurniturePatternModel {
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
