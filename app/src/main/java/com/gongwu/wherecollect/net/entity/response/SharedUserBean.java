package com.gongwu.wherecollect.net.entity.response;

public class SharedUserBean {
    //        "_id": "5cfbb4979b9a0a4e7c9520cf",
//                "nickname": "10010@qq.com",
//                "avatar": "http://cdn.shouner.com/default/shounaer_icon.png"
    String _id;
    String nickname;
    String avatar;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
}
