package com.gongwu.wherecollect.util;


import com.gongwu.wherecollect.net.entity.response.MessageBean;
import com.gongwu.wherecollect.net.entity.response.ObjectBean;

import java.util.Map;

/**
 * Function:
 * Date: 2017/12/15
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class EventBusMsg {

    public static class RefreshRoomsFragment {
    }

    public static class RefreshFragment {
    }

    public static class RefreshRemind {
    }

    public static class RefreshActivity {
    }

    public static class RefreshEditItem {
    }

    public static class FinishActivity {
    }

    public static class UpdateShareMsg {
    }

    public static class StartService {
    }

    public static class BuyVipSuccess {

    }

    public static class StopService {
    }

    public static class SetGoodsLocationByCangWang {
        public Map<String, ObjectBean> addGoodList;

        public SetGoodsLocationByCangWang(Map<String, ObjectBean> addGoodList) {
            this.addGoodList = addGoodList;
        }
    }

    public static class SelectHomeFragmentTab {
    }

    public static class SelectHomeTab {
        public boolean isShowEndTimeHint;
    }

    public static class LookGoodsAct {
        public boolean isShowEndTimeHint;
    }

    public static class RefreshFurnitureLook {
    }

    public static class MainTabMessage {
        public int position;

        public MainTabMessage(int position) {
            this.position = position;
        }
    }

    public static class WXPayMessage {
        public int code;

        public WXPayMessage(int code) {
            this.code = code;
        }
    }

    public static class GetMessageList {
        public MessageBean messageBean;

        public GetMessageList(MessageBean messageBean) {
            this.messageBean = messageBean;
        }

    }
}
