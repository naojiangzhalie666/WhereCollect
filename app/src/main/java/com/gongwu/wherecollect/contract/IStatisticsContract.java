package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.StatisticsBean;

import java.util.List;

public interface IStatisticsContract {
    interface IStatisticsModel {
        void getGoodsReturnDetails(String uid, String family_code, String code, final RequestCallback callback);

        void getGoodsSortDetails(String uid, String family_code, final RequestCallback callback);

        void getGoodsColorsDetails(String uid, String family_code, final RequestCallback callback);

        void getGoodsSeasonDetails(String uid, String family_code, final RequestCallback callback);

        void getGoodsPriceDetails(String uid, String family_code, String code, final RequestCallback callback);

        void getGoodsTimeDetails(String uid, String family_code, String code, final RequestCallback callback);
    }

    interface IStatisticsPresenter {
        void getGoodsReturnDetails(String uid, String family_code, String code);

        void getGoodsSortDetails(String uid, String family_code);

        void getGoodsColorsDetails(String uid, String family_code);

        void getGoodsSeasonDetails(String uid, String family_code);

        void getGoodsPriceDetails(String uid, String family_code, String code);

        void getGoodsTimeDetails(String uid, String family_code, String code);
    }

    interface IStatisticsView extends BaseView {

        void getGoodsReturnDetailsSuccess(List<StatisticsBean> bean);

        void getGoodsSortDetailsSuccess(List<StatisticsBean> bean);

        void getGoodsColorsDetailsSuccess(List<StatisticsBean> bean);

        void getGoodsSeasonDetailsSuccess(List<StatisticsBean> bean);

        void getGoodsPriceDetailsSuccess(List<StatisticsBean> bean);

        void getGoodsTimeDetailsSuccess(List<StatisticsBean> bean);
    }
}
