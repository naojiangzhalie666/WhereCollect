package com.gongwu.wherecollect.util;

import com.gongwu.wherecollect.contract.AppConstant;
import com.gongwu.wherecollect.net.ApiCallBack;
import com.gongwu.wherecollect.net.ApiInstance;
import com.gongwu.wherecollect.net.entity.BarcodeBean;
import com.gongwu.wherecollect.net.entity.base.RequestBase;
import com.gongwu.wherecollect.net.entity.request.BuyEnergyReq;
import com.gongwu.wherecollect.net.entity.response.ArticleBean;
import com.gongwu.wherecollect.net.entity.response.BarcodeResultBean;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.BookBean;
import com.gongwu.wherecollect.net.entity.response.BuyVIPResultBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangDetailBean;
import com.gongwu.wherecollect.net.entity.response.ChangWangListBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;
import com.gongwu.wherecollect.net.entity.response.DetailedListBean;
import com.gongwu.wherecollect.net.entity.response.EnergyPriceBean;
import com.gongwu.wherecollect.net.entity.response.FamilyBean;
import com.gongwu.wherecollect.net.entity.response.FamilyListDetailsBean;
import com.gongwu.wherecollect.net.entity.response.FeedbackBean;
import com.gongwu.wherecollect.net.entity.response.ImportGoodsBean;
import com.gongwu.wherecollect.net.entity.response.MessagePostBean;
import com.gongwu.wherecollect.net.entity.response.MyFamilyListBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureTemplateBean;
import com.gongwu.wherecollect.net.entity.response.HomeFamilyRoomBean;
import com.gongwu.wherecollect.net.entity.response.FurnitureBean;
import com.gongwu.wherecollect.net.entity.response.LayerTemplateBean;
import com.gongwu.wherecollect.net.entity.response.MainGoodsBean;
import com.gongwu.wherecollect.net.entity.response.MsgBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;
import com.gongwu.wherecollect.net.entity.response.RelationGoodsBean;
import com.gongwu.wherecollect.net.entity.response.RemindBean;
import com.gongwu.wherecollect.net.entity.response.RemindDetailsBean;
import com.gongwu.wherecollect.net.entity.response.RemindListBean;
import com.gongwu.wherecollect.net.entity.response.RequestSuccessBean;
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
import java.util.TreeMap;

/**
 * @author pengqm
 * @name FilmApplication
 * @class name：com.baidu.iov.dueros.film.utils
 * @time 2018/10/10 10:43
 * @change
 * @class describe
 */

public class ApiUtils {

    private static final String TAG = "ApiUtils";

    private static <D extends RequestBase> Map<String, String> requestPrepare(D request) {
        return CommonUtils.getAllFields(request);
    }

    /**
     * 注册测试账号
     */
    public static <D extends RequestBase> void registerUserTest(D request, ApiCallBack<UserBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().registerUserTest(requestMap).enqueue(callBack);
    }

    /**
     * 登录
     */
    public static <D extends RequestBase> void login(D request, ApiCallBack<UserBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().login(requestMap).enqueue(callBack);
    }

    /**
     * 第三方登录
     */
    public static <D extends RequestBase> void loginbyThirdParty(D request, String code, ApiCallBack<UserBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().loginbyThirdParty(code, requestMap).enqueue(callBack);
    }

    /**
     * 注销测试账号
     */
    public static <D extends RequestBase> void logoutTest(D request, ApiCallBack<ResponseBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().logoutTest(requestMap).enqueue(callBack);
    }

    /**
     * 手机登录获取验证码
     */
    public static <D extends RequestBase> void getCode(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCode(requestMap).enqueue(callBack);
    }

    /**
     * 邮箱注册
     */
    public static <D extends RequestBase> void register(D request, ApiCallBack<UserBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().register(requestMap).enqueue(callBack);
    }

    /**
     * 修改手机账号密码
     */
    public static <D extends RequestBase> void forgetPWD(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().forgetPWD(requestMap).enqueue(callBack);
    }

    /**
     * 图形验证
     *
     * @param callBack
     * @param <D>
     */
    public static <D extends RequestBase> void getCatpure(ApiCallBack<String> callBack) {
        ApiInstance.getApi().getCatpure().enqueue(callBack);
    }

    /**
     * 获取用户物品列表
     */
    public static <D extends RequestBase> void getGoddsList(D request, ApiCallBack<MainGoodsBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getGoddsList(requestMap).enqueue(callBack);
    }

    /**
     * 获取用户提醒列表
     */
    public static <D extends RequestBase> void getRemindList(String uid, String done, int page, ApiCallBack<RemindListBean> callBack) {
        ApiInstance.getApi().getRemindList(uid, done, page).enqueue(callBack);
    }

    /**
     * 获取七牛云token
     */
    public static <D extends RequestBase> void getQiniuToken(String uid, String key, ApiCallBack<Map<String, Object>> callBack) {
        ApiInstance.getApi().getQiniuToken(uid, key).enqueue(callBack);
    }

    /**
     * 添加物品
     */
    public static <D extends RequestBase> void addGoods(D request, ApiCallBack<List<ObjectBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addGoods(requestMap).enqueue(callBack);
    }

    /**
     * 编辑物品
     */
    public static <D extends RequestBase> void editGoods(D request, ApiCallBack<ObjectBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().editGoods(requestMap).enqueue(callBack);
    }


    /**
     * 获取用户家庭 4.0
     */
    public static <D extends RequestBase> void getUserFamily(String uid, String user_name, ApiCallBack<List<FamilyBean>> callBack) {
        ApiInstance.getApi().getUserFamily(uid, user_name).enqueue(callBack);
    }

    /**
     * 获取房间列表信息 4.0
     */
    public static <D extends RequestBase> void getUserFamilyRoom(String uid, String code, ApiCallBack<HomeFamilyRoomBean> callBack) {
        ApiInstance.getApi().getUserFamilyRoom(uid, code).enqueue(callBack);
    }

    /**
     * 获得某个家庭下的房间列表
     */
    public static <D extends RequestBase> void getFamilyRoomList(String uid, String code, ApiCallBack<List<RoomBean>> callBack) {
        ApiInstance.getApi().getFamilyRoomList(uid, code).enqueue(callBack);
    }

    public static <D extends RequestBase> void getFamilyRoomLists(String uid, String code, ApiCallBack<List<FamilyBean>> callBack) {
        ApiInstance.getApi().getFamilyRoomLists(uid, code).enqueue(callBack);
    }

    /**
     * 获取常忘物品列表
     */
    public static <D extends RequestBase> void getCangWangList(D request, ApiCallBack<List<ChangWangBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCangWangList(requestMap).enqueue(callBack);
    }

    /**
     * 获取用户物品列表
     */
    public static <D extends RequestBase> void getUserGoddsList(String uid, String family_code, boolean darklayer, ApiCallBack<List<MainGoodsBean>> callBack) {
        ApiInstance.getApi().getUserGoddsList(uid, family_code, darklayer).enqueue(callBack);
    }

    /**
     * 获取家具下的隔层布局
     */
    public static <D extends RequestBase> void getFurnitureLayersOrBox(String uid, String location_code, float level, String family_code, String room_id, ApiCallBack<RoomFurnitureResponse> callBack) {
        ApiInstance.getApi().getFurnitureLayersOrBox(uid, location_code, level, family_code, room_id).enqueue(callBack);
    }

    /**
     * 更改隔层名称
     */
    public static <D extends RequestBase> void resetLayerName(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().resetLayerName(requestMap).enqueue(callBack);
    }

    /**
     * 获取房间内家具信息
     */
    public static <D extends RequestBase> void getFurnitureList(D request, ApiCallBack<List<FurnitureBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getFurnitureList(requestMap).enqueue(callBack);
    }

    /**
     * 获取家具详情
     */
    public static <D extends RequestBase> void getFurnitureDetails(D request, ApiCallBack<RoomFurnitureGoodsBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getFurnitureDetails(requestMap).enqueue(callBack);
    }

    /**
     * 获取物品颜色
     */
    public static <D extends RequestBase> void getColors(D request, ApiCallBack<List<String>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getColors(requestMap).enqueue(callBack);
    }

    /**
     * 获取购买渠道
     */
    public static <D extends RequestBase> void getChannel(D request, ApiCallBack<List<ChannelBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getChannel(requestMap).enqueue(callBack);
    }

    /**
     * 添加购买渠道
     */
    public static <D extends RequestBase> void addChannel(D request, ApiCallBack<ChannelBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addChannel(requestMap).enqueue(callBack);
    }

    /**
     * 获取物品归属集合
     */
    public static <D extends RequestBase> void getChannelList(D request, ApiCallBack<List<ChannelBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getChannelList(requestMap).enqueue(callBack);
    }

    /**
     * 搜索分类
     */
    public static <D extends RequestBase> void getSearchSort(D request, ApiCallBack<List<ChannelBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getSearchSort(requestMap).enqueue(callBack);
    }

    /**
     * 自定义一级分类
     */
    public static <D extends RequestBase> void saveCustomCate(D request, ApiCallBack<BaseBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().saveCustomCate(requestMap).enqueue(callBack);
    }

    /**
     * 自定义归属人
     */
    public static <D extends RequestBase> void saveBelonger(D request, ApiCallBack<BaseBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().saveBelonger(requestMap).enqueue(callBack);
    }

    /**
     * 编辑自定义
     */
    public static <D extends RequestBase> void editCustomCate(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().editCustomCate(requestMap).enqueue(callBack);
    }

    /**
     * 删除自定义
     */
    public static <D extends RequestBase> void deleteCustomCate(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().deleteCustomCate(requestMap).enqueue(callBack);
    }

    public static <D extends RequestBase> void deleteBelonger(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().deleteBelonger(requestMap).enqueue(callBack);
    }

    /**
     * 自定义二级分类
     */
    public static <D extends RequestBase> void saveCustomSubCate(D request, ApiCallBack<BaseBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().saveCustomSubCate(requestMap).enqueue(callBack);
    }

    /**
     * 获取归属人列表
     */
    public static <D extends RequestBase> void getBelongerList(String uid, ApiCallBack<List<BaseBean>> callBack) {
        ApiInstance.getApi().getBelongerList(uid).enqueue(callBack);
    }

    /**
     * 获取物品一级分类
     */
    public static <D extends RequestBase> void getFirstCategoryList(String uid, ApiCallBack<List<BaseBean>> callBack) {
        ApiInstance.getApi().getFirstCategoryList(uid, "").enqueue(callBack);
    }

    /**
     * 获取购买渠道一级分类
     */
    public static <D extends RequestBase> void getBuyFirstCategoryList(String uid, ApiCallBack<List<BaseBean>> callBack) {
        ApiInstance.getApi().getFirstCategoryList(uid, AppConstant.BUY_TYPE).enqueue(callBack);
    }

    /**
     * 根据parentCode获取下一级分类
     */
    public static <D extends RequestBase> void getSubCategoryList(String uid, String parentCode, String type, ApiCallBack<List<BaseBean>> callBack) {
        ApiInstance.getApi().getSubCategoryList(uid, parentCode, type).enqueue(callBack);
    }

    /**
     * 根据一级分类 获取物品二级分类
     */
    public static <D extends RequestBase> void getCategoryDetails(String uid, String code, ApiCallBack<List<ChannelBean>> callBack) {
        ApiInstance.getApi().getCategoryDetails(uid, code).enqueue(callBack);
    }

    /**
     * 批量添加
     */
    public static <D extends RequestBase> void addMoreGoods(D request, ApiCallBack<List<ObjectBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addMoreGoods(requestMap).enqueue(callBack);
    }

    /**
     * 扫码获取书本信息
     */
    public static <D extends RequestBase> void getBookInfo(D request, ApiCallBack<BookBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getBookInfo(requestMap).enqueue(callBack);
    }

    /**
     * 扫码获取网店物品信息
     */
    public static <D extends RequestBase> void getTaobaoInfo(D request, ApiCallBack<BookBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getTaobaoInfo(requestMap).enqueue(callBack);
    }

    /**
     * 添加提醒
     */
    public static <D extends RequestBase> void addRemind(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addRemind(requestMap).enqueue(callBack);
    }

    /**
     * 更新提醒
     */
    public static <D extends RequestBase> void updateRemind(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().updateRemind(requestMap).enqueue(callBack);
    }

    /**
     * 删除提醒
     */
    public static <D extends RequestBase> void deteleRemind(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().deteleRemind(requestMap).enqueue(callBack);
    }

    /**
     * 标记提醒已完成
     */
    public static <D extends RequestBase> void setRemindDone(String uid, String remindId, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().setRemindDone(uid, remindId).enqueue(callBack);
    }

    /**
     * 获取提醒详情
     */
    public static <D extends RequestBase> void getRemindDetails(String uid, String remindId, String associatedObjectId, ApiCallBack<RemindDetailsBean> callBack) {
        ApiInstance.getApi().getRemindDetails(uid, remindId, associatedObjectId).enqueue(callBack);
    }

    public static <D extends RequestBase> void getRelationGoodsList(String uid, String category_code, String keyword, int page, ApiCallBack<RelationGoodsBean> callBack) {
        ApiInstance.getApi().getRelationGoodsList(uid, category_code, keyword, page).enqueue(callBack);
    }

    public static <D extends RequestBase> void getRelationCategories(String uid, ApiCallBack<List<BaseBean>> callBack) {
        ApiInstance.getApi().getRelationCategories(uid).enqueue(callBack);
    }

    /**
     * 获取共享人列表
     */
    public static <D extends RequestBase> void getSharedUsersList(D request, ApiCallBack<List<SharedPersonBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getSharedUsersList(requestMap).enqueue(callBack);
    }

    /**
     * 获取共享空间列表
     */
    public static <D extends RequestBase> void getSharedLocations(D request, ApiCallBack<List<SharedLocationBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getSharedLocations(requestMap).enqueue(callBack);
    }

    /**
     * 获取添加共享人历史列表
     */
    public static <D extends RequestBase> void getSharePersonOldList(D request, ApiCallBack<List<SharedPersonBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getSharePersonOldList(requestMap).enqueue(callBack);
    }

    /**
     * 扫码获取分享用户信息
     */
    public static <D extends RequestBase> void getShareUserCodeInfo(D request, ApiCallBack<SharedPersonBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getShareUserCodeInfo(requestMap).enqueue(callBack);
    }

    /**
     * 获取分享房间list
     */
    public static <D extends RequestBase> void getShareRoomList(String uid, String family_code, String be_shared_user_id, ApiCallBack<List<BaseBean>> callBack) {
        ApiInstance.getApi().getShareRoomList(uid, family_code, be_shared_user_id).enqueue(callBack);
    }

    /**
     * 邀请共享
     */
    public static <D extends RequestBase> void setShareLocation(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().setShareLocation(requestMap).enqueue(callBack);
    }

    /**
     * 断开共享
     */
    public static <D extends RequestBase> void closeShareUser(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().closeShareUser(requestMap).enqueue(callBack);
    }


    /**
     * 获取共享消息列表
     */
    public static <D extends RequestBase> void getShareMsgList(String uid, ApiCallBack<MsgBean> callBack) {
        ApiInstance.getApi().getShareMsgList(uid, "0").enqueue(callBack);
    }

    /**
     * 处理共享请求
     */
    public static <D extends RequestBase> void dealWithShareRequest(String uid, String url, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = new TreeMap<>();
        requestMap.put("uid", uid);
        ApiInstance.getApi().dealWithShareRequest(url, requestMap).enqueue(callBack);
    }

    /**
     * 更新家具布局
     */
    public static <D extends RequestBase> void updataFurniture(D request, ApiCallBack<FurnitureBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().updataFurniture(requestMap).enqueue(callBack);
    }

    /**
     * 删除家具
     */
    public static <D extends RequestBase> void deleteFurniture(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().deleteFurniture(requestMap).enqueue(callBack);
    }

    /**
     * 置顶家具
     */
    public static <D extends RequestBase> void topFurniture(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().topFurniture(requestMap).enqueue(callBack);
    }

    /**
     * 移动家具
     */
    public static <D extends RequestBase> void moveFurniture(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().moveFurniture(requestMap).enqueue(callBack);
    }

    /**
     * 移动空间房间排序
     */
    public static <D extends RequestBase> void updataRoomPosition(D request, ApiCallBack<List<RoomBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().updataRoomPosition(requestMap).enqueue(callBack);
    }

    /**
     * 添加空间房间
     */
    public static <D extends RequestBase> void addRoom(D request, ApiCallBack<RoomBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addRoom(requestMap).enqueue(callBack);
    }

    public static <D extends RequestBase> void getImportGoods(D request, ApiCallBack<ImportGoodsBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getImportGoods(requestMap).enqueue(callBack);
    }

    /**
     * 删除空间房间
     */
    public static <D extends RequestBase> void delRoom(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().delRoom(requestMap).enqueue(callBack);
    }

    /**
     * 编辑空间房间名称
     */
    public static <D extends RequestBase> void editRoom(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().editRoom(requestMap).enqueue(callBack);
    }

    /**
     * 移动空间房间
     */
    public static <D extends RequestBase> void moveRoom(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().moveRoom(requestMap).enqueue(callBack);
    }

    /**
     * 获取家具模板类别列表
     */
    public static <D extends RequestBase> void getTemplateFurnitureList(D request, ApiCallBack<List<FurnitureTemplateBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getTemplateFurnitureList(requestMap).enqueue(callBack);
    }

    /**
     * 添加家具
     */

    public static <D extends RequestBase> void addFurniture(D request, ApiCallBack<FurnitureBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().addFurniture(requestMap).enqueue(callBack);
    }

    /**
     * 获取房间（根据系统模板）
     */
    public static <D extends RequestBase> void getRoomsTemplate(D request, ApiCallBack<List<RoomFurnitureBean>> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getRoomsTemplate(requestMap).enqueue(callBack);
    }

    /**
     * 创建家庭
     */
    public static <D extends RequestBase> void createFamily(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().createFamily(requestMap).enqueue(callBack);
    }


    /**
     * 获取隔层模板列表
     */
    public static <D extends RequestBase> void getTemplateLayerList(String uid, String system_furniture_code, ApiCallBack<LayerTemplateBean> callBack) {
        ApiInstance.getApi().getTemplateLayerList(uid, system_furniture_code).enqueue(callBack);
    }

    /**
     * 获取我自己的、以及别人共享给我的家庭列表
     */
    public static <D extends RequestBase> void getFamilyList(String uid, ApiCallBack<MyFamilyListBean> callBack) {
        ApiInstance.getApi().getFamilyList(uid).enqueue(callBack);
    }

    /**
     * 获取用户的某一个家庭（这个家庭可能是我的，也可能是被人共享给我的）的房间列表(按权重倒序排)（含家具个数、被共享人列表）
     */
    public static <D extends RequestBase> void getFamilyDetails(String uid, String familyCode, ApiCallBack<FamilyListDetailsBean> callBack) {
        ApiInstance.getApi().getFamilyDetails(uid, familyCode).enqueue(callBack);
    }

    /**
     * 删除家庭
     */
    public static <D extends RequestBase> void delFamily(String uid, String familyCode, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().delFamily(uid, familyCode).enqueue(callBack);
    }

    /**
     * 取消家庭共享 （家庭管理页面，关闭共享按钮）
     */
    public static <D extends RequestBase> void disShareFamily(String uid, String familyCode, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().disShareFamily(uid, familyCode).enqueue(callBack);
    }

    /**
     * 编辑家庭名称
     */
    public static <D extends RequestBase> void editFamily(String uid, String familyCode, String familyName, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().editFamily(uid, familyCode, familyName).enqueue(callBack);
    }

    /**
     * 家庭管理页面 -- 获取被共享人的信息和所共享的房间，以及共享人的状态
     */
    public static <D extends RequestBase> void getShareListUserByFamily(String uid, String familyCode, ApiCallBack<List<SharedPersonBean>> callBack) {
        ApiInstance.getApi().getShareUserByFamily(uid, familyCode).enqueue(callBack);
    }

    /**
     * 删除协作人
     */
    public static <D extends RequestBase> void delCollaborator(String uid, String be_shared_user, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().delCollaborator(uid, be_shared_user).enqueue(callBack);
    }

    /**
     * 家庭管理页面 -- -向某个协作人 新增或取消共享某个家庭下的某几个房间
     */
    public static <D extends RequestBase> void shareOrCancelShareRooms(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().shareOrCancelShareRooms(requestMap).enqueue(callBack);
    }

    /**
     * 导入物品
     */
    public static <D extends RequestBase> void importGoods(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().importGoods(requestMap).enqueue(callBack);
    }

    /**
     * 删除物品
     */
    public static <D extends RequestBase> void delSelectGoods(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().delSelectGoods(requestMap).enqueue(callBack);
    }

    /**
     * 封存/解封物品
     */
    public static <D extends RequestBase> void goodsArchive(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().goodsArchive(requestMap).enqueue(callBack);
    }

    /**
     * 一键移除所有封存物品
     */
    public static <D extends RequestBase> void removeArchiveObjects(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().removeArchiveObjects(requestMap).enqueue(callBack);
    }

    /**
     * 置顶物品
     */
    public static <D extends RequestBase> void topSelectGoods(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().topSelectGoods(requestMap).enqueue(callBack);
    }

    /**
     * 编辑收纳盒名称
     */
    public static <D extends RequestBase> void editBoxName(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().editBoxName(requestMap).enqueue(callBack);
    }

    /**
     * 删除收纳盒
     */
    public static <D extends RequestBase> void delBox(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().delBox(requestMap).enqueue(callBack);
    }

    /**
     * 迁移隔层
     */
    public static <D extends RequestBase> void moveLayer(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().moveLayer(requestMap).enqueue(callBack);
    }

    /**
     * 迁移收纳盒
     */
    public static <D extends RequestBase> void moveBox(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().moveBox(requestMap).enqueue(callBack);
    }


    /**
     * 批量编辑物品列表
     */
    public static <D extends RequestBase> void getEditMoreGoodsList(String uid, String family_code, String category_code, ApiCallBack<List<ObjectBean>> callBack) {
        ApiInstance.getApi().getEditMoreGoodsList(uid, family_code, category_code).enqueue(callBack);
    }

    /**
     * 批量添加分类
     */
    public static <D extends RequestBase> void objectsAddCategory(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().objectsAddCategory(requestMap).enqueue(callBack);
    }

    /**
     * 搜索列表
     */
    public static <D extends RequestBase> void getSearchList(D request, ApiCallBack<SerchListBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getSearchList(requestMap).enqueue(callBack);
    }

    /**
     * 消息列表
     */
    public static <D extends RequestBase> void getMessagesList(String uid, int page, String content_type, ApiCallBack<MessagePostBean> callBack) {
        ApiInstance.getApi().getMessagesList(uid, page, content_type).enqueue(callBack);
    }

    /**
     * 清空物品位置
     */
    public static <D extends RequestBase> void removeObjectFromFurnitrue(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().removeObjectFromFurnitrue(requestMap).enqueue(callBack);
    }

    public static <D extends RequestBase> void getGoodsRemindsById(String uid, String object_id, ApiCallBack<List<RemindBean>> callBack) {
        ApiInstance.getApi().getGoodsRemindsById(uid, object_id).enqueue(callBack);
    }


    /**
     * 删除物品
     */
    public static <D extends RequestBase> void delGoods(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().delGoods(requestMap).enqueue(callBack);
    }


    /**
     * 获取vip价格
     */
    public static <D extends RequestBase> void getVIPPrice(String uid, ApiCallBack<VIPBean> callBack) {
        ApiInstance.getApi().getVIPPrice(uid).enqueue(callBack);
    }

    /**
     * 购买vip
     */
    public static <D extends RequestBase> void buyVipWXOrAli(String uid, int price, String type, String couponId, ApiCallBack<BuyVIPResultBean> callBack) {
        ApiInstance.getApi().buyVipWXOrAli(uid, price, type, couponId).enqueue(callBack);
    }

    /**
     * 购买能量
     */
    public static <D extends RequestBase> void buyEnergy(D request, ApiCallBack<BuyVIPResultBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().buyEnergy(requestMap).enqueue(callBack);
    }

    /**
     * 获取用户信息
     */
    public static <D extends RequestBase> void getUserInfo(String uid, ApiCallBack<UserBean> callBack) {
        ApiInstance.getApi().getUserInfo(uid).enqueue(callBack);
    }

    public static <D extends RequestBase> void getEnergyCode(String uid, String code, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().getEnergyCode(uid, code).enqueue(callBack);
    }

    /**
     * 反馈意见
     */
    public static <D extends RequestBase> void feedBack(D request, ApiCallBack<FeedbackBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().feedBack(requestMap).enqueue(callBack);
    }

    /**
     * 修改用户信息
     */
    public static <D extends RequestBase> void editInfo(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().editInfo(requestMap).enqueue(callBack);
    }

    /**
     * 设置手机号
     */
    public static <D extends RequestBase> void changePhone(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().changePhone(requestMap).enqueue(callBack);
    }

    /**
     * 判断手机号是否绑定
     */
    public static <D extends RequestBase> void isRegistered(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().isRegistered(requestMap).enqueue(callBack);
    }


    /**
     * APP端分享成功记录
     */
    public static <D extends RequestBase> void sharedApp(String uid, String share_type, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().sharedApp(uid, share_type).enqueue(callBack);
    }

    /**
     * 支付成功后，app端的同步通知
     */
    public static <D extends RequestBase> void notificationServer(String uid, String pay_type, String order_no, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().notificationServer(uid, pay_type, order_no).enqueue(callBack);
    }

    /**
     * 判断用户是否绑定了第三方账号
     */
    public static <D extends RequestBase> void bindCheck(String uid, String pay_type, String order_no, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().bindCheck(uid, pay_type, order_no).enqueue(callBack);
    }

    /**
     * 绑定第三方账号
     */
    public static <D extends RequestBase> void bindAccount(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().bindAccount(requestMap).enqueue(callBack);
    }

    /**
     * 修改密码
     */
    public static <D extends RequestBase> void changePassword(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().changePassword(requestMap).enqueue(callBack);
    }

    /**
     * 获取最新版本
     */
    public static <D extends RequestBase> void getVersion(String app_version, ApiCallBack<VersionBean> callBack) {
        ApiInstance.getApi().getVersion("ANDROID", app_version).enqueue(callBack);
    }

    /**
     * 获取常忘物品详情的list
     */
    public static <D extends RequestBase> void getCangWangGoodsList(D request, ApiCallBack<ChangWangListBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().getCangWangGoodsList(requestMap).enqueue(callBack);
    }

    /**
     * 设置常忘物品有没有
     */
    public static <D extends RequestBase> void setCangWangDetail(D request, ApiCallBack<ChangWangDetailBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().setCangWangDetail(requestMap).enqueue(callBack);
    }

    public static <D extends RequestBase> void setGoodsWeight(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().setGoodsWeight(requestMap).enqueue(callBack);
    }

    public static <D extends RequestBase> void setGoodsNoWeight(D request, ApiCallBack<RequestSuccessBean> callBack) {
        Map<String, String> requestMap = requestPrepare(request);
        ApiInstance.getApi().setGoodsNoWeight(requestMap).enqueue(callBack);
    }

    public static <D extends RequestBase> void cancelWXEnergy(BuyEnergyReq barcodeBean, ApiCallBack<RequestSuccessBean> callBack) {
        ApiInstance.getApi().cancelWX(barcodeBean).enqueue(callBack);
    }

    public static <D extends RequestBase> void getGoodsByBarcode(BarcodeBean barcodeBean, ApiCallBack<BarcodeResultBean> callBack) {
        ApiInstance.getApi().getGoodsByBarcode(barcodeBean).enqueue(callBack);
    }

    public static <D extends RequestBase> void getGoodsByTBbarcode(BarcodeBean barcodeBean, ApiCallBack<BarcodeResultBean> callBack) {
        ApiInstance.getApi().getGoodsByTBbarcode(barcodeBean).enqueue(callBack);
    }

    /**
     * 归位统计
     */
    public static <D extends RequestBase> void getGoodsReturnDetails(String uid, String family_code, String code, ApiCallBack<List<StatisticsBean>> callBack) {
        ApiInstance.getApi().getGoodsReturnDetails(uid, family_code, code).enqueue(callBack);
    }

    /**
     * 分类统计
     */
    public static <D extends RequestBase> void getGoodsSortDetails(String uid, String family_code, ApiCallBack<List<StatisticsBean>> callBack) {
        ApiInstance.getApi().getGoodsSortDetails(uid, family_code).enqueue(callBack);
    }

    /**
     * 颜色统计
     */
    public static <D extends RequestBase> void getGoodsColorsDetails(String uid, String family_code, ApiCallBack<List<StatisticsBean>> callBack) {
        ApiInstance.getApi().getGoodsColorsDetails(uid, family_code).enqueue(callBack);
    }

    /**
     * 季节统计
     */
    public static <D extends RequestBase> void getGoodsSeasonDetails(String uid, String family_code, ApiCallBack<List<StatisticsBean>> callBack) {
        ApiInstance.getApi().getGoodsSeasonDetails(uid, family_code).enqueue(callBack);
    }

    /**
     * 价格统计
     */
    public static <D extends RequestBase> void getGoodsPriceDetails(String uid, String family_code, String code, ApiCallBack<List<StatisticsBean>> callBack) {
        ApiInstance.getApi().getGoodsPriceDetails(uid, family_code, code).enqueue(callBack);
    }

    /**
     * 购买时间统计(季度)
     */
    public static <D extends RequestBase> void getGoodsTimeDetails(String uid, String family_code, String code, ApiCallBack<List<StatisticsBean>> callBack) {
        ApiInstance.getApi().getGoodsTimeDetails(uid, family_code, code).enqueue(callBack);
    }

    /**
     * 清单
     */
    public static <D extends RequestBase> void getDetailedList(String uid, String family_code, String room_code, String furniture_code, ApiCallBack<DetailedListBean> callBack) {
        ApiInstance.getApi().getDetailedList(uid, family_code, room_code, furniture_code).enqueue(callBack);
    }

    public static <D extends RequestBase> void getArticList(ApiCallBack<List<ArticleBean>> callBack) {
        ApiInstance.getApi().getArticList().enqueue(callBack);
    }

    public static <D extends RequestBase> void getEnergyPrice(String uid, ApiCallBack<EnergyPriceBean> callBack) {
        ApiInstance.getApi().getEnergyPrice(uid).enqueue(callBack);
    }

}
