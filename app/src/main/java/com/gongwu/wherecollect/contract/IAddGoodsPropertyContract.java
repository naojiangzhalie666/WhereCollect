package com.gongwu.wherecollect.contract;


import com.gongwu.wherecollect.base.BaseView;
import com.gongwu.wherecollect.interfacedef.RequestCallback;
import com.gongwu.wherecollect.net.entity.request.CustomSubCateReq;
import com.gongwu.wherecollect.net.entity.response.BaseBean;
import com.gongwu.wherecollect.net.entity.response.ChannelBean;

import java.util.List;

public interface IAddGoodsPropertyContract {
    interface IAddGoodsPropertyModel {

        void getColors(String uid, final RequestCallback callback);

        void getChannel(String uid, final RequestCallback callback);

        void getFirstCategoryList(String uid, final RequestCallback callback);

        void getCategoryDetails(String uid, String code, final RequestCallback callback);

        void getChannelList(String uid, String keyword, final RequestCallback callback);

        void getSearchSort(String uid, String keyword, final RequestCallback callback);

        void saveCustomCate(CustomSubCateReq req, final RequestCallback callback);

        void saveCustomSubCate(CustomSubCateReq req, final RequestCallback callback);

        void addChannel(String uid, String name, String code, final RequestCallback callback);
    }

    interface IAddGoodsPropertyPresenter {
        void getColors(String uid);

        void getChannel(String uid);

        void getFirstCategoryList(String uid);

        void getCategoryDetails(String uid, String code);

        void getChannelList(String uid, String keyword);

        void getSearchSort(String uid, String keyword);

        void saveCustomCate(String uid, String name);

        void saveCustomSubCate(String uid, String name, String parent_code);

        void addChannel(String uid, String name, String code);
    }

    interface IAddGoodsPropertyView extends BaseView {

        void getColorsSuccess(List<String> data);

        void getChannelSuccess(List<ChannelBean> data);

        void getFirstCategoryListSuccess(List<BaseBean> data);

        void getCategoryDetailsSuccess(List<ChannelBean> data);

        void getChannelListSuccess(List<ChannelBean> data);

        void getSearchSortSuccess(List<ChannelBean> data);

        void saveCustomSubCateSuccess(BaseBean bean);

        void addChannelSuccess(ChannelBean data);

    }
}
