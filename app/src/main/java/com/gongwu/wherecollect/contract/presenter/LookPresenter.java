package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.ILookContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.contract.model.LookModel;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public class LookPresenter extends BasePresenter<ILookContract.ILookView> implements ILookContract.ILookPresenter {

    private static final String TAG = "LookPresenter";


    private ILookContract.ILookModel mModel;

    private LookPresenter() {
        mModel = new LookModel();
    }

    public static LookPresenter getInstance() {
        return new LookPresenter();
    }

    @Override
    public void getUserFamily(String uid, String user_name) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getUserFamily(uid, user_name, new RequestCallback<List<FamilyBean>>() {
            @Override
            public void onSuccess(List<FamilyBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getUserFamilySuccess(data);
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
    public void getChangWangList(String uid) {
        mModel.getChangWangList(uid, new RequestCallback<List<ChangWangBean>>() {
            @Override
            public void onSuccess(List<ChangWangBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getChangWangListSuccess(data);
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
    public void getObjectBean(String uid, String family_code, boolean darklayer) {
        mModel.getObjectBean(uid, family_code, darklayer, new RequestCallback<List<MainGoodsBean>>() {
            @Override
            public void onSuccess(List<MainGoodsBean> data) {
                if (getUIView() != null) {
                    getUIView().getGoodsListSuccess(data);
                }
            }

            @Override
            public void onFailure(String msg) {
                if (getUIView() != null) {
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
    public void setGoodsWeight(String uid, String goodsId) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq(uid, null);
        req.setObjectId(goodsId);
        mModel.setGoodsWeight(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().setGoodsWeightSuccess(data);
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
    public void setGoodsNoWeight(String uid, String goodsId) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq(uid, null);
        req.setObject_id(goodsId);
        mModel.setGoodsNoWeight(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().setGoodsNoWeightSuccess(data);
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
    public void goodsArchive(String uid, String goodsId) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.setUid(uid);
        req.setObject_id(goodsId);
        mModel.goodsArchive(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().goodsArchiveSuccess(data);
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
    public void removeArchiveObjects(String uid) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.setUid(uid);
        mModel.removeArchiveObjects(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().removeArchiveObjectsSuccess(data);
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

