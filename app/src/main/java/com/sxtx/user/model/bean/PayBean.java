package com.sxtx.user.model.bean;

/**
 * 介紹:
 * 作者:CHOK
 */
public class PayBean {


    /**
     * code : 1100
     * cmd : recharge_order
     * message : 【白名单规则】暂不支持【99.00】该自定义金额,当前支持金额为【500.00,1000.00,2000.00,3000.00,5000.00】
     * type : 0
     */

    private String code;
    private String cmd;
    private String orderNo;
    private String message="";
    private String redirect;
    private int type;


    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRedirectUrl() {
        return redirect;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirect = redirectUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
