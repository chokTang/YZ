package com.sxtx.user.model.account;

import java.io.Serializable;

/**
 * 用户登录信息
 * Created by Administrator on 2017/12/31.
 */

public class UserModel implements Serializable{
    private String user_id;
    private String user_nickname;
    private String user_phone;
    private int user_sex; //性别 0女 1男 2无
    private String user_icon;
    private String user_openid;
    private long user_register_time;
    private long user_login_time;
    private String token;
    private long expire_time;
    private int user_status;//用户状态：0为冻结，1为正常

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_nickname() {
        return user_nickname;
    }

    public void setUser_nickname(String user_nickname) {
        this.user_nickname = user_nickname;
    }

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    public int getUser_sex() {
        return user_sex;
    }

    public void setUser_sex(int user_sex) {
        this.user_sex = user_sex;
    }

    public String getUser_icon() {
        return user_icon;
    }

    public void setUser_icon(String user_icon) {
        this.user_icon = user_icon;
    }

    public String getUser_openid() {
        return user_openid;
    }

    public void setUser_openid(String user_openid) {
        this.user_openid = user_openid;
    }

    public long getUser_register_time() {
        return user_register_time;
    }

    public void setUser_register_time(long user_register_time) {
        this.user_register_time = user_register_time;
    }

    public long getUser_login_time() {
        return user_login_time;
    }

    public void setUser_login_time(long user_login_time) {
        this.user_login_time = user_login_time;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getExpire_time() {
        return expire_time;
    }

    public void setExpire_time(long expire_time) {
        this.expire_time = expire_time;
    }

    public int getUser_status() {
        return user_status;
    }

    public void setUser_status(int user_status) {
        this.user_status = user_status;
    }
}
