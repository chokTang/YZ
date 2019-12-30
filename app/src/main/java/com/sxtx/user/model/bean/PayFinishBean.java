package com.sxtx.user.model.bean;

/**
 * 介紹:
 * 作者:CHOK
 */
public class PayFinishBean {

    /**
     * message : 处理成功
     * payStatus : 0
     * status : 1
     */

    private String message;
    private int payStatus;
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(int payStatus) {
        this.payStatus = payStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
