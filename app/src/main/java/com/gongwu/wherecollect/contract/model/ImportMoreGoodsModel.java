package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IImportMoreGoodsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class ImportMoreGoodsModel implements IImportMoreGoodsContract.IImportMoreGoodsModel {
    @Override
    public void getImportGoodsList(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getImportGoods(req, new ApiCallBack<ImportGoodsBean>() {
            @Override
            public void onSuccess(ImportGoodsBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
