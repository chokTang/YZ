package com.sxtx.user.mvp.view.account;

import com.sxtx.user.model.account.UserModel;

/**
 * Created by Administrator on 2017/12/30.
 */

public interface IRegisterView {

    void onShowSendCodeResult(boolean iSuccess);

    void onRegisterResult(UserModel model);

    void onGetTestData(String content);
}
