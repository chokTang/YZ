package com.like.base.net;

import com.like.base.BuildConfig;
import com.like.base.app.LongshaoAPP;
import com.like.base.app.config.ConfigType;
import com.like.base.net.conver.GsonNullConverterFactory;
import com.like.base.net.interceptor.LogInterceptor;
import com.like.utilslib.app.CommonUtil;

import java.net.Proxy;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by longshao on 2017/3/12.
 */

public class BaseHttpUtil {

    private static final int DEFAULT_TIMEOUT = 10;//默认超时时间
    private static final int CACHE_SIZE = 10 * 1024 * 1024;//10M
    private Retrofit retrofit, otherRetrofit;
    private OkHttpClient.Builder mHttpBuilder;

    private static class BaseHolder {
        private static final BaseHttpUtil INTANCE = new BaseHttpUtil();
    }

    /**
     * 默认超时时间
     */
    private BaseHttpUtil() {
        mHttpBuilder = new OkHttpClient.Builder();
        mHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);//设置超时时间
        mHttpBuilder.readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mHttpBuilder.writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mHttpBuilder.retryOnConnectionFailure(false);//连接超时了是否重新连接
        mHttpBuilder.proxy(Proxy.NO_PROXY);//设置无代理
        final ArrayList<Interceptor> interceptors = LongshaoAPP.getConfiguration(ConfigType.INTERCEPTOR);
        if (interceptors != null && !interceptors.isEmpty()) {
            for (Interceptor interceptor : interceptors) {
                mHttpBuilder.addInterceptor(interceptor);
            }
        }

        /*设置缓存大小*/
//        File httpCacheDirectory = new File(UtilApp.getIntance().getApplicationContext().getCacheDir(), "cacheresponse");
//        Cache cache = new Cache(httpCacheDirectory, CACHE_SIZE);
//        mHttpBuilder.cache(cache);

        /*打印日志*/
        if (BuildConfig.LOG_DEBUG) {
            mHttpBuilder.addInterceptor(new LogInterceptor());
        }

        /*处理缓存策略*/
//        mHttpBuilder.addInterceptor(new CacheInterceptor());
//        mHttpBuilder.addNetworkInterceptor(new NetworkInterceptor());

        retrofit = new Retrofit.Builder()
                .client(mHttpBuilder.build())
//                .addConverterFactory(BodyConverterFactory.create())//设置返回来为String类型的数据
                .addConverterFactory(GsonNullConverterFactory.create())//设置返回来为Gson类型的数据
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(LongshaoAPP.getConfiguration(ConfigType.API_HOST).toString())
                .build();
    }

    public static final BaseHttpUtil getIntance() {
        return BaseHolder.INTANCE;
    }

    public <T> T createApi(Class<T> t) {
        if (retrofit == null) {
            String url;
            if (CommonUtil.isNull(LongshaoAPP.getConfiguration(ConfigType.API_HOST))){
                url=LongshaoAPP.getConfiguration(ConfigType.API_HOST).toString();
            }else {
                url=LongshaoAPP.getConfiguration(ConfigType.API_HOST).toString();
            }
            retrofit = new Retrofit.Builder()
                    .client(mHttpBuilder.build())
//                    .addConverterFactory(BodyConverterFactory.create())//设置返回来为String类型的数据
                    .addConverterFactory(GsonNullConverterFactory.create())//设置返回来为Gson类型的数据
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url)
                    .build();
        }
        return retrofit.create(t);
    }

    public <T> T createOhterApi(Class<T> t) {
        if (otherRetrofit == null) {
            String url;
            if (CommonUtil.isNull(LongshaoAPP.getConfiguration(ConfigType.OTHER_HOST))){
                url=LongshaoAPP.getConfiguration(ConfigType.API_HOST).toString();
            }else {
                url=LongshaoAPP.getConfiguration(ConfigType.OTHER_HOST).toString();
            }
            otherRetrofit = new Retrofit.Builder()
                    .client(mHttpBuilder.build())
//                    .addConverterFactory(BodyConverterFactory.create())//设置返回来为String类型的数据
                    .addConverterFactory(GsonNullConverterFactory.create())//设置返回来为Gson类型的数据
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .baseUrl(url)
                    .build();
        }
        return otherRetrofit.create(t);
    }


    /**
     * 清空其他的
     */
    public void clearOtherRetrofit(){
        otherRetrofit =null;
    }

    /**
     * 清空主API
     */
    public void clearRetrofit(){
        retrofit =null;
    }
}
