package com.sxtx.user.mvp;

import com.like.base.base.presenter.BaseStatusPresenter;
import com.sxtx.user.api.ApiFrotacy;
import com.sxtx.user.api.IAccountApi;
import com.sxtx.user.api.ICommonApi;
import com.sxtx.user.util.AppPreference;

import java.util.Map;

/**
 * Created by Administrator on 2018/1/1.
 */

public class APPresenter<V> extends BaseStatusPresenter<V> {
    public static IAccountApi accountApi = ApiFrotacy.getAccountApiSingleton();
    public static ICommonApi commonApi = ApiFrotacy.getCommonApiSingleton();

    protected Map<String, Object> paramsDeal(Map<String, Object> params) {
        return params;
    }

    /**
     * 获取用户token
     *
     * @return
     */
    protected String getToken() {
        return AppPreference.getIntance().getToken1();
    }

    /**
     * 获取用户ID
     *
     * @return
     */
    protected String getUserId() {
        return AppPreference.getIntance().getAccountData().getUser_id();
    }
}
