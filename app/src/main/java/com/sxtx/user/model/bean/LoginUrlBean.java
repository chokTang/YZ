package com.sxtx.user.model.bean;

/**
 * 介紹:
 * 作者:CHOK
 */
public class LoginUrlBean {

    /**
     * loginIp : http://192.168.0.2
     * loginPort : 8089
     */

    private String loginIp;
    private String loginPort;
    private String crashUrl;

    @Override
    public String toString() {
        return "LoginUrlBean{" +
                "loginIp='" + loginIp + '\'' +
                ", loginPort='" + loginPort + '\'' +
                ", crashUrl='" + crashUrl + '\'' +
                '}';
    }

    public String getCrashUrl() {
        return crashUrl;
    }

    public void setCrashUrl(String crashUrl) {
        this.crashUrl = crashUrl;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public String getLoginPort() {
        return loginPort;
    }

    public void setLoginPort(String loginPort) {
        this.loginPort = loginPort;
    }
}
