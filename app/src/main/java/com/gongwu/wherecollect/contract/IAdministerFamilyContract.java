package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;

public interface IAdministerFamilyContract {
    interface IAdministerFamilyModel {

        void getFamilyList(String uid, final RequestCallback callback);

    }

    interface IAdministerFamilyPresenter {

        void getFamilyList(String uid);

    }

    interface IAdministerFamilyView extends BaseView {


        void getFamilyListSuccess(MyFamilyListBean bean);
    }
}
