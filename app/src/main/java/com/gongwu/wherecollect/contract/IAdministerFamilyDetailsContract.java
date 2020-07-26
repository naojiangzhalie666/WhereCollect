package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.FamilyListDetailsBean;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

public interface IAdministerFamilyDetailsContract {
    interface IAdministerFamilyDetailsModel {

        void getFamilyDetails(String uid, String familyCode, RequestCallback callback);

        void delFamily(String uid, String familyCode, RequestCallback callback);

        void disShareFamily(String uid, String familyCode, RequestCallback callback);

        void editFamily(String uid, String familyCode, String familyName, RequestCallback callback);

    }

    interface IAdministerFamilyDetailsPresenter {

        void getFamilyDetails(String uid, String familyCode);

        void delFamily(String uid, String familyCode);

        void disShareFamily(String uid, String familyCode);

        void editFamily(String uid, String familyCode, String familyName);

    }

    interface IAdministerFamilyDetailsView extends BaseView {


        void getFamilyDetailsSuccess(FamilyListDetailsBean bean);

        void delFamilySuccess(RequestSuccessBean bean);

        void disShareFamilySuccess(RequestSuccessBean bean);

        void editFamilySuccess(RequestSuccessBean bean);
    }
}
