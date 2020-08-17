package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.BuyVIPResultBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.net.entity.response.VIPBean;


public interface IBuyVIPContract {
    interface IBuyVIPModel {
        void getVIPPrice(String uid, final RequestCallback callback);

        void getUserInfo(String uid, final RequestCallback callback);

        void sharedApp(String uid, String share_type, final RequestCallback callback);

        void notificationServer(String uid, String pay_type, String order_no, final RequestCallback callback);

        void buyVipWXOrAli(String uid, int price, String type, String couponId, final RequestCallback callback);
    }

    interface IBuyVIPPresenter {
        void getVIPPrice(String uid);

        void getUserInfo(String uid);

        void sharedApp(String uid, String share_type);

        void notificationServer(String uid, String pay_type, String order_no);

        void buyVipWXOrAli(String uid, int price, String type, String couponId);
    }

    interface IBuyVIPView extends BaseView {
        void getVIPPriceSuccess(VIPBean data);

        void getUserInfoSuccess(UserBean data);

        void sharedAppSuccess(RequestSuccessBean data);

        void notificationServerSuccess(RequestSuccessBean data);

        void buyVipWXOrAliSuccess(BuyVIPResultBean data);
    }
}
