package com.gongwu.wherecollect.contract.presenter;

import com.gongwu.wherecollect.base.BasePresenter;
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
import com.gongwu.wherecollect.util.StringUtils;

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
    public void editBoxName(String uid, String location_code, String name) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditGoodsReq req = new EditGoodsReq();
        req.setUid(uid);
        req.setLocation_code(location_code);
        req.setName(name);
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
    public void addBox(String uid, String location_code, String location_name) {
        if (getUIView() != null) {
            getUIView().showProgressDialog();
        }
        EditRoomReq roomReq = new EditRoomReq();
        roomReq.setUid(uid);
        roomReq.setLocation_code(location_code);
        roomReq.setLocation_name(location_name);
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


}
