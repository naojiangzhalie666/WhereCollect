package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.GoodsListReq;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;

import java.util.List;

public interface ILookContract {
    interface ILookModel {
        void getUserFamily(String uid, String user_name, final RequestCallback callback);

        void getObjectBean(String uid, String family_code, final RequestCallback callback);
    }

    interface ILookPresenter {
        void getUserFamily(String uid, String user_name);

        void getObjectBean(String uid, String family_code);
    }

    interface ILookView extends BaseView {
        void getUserFamilySuccess(List<FamilyBean> data);

        void getGoodsListSuccess(List<MainGoodsBean> data);
    }
}
