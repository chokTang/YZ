package com.sxtx.user.fragment.update;

/**
 * 版本更新
 * Created by Yan on 2018/3/28.
 */

public class UpdateInfoModel {
    private int versionCode;
    private String versionName;
    private String downloadUrl;
    private int forceUpdateLimit;
    private String versionDesc;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getForceUpdateLimit() {
        return forceUpdateLimit;
    }

    public void setForceUpdateLimit(int forceUpdateLimit) {
        this.forceUpdateLimit = forceUpdateLimit;
    }

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }
}