package com.gongwu.wherecollect.net.entity.response;

public class VersionBean {
//        "system_name": "ANDROID", // 系统名称
//        "version": "4.2.0", // 客户端传给后端的 版本号
//        "newlyVersion": "", // 目前管理后台中配置的当前系统的最新版本号
//        "force": true // 是否强制更新
    private String system_name;
    private String version;
    private String newlyVersion;
    private boolean force;
    private String download_url;

    public String getSystem_name() {
        return system_name;
    }

    public void setSystem_name(String system_name) {
        this.system_name = system_name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getNewlyVersion() {
        return newlyVersion;
    }

    public void setNewlyVersion(String newlyVersion) {
        this.newlyVersion = newlyVersion;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public String getDownload_url() {
        return download_url;
    }

    public void setDownload_url(String download_url) {
        this.download_url = download_url;
    }
}
