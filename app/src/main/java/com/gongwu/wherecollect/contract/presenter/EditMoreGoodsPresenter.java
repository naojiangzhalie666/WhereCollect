package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IEditMoreGoodsContract;
import com.gongwu.wherecollect.contract.model.EditMoreGoodsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.EditMoreGoodsReq;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public class EditMoreGoodsPresenter extends BasePresenter<IEditMoreGoodsContract.IEditMoreGoodsView> implements IEditMoreGoodsContract.IEditMoreGoodsPresenter {


    private IEditMoreGoodsContract.IEditMoreGoodsModel mModel;

    private EditMoreGoodsPresenter() {
        mModel = new EditMoreGoodsModel();
    }

    public static EditMoreGoodsPresenter getInstance() {
        return EditMoreGoodsPresenter.Inner.instance;
    }

    private static class Inner {
        private static final EditMoreGoodsPresenter instance = new EditMoreGoodsPresenter();
    }

    @Override
    public void getEditMoreGoodsList(String uid, String family_code) {
        mModel.getEditMoreGoodsList(uid, family_code, "all", new RequestCallback<List<ObjectBean>>() {
            @Override
            public void onSuccess(List<ObjectBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getEditMoreGoodsListSuccess(data);
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
    public void delSelectGoods(String uid, String ids) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq(uid, null);
        req.setIds(ids);
        mModel.delSelectGoods(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().delSelectGoodsSuccess(data);
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
    public void objectsAddCategory(String uid, List<String> object_ids, List<String> category_codes) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditMoreGoodsReq req = new EditMoreGoodsReq();
        req.setUid(uid);
        req.setObject_ids(object_ids);
        req.setCategory_codes(category_codes);
        mModel.objectsAddCategory(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().objectsAddCategorySuccess(data);
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
