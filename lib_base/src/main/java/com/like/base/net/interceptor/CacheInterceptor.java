package com.like.base.net.interceptor;

import com.like.utilslib.other.LogUtil;
import com.like.utilslib.other.NetUtil;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.CacheControl;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 缓存数据处理
 */
public class CacheInterceptor extends BaseInterceptor {

    private int mCacheTime=60;//缓存60秒 单位秒

    public CacheInterceptor(){

    }

    public CacheInterceptor(int cacheTime){
        this.mCacheTime=cacheTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        if (!NetUtil.isConnected()){//如果网络不可用
            CacheControl cacheControl=new CacheControl.Builder()
                    .onlyIfCached()
                    .maxStale(mCacheTime, TimeUnit.SECONDS)
                    .build();
            request =request.newBuilder()
                    .cacheControl(cacheControl)
                    .build();
            LogUtil.loge("CacheInterceptor: no network");
        }
        return chain.proceed(request);
    }
}
