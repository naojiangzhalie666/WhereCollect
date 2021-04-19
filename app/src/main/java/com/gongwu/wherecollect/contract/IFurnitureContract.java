package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.EditRoomReq;
import com.gongwu.wherecollect.net.entity.request.FurnitureDetailsReq;
import com.gongwu.wherecollect.net.entity.request.EditGoodsReq;
import com.gongwu.wherecollect.net.entity.request.LayerReq;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;

import java.util.List;

public interface IFurnitureContract {
    interface IFurnitureModel {

        void getFurnitureDetails(FurnitureDetailsReq req, final RequestCallback callback);

        void getImportGoodsList(EditGoodsReq req, final RequestCallback callback);

        void importGoods(EditGoodsReq req, final RequestCallback callback);

        void delSelectGoods(EditGoodsReq req, final RequestCallback callback);

        void topSelectGoods(EditGoodsReq req, final RequestCallback callback);

        void editBoxName(EditGoodsReq req, final RequestCallback callback);

        void delBox(EditGoodsReq req, final RequestCallback callback);

        void moveLayer(EditGoodsReq req, final RequestCallback callback);

        void moveBox(EditGoodsReq req, final RequestCallback callback);

        void getFurnitureLayersOrBox(String uid, String location_code, float level, String family_code, String room_id, final RequestCallback callback);

        void resetLayerName(LayerReq req, final RequestCallback callback);

        void addBox(EditRoomReq roomReq, final RequestCallback callback);
    }

    interface IFurniturePresenter {
        void getFurnitureDetails(String uid, String location_code, String family_code);

        void getImportGoodsList(String uid, String location_code);

        void delSelectGoods(String uid, String ids);

        void topSelectGoods(String uid, String furnitureCode, List<String> objectIds);

        void moveLayer(String uid, String location_code, String code, String family_code, String target_family_code);

        void moveBox(String uid, String location_code, String code);

        void importGoods(String uid, String location_code, String object_codes, String code);

        void editBoxName(String uid, String location_code, String name, String path);

        void delBox(String uid, String code);

        void getFurnitureLayersOrBox(String uid, String location_code, float level, String family_code, String room_id);

        void resetLayerName(String uid, String name, String layerCode, String furnitureCode);

        void addBox(String uid, String location_code, String location_name, String path);
    }

    interface IFurnitureView extends BaseView {
        void getFurnitureDetailsSuccess(RoomFurnitureGoodsBean data);

        void getImportGoodsListSuccess(ImportGoodsBean bean);

        void delSelectGoodsSuccess(RequestSuccessBean bean);

        void topSelectGoodsSuccess(RequestSuccessBean bean);

        void importGoodsSuccess(RequestSuccessBean bean);

        void editBoxNameSuccess(RequestSuccessBean bean);

        void delBoxSuccess(RequestSuccessBean bean);

        void moveLayerSuccess(RequestSuccessBean bean);

        void moveBoxSuccess(RequestSuccessBean bean);

        void getFurnitureLayersOrBoxSuccess(RoomFurnitureResponse data);

        void resetLayerNameSuccess(RequestSuccessBean bean);

        void addBoxSuccess(RoomBean rooms);

        void onUpLoadSuccess(String path);
    }
}
