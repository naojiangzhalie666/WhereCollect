package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.ISelectSortChildNewContract;
import com.gongwu.wherecollect.contract.model.SelectSortChildNewModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.request.EditCustomizeReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;

import java.util.List;

public class SelectSortChildNewPresenter extends BasePresenter<ISelectSortChildNewContract.ISelectSortChildNewView> implements ISelectSortChildNewContract.ISelectSortChildNewPresenter {
    private ISelectSortChildNewContract.ISelectSortChildNewModel mModel;

    private SelectSortChildNewPresenter() {
        mModel = new SelectSortChildNewModel();
    }

    public static SelectSortChildNewPresenter getInstance() {
        return new SelectSortChildNewPresenter();
    }


    @Override
    public void getSubCategoryList(String uid, String parentCode) {
        mModel.getSubCategoryList(uid, parentCode, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getSubCategoryListSuccess(data);
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
    public void getTwoSubCategoryList(String uid, String parentCode) {
        mModel.getTwoSubCategoryList(uid, parentCode, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getTwoSubCategoryListSuccess(data);
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
    public void getThreeSubCategoryList(String uid, String parentCode) {
        mModel.getThreeSubCategoryList(uid, parentCode, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getThreeSubCategoryListSuccess(data);
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
    public void saveCustomSubCate(String uid, String name, String parentCode) {
        CustomSubCateReq req = new CustomSubCateReq();
        req.setUid(uid);
        req.setName(name);
        req.setParent_code(parentCode);
        mModel.saveCustomSubCate(req, new RequestCallback<BaseBean>() {
            @Override
            public void onSuccess(BaseBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().saveCustomSubCateSuccess(data);
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
    public void deleteCustomize(String uid, String id, String code) {
        EditCustomizeReq req = new EditCustomizeReq();
        req.setId(id);
        req.setCode(code);
        req.setType("cate");
        req.setUser_id(uid);
        mModel.deleteCustomize(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().deleteCustomizeSuccess(data);
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
