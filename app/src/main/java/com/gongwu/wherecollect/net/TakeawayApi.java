package com.gongwu.wherecollect.net;

import com.gongwu.wherecollect.net.entity.base.ResponseBase;
import com.gongwu.wherecollect.net.entity.response.FamilyListDetailsBean;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;
import com.gongwu.wherecollect.net.entity.response.MsgBean;
import com.gongwu.wherecollect.net.entity.response.RelationGoodsBean;
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
import com.gongwu.wherecollect.net.entity.response.SharedPersonBean;
import com.gongwu.wherecollect.net.entity.response.SharedLocationBean;
import com.gongwu.wherecollect.net.entity.response.UserBean;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
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

    @GET("api/app/v400/objectList")
    Call<ResponseBase<List<MainGoodsBean>>> getUserGoddsList(@Query("uid") String uid, @Query("family_code") String key);

    @GET("api/app/v400/furniture/layersOrBox")
    Call<ResponseBase<RoomFurnitureResponse>> getFurnitureLayersOrBox(@Query("uid") String uid, @Query("location_code") String location_code, @Query("level") float level, @Query("family_code") String family_code, @Query("room_id") String room_id);

    @FormUrlEncoded
    @POST("api/app/v400/update/layerName")
    Call<ResponseBase<RequestSuccessBean>> resetLayerName(@FieldMap Map<String, String> map);

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

    @GET("api/app/v400/firstCategory/list")
    Call<ResponseBase<List<BaseBean>>> getFirstCategoryList(@Query("uid") String uid);

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

    @FormUrlEncoded
    @POST("api/app/v330/messages")
    Call<ResponseBase<MsgBean>> getShareMsgList(@FieldMap Map<String, String> map);


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
}