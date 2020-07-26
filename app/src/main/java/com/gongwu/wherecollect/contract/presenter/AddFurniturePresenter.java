package com.gongwu.wherecollect.contract.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IAddFurnitureContract;
import com.gongwu.wherecollect.contract.model.AddFurnitureModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.AddFurnitureReq;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
import com.gongwu.wherecollect.util.JsonUtils;
import com.gongwu.wherecollect.util.QiNiuUploadUtil;

import java.io.File;
import java.util.List;

public class AddFurniturePresenter extends BasePresenter<IAddFurnitureContract.IAddFurnitureView> implements IAddFurnitureContract.IAddFurniturePresenter {

    private static class Inner {
        private static final AddFurniturePresenter instance = new AddFurniturePresenter();
    }

    private IAddFurnitureContract.IAddFurnitureModel mModel;

    private AddFurniturePresenter() {
        mModel = new AddFurnitureModel();
    }

    public static AddFurniturePresenter getInstance() {
        return AddFurniturePresenter.Inner.instance;
    }

    @Override
    public void getFurnitureList(String uid, String type, String keyword) {
        AddFurnitureReq req = new AddFurnitureReq();
        req.setUid(uid);
        req.setType(type);
        req.setKeyword(keyword);
        mModel.getFurnitureList(req, new RequestCallback<List<FurnitureTemplateBean>>() {

            @Override
            public void onSuccess(List<FurnitureTemplateBean> data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getFurnitureListSuccess(data);
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
    public void addFurniture(String uid, String familyCode, String locationCode, FurnitureBean bean) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        AddFurnitureReq req = new AddFurnitureReq();
        req.setBackground_url(bean.getBackground_url());
        req.setImage_url(bean.getImage_url());
        req.setFamily_code(familyCode);
        req.setLocation_code(locationCode);
        req.setName(bean.getName());
        if (bean.getRatio() > 0) {
            req.setRatio(bean.getRatio());
        }
        req.setScale(JsonUtils.jsonFromObject(bean.getScale()));
        if (!TextUtils.isEmpty(bean.getCode())) {
            req.setSystem_furniture_code(bean.getCode());
        }
        req.setUid(uid);
        //旧版接口，4.0不用了先写死
        req.setPosition("{\"x\":0.0,\"y\":1.0}");
        req.setLayers("[]");
        mModel.addFurniture(req, new RequestCallback<FurnitureBean>() {

            @Override
            public void onSuccess(FurnitureBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().addFurnitureSuccess(data);
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
}
