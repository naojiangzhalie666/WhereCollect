package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IAddFurnitureContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.AddFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;


public class AddFurnitureModel implements IAddFurnitureContract.IAddFurnitureModel {
    @Override
    public void getFurnitureList(AddFurnitureReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getTemplateFurnitureList(req, new ApiCallBack<List<FurnitureTemplateBean>>() {
            @Override
            public void onSuccess(List<FurnitureTemplateBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void addFurniture(AddFurnitureReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.addFurniture(req, new ApiCallBack<FurnitureBean>() {
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
