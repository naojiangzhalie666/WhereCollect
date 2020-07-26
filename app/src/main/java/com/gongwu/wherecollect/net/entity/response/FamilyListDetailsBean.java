package com.gongwu.wherecollect.net.entity.response;

import java.util.List;

public class FamilyListDetailsBean {
    private SharedUserBean sharedUser;
    private List<SharedUserBean> beSharedUsers;
    private List<RoomBean> rooms;
    private boolean isFamilyShared;

    public SharedUserBean getSharedUser() {
        return sharedUser;
    }

    public void setSharedUser(SharedUserBean sharedUser) {
        this.sharedUser = sharedUser;
    }

    public List<SharedUserBean> getBeSharedUsers() {
        return beSharedUsers;
    }

    public void setBeSharedUsers(List<SharedUserBean> beSharedUsers) {
        this.beSharedUsers = beSharedUsers;
    }

    public List<RoomBean> getRooms() {
        return rooms;
    }

    public void setRooms(List<RoomBean> rooms) {
        this.rooms = rooms;
    }

    public boolean isFamilyShared() {
        return isFamilyShared;
    }

    public void setFamilyShared(boolean familyShared) {
        isFamilyShared = familyShared;
    }
}
