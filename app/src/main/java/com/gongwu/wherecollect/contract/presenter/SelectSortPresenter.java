package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.ISelectSortContract;
import com.gongwu.wherecollect.contract.model.SelectSortModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;

import java.util.List;

public class SelectSortPresenter extends BasePresenter<ISelectSortContract.ISelectSortView> implements ISelectSortContract.ISelectSortPresenter {
    private static class Inner {
        private static final SelectSortPresenter instance = new SelectSortPresenter();
    }

    private ISelectSortContract.ISelectSortModel mModel;

    private SelectSortPresenter() {
        mModel = new SelectSortModel();
    }

    public static SelectSortPresenter getInstance() {
        return SelectSortPresenter.Inner.instance;
    }

    @Override
    public void getFirstCategoryList(String uid) {
        mModel.getFirstCategoryList(uid, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getFirstCategoryListSuccess(data);
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

    @Override
    public void getCategoryDetails(String uid, String code) {
        mModel.getCategoryDetails(uid, code, new RequestCallback<List<ChannelBean>>() {
            @Override
            public void onSuccess(List<ChannelBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getCategoryDetailsSuccess(data);
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
