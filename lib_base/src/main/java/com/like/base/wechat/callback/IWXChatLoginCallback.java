package com.like.base.wechat.callback;

/**
 * Created by longshao on 2017/8/15.
 */

public interface IWXChatLoginCallback {
    void onLoginSuccess(String userInfo);

    void onLoginCancle();

    void onLoginError(String error);
}
