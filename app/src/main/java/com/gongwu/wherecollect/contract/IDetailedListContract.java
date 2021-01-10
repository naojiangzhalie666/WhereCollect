package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddChangWangGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ChangWangDetailBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangListBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;


public interface IDetailedListContract {
    interface IDetailedListModel {
        void getDetailedList(String uid, String family_code, String room_code, String furniture_code, final RequestCallback callback);
    }

    interface IDetailedListPresenter {
        void getDetailedList(String uid, String family_code, String room_code, String furniture_code);
    }

    interface IDetailedListView extends BaseView {
        void getDetailedListSuccess(DetailedListBean data);
    }
}
