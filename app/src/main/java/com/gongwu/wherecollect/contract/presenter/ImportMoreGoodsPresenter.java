package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IImportMoreGoodsContract;
import com.gongwu.wherecollect.contract.model.ImportMoreGoodsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;

public class ImportMoreGoodsPresenter extends BasePresenter<IImportMoreGoodsContract.IImportMoreGoodsView> implements IImportMoreGoodsContract.IImportMoreGoodsPresenter {

    public static ImportMoreGoodsPresenter getInstance() {
        return Inner.instance;
    }

    private IImportMoreGoodsContract.IImportMoreGoodsModel mModel;

    private ImportMoreGoodsPresenter() {
        mModel = new ImportMoreGoodsModel();
    }

    private static class Inner {
        private static final ImportMoreGoodsPresenter instance = new ImportMoreGoodsPresenter();
    }

    @Override
    public void getImportGoodsList(String uid, String location_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getImportGoodsList(new EditGoodsReq(uid, location_code), new RequestCallback<ImportGoodsBean>() {
            @Override
            public void onSuccess(ImportGoodsBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getImportGoodsListSuccess(data);
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
