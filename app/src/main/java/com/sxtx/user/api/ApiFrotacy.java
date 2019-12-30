package com.sxtx.user.api;

import com.like.base.net.BaseHttpUtil;

/**
 * Created by Administrator on 2017/12/31.
 */

public class ApiFrotacy {

    private static final Object monitor = new Object();//防止同时在初始化
    private static IAccountApi accountApi = null;
    private static ICommonApi commonApi = null;

    public static IAccountApi getAccountApiSingleton() {
        synchronized (monitor) {
            if (accountApi == null) {
                accountApi = BaseHttpUtil.getIntance().createApi(IAccountApi.class);
            }
            return accountApi;
        }
    }

    public static ICommonApi getCommonApiSingleton() {
        synchronized (monitor) {
            if (commonApi == null) {
                commonApi = BaseHttpUtil.getIntance().createOhterApi(ICommonApi.class);
            }
            return commonApi;
        }
    }


}
