package com.sxtx.user.model.cenum;

/**
 * Created by longshao on 2017/8/1.
 */

public enum APPTag {
    IS_FRIST_ENTER,//用户第一次打开APP
    LOGIN_STATE,//用户登录状态
    ACCOUNT_DATA,//用户数据
    ACCOUNT_PASSWORD,//用户密码
    ACCOUNT_NAME,//用户账号


    TOKEN_1_NAME,//  TOKEN1  第一批接口的token
    HEART_STATUS,//  心跳是否發送成功狀態
    FILTER_STATUS,//  是否开启筛选
    VIDEO_MUTE,//  視頻播放是否靜音

    GESTURE_PASSWORD,
    GESTURE_OPEN,
    OSS_CRASH_URL,//正式服服務器url
    OSS_URL,//正式服服務器url
    PHONE,
    PASSWORD_TWO,
    PASSWORD_ONE,
    DEVICEID,
    IS_BIND_PHONE_SUCCESS,//绑定手机成功
    IS_SAVE_CERTIFICATE//已保存憑證
}
