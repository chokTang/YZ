package com.like.base.wechat.api;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Url;

/**
 * Created by longshao on 2018/1/15.
 */

public interface WXLoginApi {

    //微信登录
    @GET
    Observable<String> onWXLogin(@Url String url);

    //获取微信用户信息
    @GET
    Observable<String> onGetWXUserInfo(@Url String url);
}
