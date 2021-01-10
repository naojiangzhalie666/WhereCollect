package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IDetailedListContract;
import com.gongwu.wherecollect.contract.model.DetailedListModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;

public class DetailedListPresenter extends BasePresenter<IDetailedListContract.IDetailedListView> implements IDetailedListContract.IDetailedListPresenter {

    private IDetailedListContract.IDetailedListModel mModel;

    private DetailedListPresenter() {
        mModel = new DetailedListModel();
    }

    public static DetailedListPresenter getInstance() {
        return new DetailedListPresenter();
    }

    /**
     * @param uid            uid
     * @param family_code    家庭
     * @param room_code      房间
     * @param furniture_code 家具
     */
    @Override
    public void getDetailedList(String uid, String family_code, String room_code, String furniture_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getDetailedList(uid, family_code, room_code, furniture_code, new RequestCallback<DetailedListBean>() {
            @Override
            public void onSuccess(DetailedListBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getDetailedListSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
    }
}
