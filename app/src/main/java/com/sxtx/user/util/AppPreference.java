package com.sxtx.user.util;

import android.util.Log;

import com.like.utilslib.preference.PreferenceUtil;
import com.sxtx.user.model.account.UserModel;
import com.sxtx.user.model.cenum.APPTag;

/**
 * Created by longshao on 2017/8/1.
 */

public class AppPreference {
    private static AppPreference preference;

    public final static AppPreference getIntance() {
        synchronized (AppPreference.class) {
            if (preference == null) {
                preference = new AppPreference();
            }
        }
        return preference;
    }

    /**
     * 设置是否第一次登陆
     *
     * @param islogin
     */
    public final void setFirstLogin(boolean islogin) {
        PreferenceUtil.getIntance().setBoolean(APPTag.IS_FRIST_ENTER.name(), islogin);
    }

    /**
     * 获取是否第一次登陆
     *
     * @return
     */
    public final boolean getIsFirstLogin() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.IS_FRIST_ENTER.name(), true);
    }

    /**
     * 设置用户登录状态
     *
     * @param state
     */
    public final void setAccountLonginState(boolean state) {
        PreferenceUtil.getIntance().setBoolean(APPTag.LOGIN_STATE.name(), state);
    }

    /**
     * 获取用户登录状态
     *
     * @return
     */
    public final boolean getAccountLonginState() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.LOGIN_STATE.name(), false);
    }


    /**
     * 保存登录用户数据
     *
     * @param model
     */
    public final void setAccountData(UserModel model) {
        PreferenceUtil.getIntance().saveObject(APPTag.ACCOUNT_DATA.name(), model);
    }

    /**
     * 获取登录用户数据
     *
     * @return
     */
    public final UserModel getAccountData() {
        return (UserModel) PreferenceUtil.getIntance().readObject(APPTag.ACCOUNT_DATA.name());
    }

    /**
     * 保存登录用户密码
     *
     * @param password
     */
    public final void setAccountPassword(String password) {
        PreferenceUtil.getIntance().setString(APPTag.ACCOUNT_PASSWORD.name(), password);
    }

    /**
     * 获取登录用户密码
     *
     * @return
     */
    public final String getAccountPassword() {
        return PreferenceUtil.getIntance().getString(APPTag.ACCOUNT_PASSWORD.name());
    }

    /**
     * 保存登录用户账号
     *
     * @param name
     */
    public final void setAccountName(String name) {
        PreferenceUtil.getIntance().setString(APPTag.ACCOUNT_NAME.name(), name);
    }

    /**
     * 設置綁定憑證
     *
     * @param isBind
     */
    public final void setSaveCertificate(boolean isBind) {
        PreferenceUtil.getIntance().setBoolean(APPTag.IS_SAVE_CERTIFICATE.name(), isBind);
    }

    /**
     * 獲取是否綁定憑證
     *
     */
    public final boolean getSaveCertificate() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.IS_SAVE_CERTIFICATE.name(), false);
    }

    /**
     * 获取登录用户账号
     *
     * @return
     */
    public final String getAccountName() {
        return PreferenceUtil.getIntance().getString(APPTag.ACCOUNT_NAME.name());
    }


    /**
     * 存登陆时候的token
     *
     * @return
     */
    public final void setToken1(String name) {
        Log.e("setToken1", name);
        PreferenceUtil.getIntance().setString(APPTag.TOKEN_1_NAME.name(), name);
    }

    /**
     * 获取登登陆时候的token
     *
     * @return
     */
    public final String getToken1() {
        Log.e("getToken1", PreferenceUtil.getIntance().getString(APPTag.TOKEN_1_NAME.name()));
        return PreferenceUtil.getIntance().getString(APPTag.TOKEN_1_NAME.name());
    }

    /**
     * 獲取正式crash服服務器地址
     *
     * @return
     */
    public final String getOSS1Url() {
        return PreferenceUtil.getIntance().getString(APPTag.OSS_CRASH_URL.name());
    }

    /**
     * 設置正式服crash服務器地址
     *
     * @param url
     */
    public final void setOSS1Url(String url) {
        PreferenceUtil.getIntance().setString(APPTag.OSS_CRASH_URL.name(), url);
    }

    /**
     * 獲取正式服服務器地址
     *
     * @return
     */
    public final String getOSSUrl() {
        return PreferenceUtil.getIntance().getString(APPTag.OSS_URL.name(),"");
    }

    /**
     * 設置正式服服務器地址
     *
     * @param url
     */
    public final void setOSSUrl(String url) {
        PreferenceUtil.getIntance().setString(APPTag.OSS_URL.name(), url);
    }


    /**
     * 保存發送心跳是否成功
     *
     * @param isSuccess
     */
    public final void setHeart(Boolean isSuccess) {
        PreferenceUtil.getIntance().setBoolean(APPTag.HEART_STATUS.name(), isSuccess);
    }


    /**
     * 设置是否开启筛选 页面
     *
     * @param isSuccess
     */
    public final void setFilter(Boolean isSuccess) {
        PreferenceUtil.getIntance().setBoolean(APPTag.FILTER_STATUS.name(), isSuccess);
    }

    /**
     * 设置是否开启筛选 页面
     */
    public final Boolean getFilter() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.FILTER_STATUS.name());
    }


    /**
     * 获取服务器地址
     *
     * @return
     */
    public final Boolean getHeart() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.HEART_STATUS.name());
    }

    /**
     * 視頻播放是否靜音
     *
     * @param ismute
     */
    public final void setMute(Boolean ismute) {
        PreferenceUtil.getIntance().setBoolean(APPTag.VIDEO_MUTE.name(), ismute);
    }


    /**
     * 視頻播放是否靜音
     *
     * @return
     */
    public final Boolean getMute() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.VIDEO_MUTE.name(), true);
    }


    /**
     * 获取手势密码
     *
     * @return
     */
    public final String getGeSture() {
        return PreferenceUtil.getIntance().getString(APPTag.GESTURE_PASSWORD.name());
    }

    /**
     * 设置手势密码
     *
     * @return
     */
    public final void setGeSture(String geSturePassword) {
        PreferenceUtil.getIntance().setString(APPTag.GESTURE_PASSWORD.name(), geSturePassword);
    }


    /**
     * 获取手势开启状态
     *
     * @return
     */
    public final boolean getIsGeSture() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.GESTURE_OPEN.name(), false);
    }

    /**
     * 设置手势开启状态
     *
     * @return
     */
    public final void setIsGeSture(boolean isOpen) {
        PreferenceUtil.getIntance().setBoolean(APPTag.GESTURE_OPEN.name(), isOpen);
    }


    /**
     * PHONE
     *
     * @return
     */
    public final String getPhone() {
        return PreferenceUtil.getIntance().getString(APPTag.PHONE.name());
    }

    /**
     * PHONE
     *
     * @return
     */
    public final void setPhone(String phone) {
        PreferenceUtil.getIntance().setString(APPTag.PHONE.name(), phone);
    }

    /**
     * PASSWORD  TWO
     *
     * @return
     */
    public final String getPasswordTwo() {
        return PreferenceUtil.getIntance().getString(APPTag.PASSWORD_TWO.name());
    }

    /**
     * PASSWORD  TWO
     *
     * @return
     */
    public final void setPasswordTwo(String pas) {
        PreferenceUtil.getIntance().setString(APPTag.PASSWORD_TWO.name(), pas);
    }


    /**
     * PASSWORD  ONE
     *
     * @return
     */
    public final String getPasswordOne() {
        return PreferenceUtil.getIntance().getString(APPTag.PASSWORD_ONE.name());
    }

    /**
     * PASSWORD  ONE
     *
     * @return
     */
    public final void setPasswordOne(String pas) {
        PreferenceUtil.getIntance().setString(APPTag.PASSWORD_ONE.name(), pas);
    }


    /**
     * DEVICE
     *
     * @return
     */
    public final String getDeviceId() {
        return PreferenceUtil.getIntance().getString(APPTag.DEVICEID.name());
    }

    /**
     * DEVICE
     *
     * @return
     */
    public final void setDeviceId(String deviceId) {
        PreferenceUtil.getIntance().setString(APPTag.DEVICEID.name(), deviceId);
    }

    /**
     * 绑定手机
     *
     * @return
     */
    public final boolean getIsBindPhone() {
        return PreferenceUtil.getIntance().getBoolean(APPTag.IS_BIND_PHONE_SUCCESS.name(), false);
    }

    /**
     * 绑定手机
     *
     * @return
     */
    public final void setIsBindPhone(boolean isBindPhone) {
        PreferenceUtil.getIntance().setBoolean(APPTag.IS_BIND_PHONE_SUCCESS.name(), isBindPhone);
    }

}
