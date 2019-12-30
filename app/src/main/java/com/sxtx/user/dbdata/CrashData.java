package com.sxtx.user.dbdata;

public class CrashData {
    /**设备ID**/
    private String deviceId;
    /**崩溃时间**/
    private String crashTime;
    /**设备类型**/
    private String deviceType;
    /**设备系统版本号**/
    private String osVer;
    /**设备手机类型**/
    private String phoneType;
    /**应用ID   channelId**/
    private String appId;
    /**应用版本**/
    private String appVer;
    /**应用名称**/
    private String appName;
    /**应用包名**/
    private String appPack;
    /**崩溃类型**/
    private String crashType;
    /**备注**/
    private String notes;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getCrashTime() {
        return crashTime;
    }

    public void setCrashTime(String crashTime) {
        this.crashTime = crashTime;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getOsVer() {
        return osVer;
    }

    public void setOsVer(String osVer) {
        this.osVer = osVer;
    }

    public String getPhoneType() {
        return phoneType;
    }

    public void setPhoneType(String phoneType) {
        this.phoneType = phoneType;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVer() {
        return appVer;
    }

    public void setAppVer(String appVer) {
        this.appVer = appVer;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppPack() {
        return appPack;
    }

    public void setAppPack(String appPack) {
        this.appPack = appPack;
    }

    public String getCrashType() {
        return crashType;
    }

    public void setCrashType(String crashType) {
        this.crashType = crashType;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
