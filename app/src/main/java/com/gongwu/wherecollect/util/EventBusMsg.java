package com.gongwu.wherecollect.util;


import com.gongwu.wherecollect.net.entity.response.MessageBean;

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

    public static class RefreshActivity {
    }

    public static class updateShareMsg {
    }

    public static class startService {
    }

    public static class stopService {
    }

    public static class MainTabMessage {
        public int position;

        public MainTabMessage(int position) {
            this.position = position;
        }
    }

    public static class GetMessageList {
        public MessageBean messageBean;

        public GetMessageList(MessageBean messageBean) {
            this.messageBean = messageBean;
        }

    }
}