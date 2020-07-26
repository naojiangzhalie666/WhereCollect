package com.gongwu.wherecollect.contract.model;

import com.gongwu.wherecollect.contract.IFurnitureContract;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.EditRoomReq;
import com.gongwu.wherecollect.net.entity.request.FurnitureDetailsReq;
import com.gongwu.wherecollect.net.entity.request.LayerReq;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.util.ApiUtils;

public class FurnitureModel implements IFurnitureContract.IFurnitureModel {
    @Override
    public void getFurnitureDetails(FurnitureDetailsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getFurnitureDetails(req, new ApiCallBack<RoomFurnitureGoodsBean>() {
            @Override
            public void onSuccess(RoomFurnitureGoodsBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getImportGoodsList(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getImportGoods(req, new ApiCallBack<ImportGoodsBean>() {
            @Override
            public void onSuccess(ImportGoodsBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void importGoods(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.importGoods(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void delSelectGoods(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.delSelectGoods(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void topSelectGoods(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.topSelectGoods(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void editBoxName(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.editBoxName(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void delBox(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.delBox(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void moveLayer(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.moveLayer(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void moveBox(EditGoodsReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.moveBox(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void getFurnitureLayersOrBox(String uid, String location_code, float level, String family_code, String room_id, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.getFurnitureLayersOrBox(uid, location_code, level, family_code, room_id, new ApiCallBack<RoomFurnitureResponse>() {
            @Override
            public void onSuccess(RoomFurnitureResponse data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void resetLayerName(LayerReq req, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.resetLayerName(req, new ApiCallBack<RequestSuccessBean>() {
            @Override
            public void onSuccess(RequestSuccessBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }

    @Override
    public void addBox(EditRoomReq roomReq, RequestCallback callback) {
        if (callback == null) return;
        ApiUtils.addRoom(roomReq, new ApiCallBack<RoomBean>() {
            @Override
            public void onSuccess(RoomBean data) {
                callback.onSuccess(data);
            }

            @Override
            public void onFailed(String msg) {
                callback.onFailure(msg);
            }
        });
    }
}
