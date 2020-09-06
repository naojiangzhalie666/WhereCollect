package com.gongwu.wherecollect.contract.presenter;


import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.IAddGoodsPropertyContract;
import com.gongwu.wherecollect.contract.model.AddGoodsPropertyModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;

import java.util.List;

public class AddGoodsPropertyPresenter extends BasePresenter<IAddGoodsPropertyContract.IAddGoodsPropertyView> implements IAddGoodsPropertyContract.IAddGoodsPropertyPresenter {

    private static final String TAG = "AddGoodsPropertyPresenter";

    @Override
    public void getColors(String uid) {
        mModel.getColors(uid, new RequestCallback<List<String>>() {
            @Override
            public void onSuccess(List<String> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getColorsSuccess(data);
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
    public void getChannel(String uid) {
        mModel.getChannel(uid, new RequestCallback<List<ChannelBean>>() {
            @Override
            public void onSuccess(List<ChannelBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getChannelSuccess(data);
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

    @Override
    public void getChannelList(String uid, String keyword) {
        mModel.getChannelList(uid, keyword, new RequestCallback<List<ChannelBean>>() {
            @Override
            public void onSuccess(List<ChannelBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getChannelListSuccess(data);
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
    public void getSearchSort(String uid, String keyword) {
        mModel.getSearchSort(uid,  keyword, new RequestCallback<List<ChannelBean>>() {
            @Override
            public void onSuccess(List<ChannelBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getSearchSortSuccess(data);
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
    public void addChannel(String uid, String name, String code) {
        mModel.addChannel(uid, name, code, new RequestCallback<ChannelBean>() {
            @Override
            public void onSuccess(ChannelBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().addChannelSuccess(data);
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

    private static class Inner {
        private static final AddGoodsPropertyPresenter instance = new AddGoodsPropertyPresenter();
    }

    private IAddGoodsPropertyContract.IAddGoodsPropertyModel mModel;

    private AddGoodsPropertyPresenter() {
        mModel = new AddGoodsPropertyModel();
    }

    public static AddGoodsPropertyPresenter getInstance() {
        return AddGoodsPropertyPresenter.Inner.instance;
    }

}
