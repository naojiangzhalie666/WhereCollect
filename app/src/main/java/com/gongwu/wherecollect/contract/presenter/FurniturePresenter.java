package com.gongwu.wherecollect.contract.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.gongwu.wherecollect.base.BasePresenter;
import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.contract.IFurnitureContract;
import com.gongwu.wherecollect.contract.model.FurnitureModel;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditRoomReq;
import com.gongwu.wherecollect.net.entity.request.FurnitureDetailsReq;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.LayerReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.util.QiNiuUploadUtil;
import com.gongwu.wherecollect.util.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FurniturePresenter extends BasePresenter<IFurnitureContract.IFurnitureView> implements IFurnitureContract.IFurniturePresenter {

    public static FurniturePresenter getInstance() {
        return Inner.instance;
    }

    private IFurnitureContract.IFurnitureModel mModel;

    private FurniturePresenter() {
        mModel = new FurnitureModel();
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
    public void getFurnitureDetails(String uid, String location_code, String family_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        mModel.getFurnitureDetails(new FurnitureDetailsReq(uid, family_code, location_code), new RequestCallback<RoomFurnitureGoodsBean>() {
            @Override
            public void onSuccess(RoomFurnitureGoodsBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().getFurnitureDetailsSuccess(data);
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
    public void getImportGoodsList(String uid, String location_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq(uid, location_code);
        req.setPage(1);
        mModel.getImportGoodsList(req, new RequestCallback<ImportGoodsBean>() {
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
    public void topSelectGoods(String uid, String furnitureCode, List<String> objectIds) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq(uid, null);
        req.setFurnitureCode(furnitureCode);
        req.setObjectIds(objectIds);
        mModel.topSelectGoods(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().topSelectGoodsSuccess(data);
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
    public void moveLayer(String uid, String location_code, String code, String family_code, String target_family_code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq();
        req.setUid(uid);
        req.setLocation_code(location_code);
        req.setCode(code);
        req.setFamily_code(family_code);
        req.setTarget_family_code(target_family_code);
        mModel.moveLayer(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().moveLayerSuccess(data);
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
    public void moveBox(String uid, String location_code, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq();
        req.setUid(uid);
        req.setLocation_code(location_code);
        req.setCode(code);
        mModel.moveBox(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().moveBoxSuccess(data);
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
    public void importGoods(String uid, String location_code, String object_codes, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq();
        req.setUid(uid);
        req.setLocation_code(location_code);
        req.setObject_codes(object_codes);
        req.setCode(code);
        mModel.importGoods(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().importGoodsSuccess(data);
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
    public void editBoxName(String uid, String location_code, String name, String path) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq();
        req.setUid(uid);
        req.setLocation_code(location_code);
        req.setName(name);
        if (!TextUtils.isEmpty(path)) {
            req.setBackground_url(path);
            req.setImage_url(path);
        }
        mModel.editBoxName(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().editBoxNameSuccess(data);
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
    public void delBox(String uid, String code) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq();
        req.setUid(uid);
        req.setCode(code);
        mModel.delBox(req, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().delBoxSuccess(data);
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
    public void getFurnitureLayersOrBox(String uid, String location_code, float level, String family_code, String room_id) {
        mModel.getFurnitureLayersOrBox(uid, location_code, level, family_code, room_id, new RequestCallback<RoomFurnitureResponse>() {
            @Override
            public void onSuccess(RoomFurnitureResponse data) {
                if (getUIView() != null) {
                    getUIView().getFurnitureLayersOrBoxSuccess(data);
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
    public void resetLayerName(String uid, String name, String layerCode, String furnitureCode) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        LayerReq layerReq = new LayerReq();
        layerReq.setUid(uid);
        layerReq.setName(name);
        layerReq.setLayerCode(layerCode);
        layerReq.setFurnitureCode(furnitureCode);
        mModel.resetLayerName(layerReq, new RequestCallback<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().resetLayerNameSuccess(data);
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
    public void addBox(String uid, String location_code, String location_name, String path) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditRoomReq roomReq = new EditRoomReq();
        roomReq.setUid(uid);
        roomReq.setLocation_code(location_code);
        roomReq.setLocation_name(location_name);
        if (!TextUtils.isEmpty(path)) {
            roomReq.setBackground_url(path);
            roomReq.setImage_url(path);
        }
        mModel.addBox(roomReq, new RequestCallback<RoomBean>() {
            @Override
            public void onSuccess(RoomBean data) {
                if (getUIView() != null) {
                    getUIView().hideProgressDialog();
                    getUIView().addBoxSuccess(data);
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
        private static final FurniturePresenter instance = new FurniturePresenter();
    }

    public String getLoction(RoomFurnitureBean bean) {
        if (StringUtils.isEmpty(bean.getParents())) {
            return "未归位";
        }
        Collections.sort(bean.getParents(), new Comparator<BaseBean>() {
            @Override
            public int compare(BaseBean lhs, BaseBean rhs) {
                return lhs.getLevel() - rhs.getLevel();
            }
        });
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < StringUtils.getListSize(bean.getParents()); i++) {
            sb.append(bean.getParents().get(i).getName());
            if (i != bean.getParents().size() - 1) {
                sb.append("/");
            }
        }
        if (bean.isSelect()) {
            sb.append("/");
            sb.append(bean.getName());
        }
        return sb.length() == 0 ? "未归位" : sb.toString();
    }

    public void initBoxData(List<ObjectBean> boxs, List<ObjectBean> datas) {
        if (boxs == null || boxs.size() == 0) return;
        if (datas == null || datas.size() == 0) return;
        for (ObjectBean box : boxs) {
            List<ObjectBean> goods = new ArrayList<>();
            for (ObjectBean bean : datas) {
                for (int j = 0; j < bean.getLocations().size(); j++) {
                    if (goods.size() > 3) continue;
                    if (box.getCode().equals(bean.getLocations().get(j).getCode())) {
                        goods.add(bean);
                    }
                }
            }
            if (goods.size() > 0) {
                box.setGoodsByBox(goods);
            }
        }
    }

    //判断是否为收纳盒内数据
    //是,遍历所有物品数据进行添加(物品数据是所有的物品)
    //不是,遍历总数据集合根据location_code添加(总数据不含收纳盒内物品)
    public void initRoomDataOrBoxData(ObjectBean selectBoxBean, String location_code, List<ObjectBean> objects, List<ObjectBean> mData, List<ObjectBean> mAdapterData) {
        if (selectBoxBean != null) {
            //物品信息,筛选收纳盒内的物品
            for (int i = 0; i < objects.size(); i++) {
                ObjectBean bean = objects.get(i);
                if (bean.getLocations() == null || bean.getLocations().size() <= 0) {
                    continue;
                }
                for (int j = 0; j < bean.getLocations().size(); j++) {
                    if (location_code.equals(bean.getLocations().get(j).getCode())) {
                        bean.setSelect(false);
                        mAdapterData.add(bean);
                    }
                }
            }
        } else {
            //筛选隔层内的收纳盒和物品
            for (int i = 0; i < mData.size(); i++) {
                ObjectBean bean = mData.get(i);
                if (bean.getLocations() == null || bean.getLocations().size() <= 0) {
                    continue;
                }
                for (int j = 0; j < bean.getLocations().size(); j++) {
                    if (location_code.equals(bean.getLocations().get(j).getCode())) {
                        bean.setSelect(false);
                        mAdapterData.add(bean);
                    }
                }
            }
        }
    }

    //初始化所有数据
    public void initData(RoomFurnitureGoodsBean data, List<ObjectBean> mData, List<ObjectBean> mBoxlist, List<ObjectBean> objects) {
        //总数据添加收纳盒
        mData.addAll(data.getLocations());
        //收纳盒集合添加盒子信息
        mBoxlist.addAll(data.getLocations());
        if (data.getObjects() != null && data.getObjects().size() > 0) {
            //物品集合添加物品信息
            objects.addAll(data.getObjects());
            //填充box item数据最多显示4个
            initBoxData(mBoxlist, objects);
            for (ObjectBean goodsBean : objects) {
                if (goodsBean.getLocations() != null && goodsBean.getLocations().size() <= 4) {
                    //总数据添加物品信息,当物品是收纳盒内的,不进行添加
                    mData.add(goodsBean);
                }
            }
        }
    }

    //初始化总数据
    public void initRoomData(List<ObjectBean> mData, List<ObjectBean> mBoxlist, List<ObjectBean> objects) {
        mData.clear();
        //总数据添加收纳盒
        mData.addAll(mBoxlist);
        if (objects != null && objects.size() > 0) {
            for (ObjectBean goodsBean : objects) {
                if (goodsBean.getLocations() != null && goodsBean.getLocations().size() <= 4) {
                    //总数据添加物品信息,当物品是收纳盒内的,不进行添加
                    mData.add(goodsBean);
                }
            }
        }
    }

}
