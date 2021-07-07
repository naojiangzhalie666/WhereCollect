package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IBuyEnergyContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.util.ApiUtils;

public class BuyEnergyModel implements IBuyEnergyContract.IBuyEnergyModel {
    @Override
    public void getEnergyPrice(String uid, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getEnergyPrice(uid, new ApiCallBack<EnergyPriceBean>() {
            @Override
            public void onSuccess(EnergyPriceBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
