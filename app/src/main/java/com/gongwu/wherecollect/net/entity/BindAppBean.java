package com.gongwu.wherecollect.net.entity;

import org.litepal.crud.DataSupport;

/**
 * Function:
 * Date: 2017/9/12
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class BindAppBean extends DataSupport {
    private String nickname;
    private String openid;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }
}
