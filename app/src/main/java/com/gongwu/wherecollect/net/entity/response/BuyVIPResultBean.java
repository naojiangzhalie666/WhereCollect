package com.gongwu.wherecollect.net.entity.response;

import com.gongwu.wherecollect.net.entity.ALiPayBean;
import com.gongwu.wherecollect.net.entity.WxPayBean;

public class BuyVIPResultBean {

    private WxPayBean weichat;
    private ALiPayBean alipay;

    public WxPayBean getWeichat() {
        return weichat;
    }

    public void setWeichat(WxPayBean weichat) {
        this.weichat = weichat;
    }

    public ALiPayBean getAlipay() {
        return alipay;
    }

    public void setAlipay(ALiPayBean alipay) {
        this.alipay = alipay;
    }
}
