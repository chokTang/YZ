package com.like.base.net.interceptor;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 由于http网络连接的时候导致超时或者还没有关闭 一直超时的原因
 * 备注：因后台的服务器代码不一样导致的，视情况添加
 */
public class ConnectionInterceptor extends BaseInterceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request().newBuilder()
                .addHeader("Connection", "close").build();
        return chain.proceed(request);
    }
}
