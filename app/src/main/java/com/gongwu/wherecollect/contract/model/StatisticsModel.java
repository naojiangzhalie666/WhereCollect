package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IStatisticsContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.response.StatisticsBean;
import com.gongwu.wherecollect.util.ApiUtils;

import java.util.List;

public class StatisticsModel implements IStatisticsContract.IStatisticsModel {
    @Override
    public void getGoodsReturnDetails(String uid, String family_code, String code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsReturnDetails(uid, family_code, code, new ApiCallBack<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getGoodsSortDetails(String uid, String family_code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsSortDetails(uid, family_code, new ApiCallBack<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getGoodsColorsDetails(String uid, String family_code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsColorsDetails(uid, family_code, new ApiCallBack<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getGoodsSeasonDetails(String uid, String family_code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsSeasonDetails(uid, family_code, new ApiCallBack<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getGoodsPriceDetails(String uid, String family_code, String code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsPriceDetails(uid, family_code, code, new ApiCallBack<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getGoodsTimeDetails(String uid, String family_code, String code, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getGoodsTimeDetails(uid, family_code, code, new ApiCallBack<List<StatisticsBean>>() {
            @Override
            public void onSuccess(List<StatisticsBean> data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
