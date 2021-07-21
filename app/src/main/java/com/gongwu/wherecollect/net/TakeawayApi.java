package com.gongwu.wherecollect.net;

import com.gongwu.wherecollect.net.entity.BarcodeBean;
import com.gongwu.wherecollect.net.entity.base.ResponseBase;
import com.gongwu.wherecollect.net.entity.request.BuyEnergyReq;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;
import com.gongwu.wherecollect.net.entity.response.BarcodeResultBean;
import com.gongwu.wherecollect.net.entity.response.BuyVIPResultBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangDetailBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangListBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.FamilyListDetailsBean;
import com.gongwu.wherecollect.net.entity.response.FeedbackBean;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.MessagePostBean;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;
import com.gongwu.wherecollect.net.entity.response.MsgBean;
import com.gongwu.wherecollect.net.entity.response.RelationGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RemindDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.net.entity.response.ResponseBean;
import com.gongwu.wherecollect.net.entity.response.RoomBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RoomFurnitureResponse;
import com.gongwu.wherecollect.net.entity.response.SerchListBean;
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.net.entity.response.StatisticsBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;
import com.gongwu.wherecollect.net.entity.response.VIPBean;
import com.gongwu.wherecollect.net.entity.response.VersionBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;


public interface TakeawayApi {

    @FormUrlEncoded
    @POST("api/app/v230/object-list")
    Call<ResponseBase<MainGoodsBean>> getGoddsList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/register-test")
    Call<ResponseBase<UserBean>> registerUserTest(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v340/login-with-code")
    Call<ResponseBase<UserBean>> login(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/login")
    Call<ResponseBase<UserBean>> loginbyThirdParty(@Query("version") String code, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/logout-test")
    Call<ResponseBase<ResponseBean>> logoutTest(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v320/add-objects")
    Call<ResponseBase<List<ObjectBean>>> addGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/object-add")
    Call<ResponseBase<ObjectBean>> editGoods(@FieldMap Map<String, String> map);

    @GET("api/app/v420/svg-catpure")
    Call<ResponseBase<String>> getCatpure();

    @FormUrlEncoded
    @POST("api/app/v420/verifycode")
    Call<ResponseBase<RequestSuccessBean>> getCode(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/register")
    Call<ResponseBase<UserBean>> register(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/set-password")
    Call<ResponseBase<RequestSuccessBean>> forgetPWD(@FieldMap Map<String, String> map);

    @GET("api/app/v350/remind-list")
    Call<ResponseBase<RemindListBean>> getRemindList(@Query("uid") String uid, @Query("done") String done, @Query("current_page") int page);

    @GET("qiniu-token")
    Call<ResponseBase<Map<String, Object>>> getQiniuToken(@Query("uid") String uid, @Query("key") String key);

    @GET("api/app/v400/family/list")
    Call<ResponseBase<List<FamilyBean>>> getUserFamily(@Query("uid") String uid, @Query("user_name") String key);

    @GET("api/app/v400/family/roomsAndBesharedUsers")
    Call<ResponseBase<HomeFamilyRoomBean>> getUserFamilyRoom(@Query("uid") String uid, @Query("code") String key);

    @GET("api/app/v400/family/getFamilyRoomList")
    Call<ResponseBase<List<RoomBean>>> getFamilyRoomList(@Query("uid") String uid, @Query("code") String key);

    @GET("api/app/v400/family/getFamilyRoomList")
    Call<ResponseBase<List<FamilyBean>>> getFamilyRoomLists(@Query("uid") String uid, @Query("code") String key);

    @FormUrlEncoded
    @POST("api/app/v350/object-recommend-group-new")
    Call<ResponseBase<List<ChangWangBean>>> getCangWangList(@FieldMap Map<String, String> map);

    @GET("api/app/v400/objectList")
    Call<ResponseBase<List<MainGoodsBean>>> getUserGoddsList(@Query("uid") String uid, @Query("family_code") String key, @Query("darklayer") boolean darklayer);

    @GET("api/app/v400/furniture/layersOrBox")
    Call<ResponseBase<RoomFurnitureResponse>> getFurnitureLayersOrBox(@Query("uid") String uid, @Query("location_code") String location_code, @Query("level") float level, @Query("family_code") String family_code, @Query("room_id") String room_id);

    @FormUrlEncoded
    @POST("api/app/v400/update/layerName")
    Call<ResponseBase<RequestSuccessBean>> resetLayerName(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/objectArchive")
    Call<ResponseBase<RequestSuccessBean>> goodsArchive(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/removeArchiveObjects")
    Call<ResponseBase<RequestSuccessBean>> removeArchiveObjects(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/location/furniture-list")
    Call<ResponseBase<List<FurnitureBean>>> getFurnitureList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/furniture/detail")
    Call<ResponseBase<RoomFurnitureGoodsBean>> getFurnitureDetails(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v230/recommend/color")
    Call<ResponseBase<List<String>>> getColors(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v230/recommend/channel")
    Call<ResponseBase<List<ChannelBean>>> getChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v120/channel/add")
    Call<ResponseBase<ChannelBean>> addChannel(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v230/channel/list")
    Call<ResponseBase<List<ChannelBean>>> getChannelList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/category-search")
    Call<ResponseBase<List<ChannelBean>>> getSearchSort(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/saveCustomCate")
    Call<ResponseBase<BaseBean>> saveCustomCate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v430/belonger/add")
    Call<ResponseBase<BaseBean>> saveBelonger(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v430/belonger/delete")
    Call<ResponseBase<RequestSuccessBean>> deleteBelonger(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/edit/customCate")
    Call<ResponseBase<RequestSuccessBean>> editCustomCate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/delete/customCate")
    Call<ResponseBase<RequestSuccessBean>> deleteCustomCate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/saveCustomSubCate")
    Call<ResponseBase<BaseBean>> saveCustomSubCate(@FieldMap Map<String, String> map);

    @GET("api/app/v400/firstCategory/list")
    Call<ResponseBase<List<BaseBean>>> getFirstCategoryList(@Query("uid") String uid, @Query("type") String type);

    @GET("api/app/v430/belonger/list")
    Call<ResponseBase<List<BaseBean>>> getBelongerList(@Query("uid") String uid);

    @GET("api/app/v400/subCategory/list")
    Call<ResponseBase<List<BaseBean>>> getSubCategoryList(@Query("uid") String uid, @Query("parent_code") String parent_code, @Query("type") String type);

    @GET("api/app/v400/rec-category/list")
    Call<ResponseBase<List<ChannelBean>>> getCategoryDetails(@Query("uid") String uid, @Query("code") String code);

    @FormUrlEncoded
    @POST("api/app/v320/add-objects")
    Call<ResponseBase<List<ObjectBean>>> addMoreGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v120/import-object/isbn")
    Call<ResponseBase<BookBean>> getBookInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v120/import-object/taobao")
    Call<ResponseBase<BookBean>> getTaobaoInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v350/remind")
    Call<ResponseBase<RequestSuccessBean>> addRemind(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v350/remind-update")
    Call<ResponseBase<RequestSuccessBean>> updateRemind(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v350/remind-delete")
    Call<ResponseBase<RequestSuccessBean>> deteleRemind(@FieldMap Map<String, String> map);

    @GET("api/app/v350/remind-done")
    Call<ResponseBase<RequestSuccessBean>> setRemindDone(@Query("uid") String uid, @Query("remind_id") String remindId);

    @GET("api/app/v350/remind-detail")
    Call<ResponseBase<RemindDetailsBean>> getRemindDetails(@Query("uid") String uid, @Query("remind_id") String remindId, @Query("associated_object_id") String associated_object_id);

    @GET("api/app/v350/object-list")
    Call<ResponseBase<RelationGoodsBean>> getRelationGoodsList(@Query("uid") String uid, @Query("category_code") String code,
                                                               @Query("keyword") String keyword, @Query("current_page") int page);

    @GET("api/app/v350/categories")
    Call<ResponseBase<List<BaseBean>>> getRelationCategories(@Query("uid") String uid);

    @FormUrlEncoded
    @POST("api/app/v330/a/get-all-shared-users")
    Call<ResponseBase<List<SharedPersonBean>>> getSharedUsersList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v330/a/get-all-shared-locations")
    Call<ResponseBase<List<SharedLocationBean>>> getSharedLocations(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v330/search-user-history")
    Call<ResponseBase<List<SharedPersonBean>>> getSharePersonOldList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v330/user-info-by-id")
    Call<ResponseBase<SharedPersonBean>> getShareUserCodeInfo(@FieldMap Map<String, String> map);

    @GET("api/app/v400/family/collaboratorRoomList")
    Call<ResponseBase<List<BaseBean>>> getShareRoomList(@Query("uid") String uid, @Query("family_code") String family_code, @Query("be_shared_user_id") String be_shared_user_id);

    @FormUrlEncoded
    @POST("api/app/v400/family/shareLocation2user")
    Call<ResponseBase<RequestSuccessBean>> setShareLocation(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v330/a/discontinue")
    Call<ResponseBase<RequestSuccessBean>> closeShareUser(@FieldMap Map<String, String> map);

    @GET("api/app/v330/messages")
    Call<ResponseBase<MsgBean>> getShareMsgList(@Query("uid") String uid, @Query("type") String type);


    @FormUrlEncoded
    @POST
    Call<ResponseBase<RequestSuccessBean>> dealWithShareRequest(@Url String url, @FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/location/update")
    Call<ResponseBase<FurnitureBean>> updataFurniture(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/furniture/delete")
    Call<ResponseBase<RequestSuccessBean>> deleteFurniture(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/furniture/top")
    Call<ResponseBase<RequestSuccessBean>> topFurniture(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/family/furnitures/move")
    Call<ResponseBase<RequestSuccessBean>> moveFurniture(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v120/update-location-list-position")
    Call<ResponseBase<List<RoomBean>>> updataRoomPosition(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/location-add")
    Call<ResponseBase<RoomBean>> addRoom(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v340/no-location-search")
    Call<ResponseBase<ImportGoodsBean>> getImportGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/location-del")
    Call<ResponseBase<RequestSuccessBean>> delRoom(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/location/edit")
    Call<ResponseBase<RequestSuccessBean>> editRoom(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/family/rooms/move")
    Call<ResponseBase<RequestSuccessBean>> moveRoom(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/furniture/list")
    Call<ResponseBase<List<FurnitureTemplateBean>>> getTemplateFurnitureList(@FieldMap Map<String, String> map);

    @GET("api/app/v400/layer-template/list")
    Call<ResponseBase<LayerTemplateBean>> getTemplateLayerList(@Query("uid") String uid, @Query("system_furniture_code") String system_furniture_code);

    @FormUrlEncoded
    @POST("api/app/v300/location/add")
    Call<ResponseBase<FurnitureBean>> addFurniture(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/location/recommend-list")
    Call<ResponseBase<List<RoomFurnitureBean>>> getRoomsTemplate(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/family/createFamilyAndAddRooms")
    Call<ResponseBase<RequestSuccessBean>> createFamily(@FieldMap Map<String, String> map);

    @GET("api/app/v400/family/myFamilyList")
    Call<ResponseBase<MyFamilyListBean>> getFamilyList(@Query("uid") String uid);

    @GET("api/app/v400/family/roomsAndBesharedUsers")
    Call<ResponseBase<FamilyListDetailsBean>> getFamilyDetails(@Query("uid") String uid, @Query("code") String code);

    @GET("api/app/v400/family/delete")
    Call<ResponseBase<RequestSuccessBean>> delFamily(@Query("uid") String uid, @Query("code") String code);

    @GET("api/app/v400/family/edit")
    Call<ResponseBase<RequestSuccessBean>> editFamily(@Query("uid") String uid, @Query("code") String code, @Query("name") String name);

    @GET("api/app/v400/family/disShare")
    Call<ResponseBase<RequestSuccessBean>> disShareFamily(@Query("uid") String uid, @Query("code") String code);

    @GET("api/app/v400/family/familyBeSharedUserList")
    Call<ResponseBase<List<SharedPersonBean>>> getShareUserByFamily(@Query("uid") String uid, @Query("code") String code);

    @GET("api/app/v400/family/delCollaborator")
    Call<ResponseBase<RequestSuccessBean>> delCollaborator(@Query("uid") String uid, @Query("be_shared_user_id") String code);

    @FormUrlEncoded
    @POST("api/app/v400/family/shareOrCancelShareRooms")
    Call<ResponseBase<RequestSuccessBean>> shareOrCancelShareRooms(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/location/import-objects")
    Call<ResponseBase<RequestSuccessBean>> importGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v340/object/multiple/delete")
    Call<ResponseBase<RequestSuccessBean>> delSelectGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v400/objects/top")
    Call<ResponseBase<RequestSuccessBean>> topSelectGoods(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/location/edit")
    Call<ResponseBase<RequestSuccessBean>> editBoxName(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/location/delete")
    Call<ResponseBase<RequestSuccessBean>> delBox(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/location/move-layer")
    Call<ResponseBase<RequestSuccessBean>> moveLayer(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v300/location/move")
    Call<ResponseBase<RequestSuccessBean>> moveBox(@FieldMap Map<String, String> map);

    @GET("api/app/v400/batchOpsObjectList")
    Call<ResponseBase<List<ObjectBean>>> getEditMoreGoodsList(@Query("uid") String uid, @Query("family_code") String family_code, @Query("category_code") String code);

    @FormUrlEncoded
    @POST("api/app/v400/objectsAddCategory")
    Call<ResponseBase<RequestSuccessBean>> objectsAddCategory(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v340/search")
    Call<ResponseBase<SerchListBean>> getSearchList(@FieldMap Map<String, String> map);

    @GET("api/app/v330/messages")
    Call<ResponseBase<MessagePostBean>> getMessagesList(@Query("uid") String uid, @Query("page") int page, @Query("content_type") String content_type);

    @FormUrlEncoded
    @POST("api/app/v300/location/remove-object")
    Call<ResponseBase<RequestSuccessBean>> removeObjectFromFurnitrue(@FieldMap Map<String, String> map);


    @GET("api/app/v350/get-reminds-by-objid")
    Call<ResponseBase<List<RemindBean>>> getGoodsRemindsById(@Query("uid") String uid, @Query("obj_id") String obj_id);

    @FormUrlEncoded
    @POST("users/object-del")
    Call<ResponseBase<RequestSuccessBean>> delGoods(@FieldMap Map<String, String> map);

    @GET("api/app/v420/vip-price")
    Call<ResponseBase<VIPBean>> getVIPPrice(@Query("uid") String uid);

    @GET("api/app/v420/vorder")
    Call<ResponseBase<BuyVIPResultBean>> buyVipWXOrAli(@Query("uid") String uid, @Query("price") int price, @Query("type") String type, @Query("couponId") String couponId);

    @FormUrlEncoded
    @POST("api/app/v440/vorder")
    Call<ResponseBase<BuyVIPResultBean>> buyEnergy(@FieldMap Map<String, String> map);

    @GET("users/user-info")
    Call<ResponseBase<UserBean>> getUserInfo(@Query("uid") String uid);

    @GET("api/app/v440/promotion2energy")
    Call<ResponseBase<RequestSuccessBean>> getEnergyCode(@Query("uid") String uid, @Query("code") String code);

    @FormUrlEncoded
    @POST("users/feedback")
    Call<ResponseBase<FeedbackBean>> feedBack(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/edit")
    Call<ResponseBase<RequestSuccessBean>> editInfo(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/set-phone")
    Call<ResponseBase<RequestSuccessBean>> changePhone(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/is-registered")
    Call<ResponseBase<RequestSuccessBean>> isRegistered(@FieldMap Map<String, String> map);

    @GET("api/app/v420/app/share")
    Call<ResponseBase<RequestSuccessBean>> sharedApp(@Query("uid") String uid, @Query("share_type") String share_type);

    @GET("api/app/v420/app/notify")
    Call<ResponseBase<RequestSuccessBean>> notificationServer(@Query("uid") String uid, @Query("pay_type") String pay_type, @Query("order_no") String order_no);

    @GET("api/app/v420/bind-check")
    Call<ResponseBase<RequestSuccessBean>> bindCheck(@Query("uid") String uid, @Query("openid") String openid, @Query("type") String type);

    @FormUrlEncoded
    @POST("api/app/v420/bind-account")
    Call<ResponseBase<RequestSuccessBean>> bindAccount(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("users/change-password")
    Call<ResponseBase<RequestSuccessBean>> changePassword(@FieldMap Map<String, String> map);

    @GET("api/app/v420/app/version/check")
    Call<ResponseBase<VersionBean>> getVersion(@Query("system_name") String system_name, @Query("app_version") String app_version);

    @FormUrlEncoded
    @POST("api/app/v350/object-recommend-group-objects")
    Call<ResponseBase<ChangWangListBean>> getCangWangGoodsList(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v350/object-recommend-option")
    Call<ResponseBase<ChangWangDetailBean>> setCangWangDetail(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v430/object/setWeight")
    Call<ResponseBase<RequestSuccessBean>> setGoodsWeight(@FieldMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/app/v440/object/untop")
    Call<ResponseBase<RequestSuccessBean>> setGoodsNoWeight(@FieldMap Map<String, String> map);


    @POST("api/app/v440/vorder-cancel")
    Call<ResponseBase<RequestSuccessBean>> cancelWX(@Body BuyEnergyReq barcodeBean);

    @POST("api/app/v440/barcode")
    Call<ResponseBase<BarcodeResultBean>> getGoodsByBarcode(@Body BarcodeBean barcodeBean);

    @POST("api/app/v440/taokouling")
    Call<ResponseBase<BarcodeResultBean>> getGoodsByTBbarcode(@Body BarcodeBean barcodeBean);

    @GET("api/app/v420/statics1")
    Call<ResponseBase<List<StatisticsBean>>> getGoodsReturnDetails(@Query("uid") String uid, @Query("family_code") String family_code, @Query("code") String code);

    @GET("api/app/v420/statics2")
    Call<ResponseBase<List<StatisticsBean>>> getGoodsSortDetails(@Query("uid") String uid, @Query("family_code") String family_code);

    @GET("api/app/v420/statics3")
    Call<ResponseBase<List<StatisticsBean>>> getGoodsColorsDetails(@Query("uid") String uid, @Query("family_code") String family_code);

    @GET("api/app/v420/statics4")
    Call<ResponseBase<List<StatisticsBean>>> getGoodsSeasonDetails(@Query("uid") String uid, @Query("family_code") String family_code);

    @GET("api/app/v420/statics5")
    Call<ResponseBase<List<StatisticsBean>>> getGoodsPriceDetails(@Query("uid") String uid, @Query("family_code") String family_code, @Query("code") String code);

    @GET("api/app/v420/statics6")
    Call<ResponseBase<List<StatisticsBean>>> getGoodsTimeDetails(@Query("uid") String uid, @Query("family_code") String family_code, @Query("code") String code);

    @GET("api/app/v420/export-furniture")
    Call<ResponseBase<DetailedListBean>> getDetailedList(@Query("uid") String uid, @Query("family_code") String family_code, @Query("room_code") String room_code, @Query("furniture_code") String furniture_code);

    @GET("article/list")
    Call<ResponseBase<List<ArticleBean>>> getArticList();

    @GET("api/app/v440/energyPrice/list")
    Call<ResponseBase<EnergyPriceBean>> getEnergyPrice(@Query("uid") String uid);


}
