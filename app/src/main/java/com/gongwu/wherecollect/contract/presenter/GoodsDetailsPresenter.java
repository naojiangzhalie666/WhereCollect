package com.gongwu.wherecollect.contract.presenter;

import android.content.Context;
import android.text.TextUtils;
import android.view.TextureView;

import com.gongwu.wherecollect.base.App;
import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IGoodsDetailsContract;
import com.gongwu.wherecollect.contract.model.GoodsDetailsModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddGoodsReq;
import com.gongwu.wherecollect.net.entity.request.GoodsDetailsReq;
import com.gongwu.wherecollect.net.entity.request.SearchReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.QiNiuUploadUtil;
import com.gongwu.wherecollect.util.StringUtils;

import java.io.File;
import java.util.List;

public class GoodsDetailsPresenter extends BasePresenter<IGoodsDetailsContract.IGoodsDetailsView> implements IGoodsDetailsContract.IGoodsDetailsPresenter {

    private static final String TAG = "GoodsDetailsPresenter";

    private IGoodsDetailsContract.IGoodsDetailsModel mModel;

    private GoodsDetailsPresenter() {
        mModel = new GoodsDetailsModel();
    }

    public static GoodsDetailsPresenter getInstance() {
        return GoodsDetailsPresenter.Inner.instance;
    }

    private static class Inner {
        private static final GoodsDetailsPresenter instance = new GoodsDetailsPresenter();
    }

    /**
     * 上传图片
     *
     * @param mContext 上下文
     * @param file     图片
     */
    public void uploadImg(Context mContext, File file) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        QiNiuUploadUtil uploadUtil = QiNiuUploadUtil.getInstance(mContext);
        uploadUtil.setUpLoadListener(new QiNiuUploadUtil.UpLoadListener() {
            @Override
            public void onUpLoadSuccess(String path) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onUpLoadSuccess(path);
                }
            }

            @Override
            public void onUpLoadError(String msg) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().onError(msg);
                }
            }
        });
        uploadUtil.start(AppConstant.UPLOAD_GOODS_IMG_PATH, file);
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
    public void editGoods(Context context, ObjectBean tempBean, String names, String isbn) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddGoodsReq goodsReq = new AddGoodsReq();
        goodsReq.setUid(App.getUser(context).getId());
        goodsReq.setISBN(isbn);
        goodsReq.setChannel(TextUtils.isEmpty(tempBean.getChannel()) ? "" : JsonUtils.jsonFromObject(tempBean.getChannel().split(">")));
        goodsReq.setColor(TextUtils.isEmpty(tempBean.getColor()) ? "" : JsonUtils.jsonFromObject(tempBean.getColor().split("、")));
        goodsReq.setDetail(TextUtils.isEmpty(tempBean.getDetail()) ? "" : tempBean.getDetail());
        goodsReq.setPrice_max(tempBean.getPrice() + "");
        goodsReq.setPrice_min(tempBean.getPrice() + "");
        goodsReq.setSeason(tempBean.getSeason());
        goodsReq.setStar(tempBean.getStar() + "");
        goodsReq.setName(names);
        goodsReq.setImage_url(tempBean.getObject_url());
        goodsReq.setCount(tempBean.getCount() + "");
        goodsReq.setBuy_date(tempBean.getBuy_date());
        goodsReq.setExpire_date(tempBean.getExpire_date());
        goodsReq.setCode(tempBean.get_id());
        goodsReq.setBelonger(tempBean.getBelonger());
        if (tempBean.getCategories() != null && tempBean.getCategories().size() > 0) {
            StringBuilder ca = new StringBuilder();
            for (int i = 0; i < StringUtils.getListSize(tempBean.getCategories()); i++) {
                ca.append(tempBean.getCategories().get(i).getCode());
                if (i != tempBean.getCategories().size() - 1) {
                    ca.append(",");
                }
            }
            goodsReq.setCategory_codes(ca.toString());
        }
        if (tempBean.getLocations() != null && tempBean.getLocations().size() > 0) {
            StringBuilder lc = new StringBuilder();
            for (int i = 0; i < StringUtils.getListSize(tempBean.getLocations()); i++) {
                lc.append(tempBean.getLocations().get(i).getCode());
                if (i != tempBean.getLocations().size() - 1) {
                    lc.append(",");
                }
            }
            goodsReq.setLocation_codes(lc.toString());
        }
        mModel.editGoods(goodsReq, new RequestCallback<ObjectBean>() {

            @Override
            public void onSuccess(ObjectBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().editGoodsSuccess(data);
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
    public void removeObjectFromFurnitrue(String uid, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.setUid(uid);
        req.setCode(code);
        mModel.removeObjectFromFurnitrue(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().removeObjectFromFurnitrueSuccess(data);
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
    public void delGoods(String uid, String object_id) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        GoodsDetailsReq req = new GoodsDetailsReq();
        req.setUid(uid);
        req.setObject_id(object_id);
        mModel.delGoods(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().delGoodsSuccess(data);
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
    public void getGoodsRemindsById(String uid, String obj_id) {
        mModel.getGoodsRemindsById(uid, obj_id, new RequestCallback<List<RemindBean>>() {
            @Override
            public void onSuccess(List<RemindBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getGoodsRemindsByIdSuccess(data);
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
    public void getBelongerList(String uid) {
        mModel.getBelongerList(uid, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getBelongerListSuccess(data);
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
    public void getBuyFirstCategoryList(String uid) {
        mModel.getBuyFirstCategoryList(uid, new RequestCallback<List<BaseBean>>() {
            @Override
            public void onSuccess(List<BaseBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getBuyFirstCategoryListSuccess(data);
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
    public void getSubCategoryList(String uid, String parentCode, String type) {
        mModel.getSubCategoryList(uid, parentCode, type, new RequestCallback<List<BaseBean>>() {
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
    public void getTwoSubCategoryList(String uid, String parentCode, String type) {
        mModel.getTwoSubCategoryList(uid, parentCode, type, new RequestCallback<List<BaseBean>>() {
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
    public void getThreeSubCategoryList(String uid, String parentCode, String type) {
        mModel.getThreeSubCategoryList(uid, parentCode, type, new RequestCallback<List<BaseBean>>() {
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

    public boolean goodsInfosIsEdit(ObjectBean newBean, ObjectBean oldBean) {
        //图片
        if (!newBean.getObject_url().equals(oldBean.getObject_url())) {
            return true;
        }
        //名称
        if (!newBean.getName().equals(oldBean.getName())) {
            return true;
        }
        //分类
        if (newBean.getCategories() != null && oldBean.getCategories() == null) {
            return true;
        } else if (newBean.getCategories() == null && oldBean.getCategories() != null) {
            return true;
        } else if (newBean.getCategories() != null && oldBean.getCategories() != null) {
            if (!JsonUtils.jsonFromObject(newBean.getCategories()).equals(JsonUtils.jsonFromObject(oldBean.getCategories()))) {
                return true;
            }
        }
        //数量
        if (newBean.getCount() != oldBean.getCount()) {
            return true;
        }
        //星级
        if (newBean.getStar() != oldBean.getStar()) {
            return true;
        }
        //购买时间
        if (!TextUtils.isEmpty(newBean.getBuy_date()) && TextUtils.isEmpty(oldBean.getBuy_date())) {
            return true;
        } else if (TextUtils.isEmpty(newBean.getBuy_date()) && !TextUtils.isEmpty(oldBean.getBuy_date())) {
            return true;
        } else if (!TextUtils.isEmpty(newBean.getBuy_date()) && !TextUtils.isEmpty(oldBean.getBuy_date()) && !newBean.getBuy_date().equals(oldBean.getBuy_date())) {
            return true;
        }
        //到期时间
        if (!TextUtils.isEmpty(newBean.getExpire_date()) && TextUtils.isEmpty(oldBean.getExpire_date())) {
            return true;
        } else if (TextUtils.isEmpty(newBean.getExpire_date()) && !TextUtils.isEmpty(oldBean.getExpire_date())) {
            return true;
        } else if (!TextUtils.isEmpty(newBean.getExpire_date()) && !TextUtils.isEmpty(oldBean.getExpire_date()) && !newBean.getExpire_date().equals(oldBean.getExpire_date())) {
            return true;
        }
        //价格
        if (!TextUtils.isEmpty(newBean.getPrice()) && TextUtils.isEmpty(oldBean.getPrice())) {
            return true;
        } else if (TextUtils.isEmpty(newBean.getPrice()) && !TextUtils.isEmpty(oldBean.getPrice())) {
            return true;
        } else if (!TextUtils.isEmpty(newBean.getPrice()) && !TextUtils.isEmpty(oldBean.getPrice()) && !newBean.getPrice().equals(oldBean.getPrice())) {
            return true;
        }
        //颜色
        if (!TextUtils.isEmpty(newBean.getColorStr()) && TextUtils.isEmpty(oldBean.getColorStr())) {
            return true;
        } else if (TextUtils.isEmpty(newBean.getColorStr()) && !TextUtils.isEmpty(oldBean.getColorStr())) {
            return true;
        } else if (!TextUtils.isEmpty(newBean.getColorStr()) && !TextUtils.isEmpty(oldBean.getColorStr()) && !newBean.getColorStr().equals(oldBean.getColorStr())) {
            return true;
        }
        //季节
        if (!TextUtils.isEmpty(newBean.getSeason()) && TextUtils.isEmpty(oldBean.getSeason())) {
            return true;
        } else if (TextUtils.isEmpty(newBean.getSeason()) && !TextUtils.isEmpty(oldBean.getSeason())) {
            return true;
        } else if (!TextUtils.isEmpty(newBean.getSeason()) && !TextUtils.isEmpty(oldBean.getSeason()) && !newBean.getSeason().equals(oldBean.getSeason())) {
            return true;
        }
        //购买渠道
        if (!newBean.getChannelStr().equals(oldBean.getChannelStr())) {
            return true;
        }
        //备注
        if (!TextUtils.isEmpty(newBean.getDetail()) && TextUtils.isEmpty(oldBean.getDetail())) {
            return true;
        } else if (TextUtils.isEmpty(newBean.getDetail()) && !TextUtils.isEmpty(oldBean.getDetail())) {
            return true;
        } else if (!TextUtils.isEmpty(newBean.getDetail()) && !TextUtils.isEmpty(oldBean.getDetail()) && !newBean.getDetail().equals(oldBean.getDetail())) {
            return true;
        }
        //归属人
        if (!TextUtils.isEmpty(newBean.getBelonger()) && TextUtils.isEmpty(oldBean.getBelonger())) {
            return true;
        } else if (TextUtils.isEmpty(newBean.getBelonger()) && !TextUtils.isEmpty(oldBean.getBelonger())) {
            return true;
        } else if (!TextUtils.isEmpty(newBean.getBelonger()) && !TextUtils.isEmpty(oldBean.getBelonger()) && !newBean.getBelonger().equals(oldBean.getBelonger())) {
            return true;
        }
        return false;
    }
}
