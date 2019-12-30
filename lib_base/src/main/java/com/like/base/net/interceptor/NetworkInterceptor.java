package com.like.base.net.interceptor;

import android.text.TextUtils;

import com.like.utilslib.other.LogUtil;

import java.io.IOException;

import okhttp3.Request;
import okhttp3.Response;

/**
 * 无网络处理
 */
public class NetworkInterceptor extends BaseInterceptor {

    private int maxAge=10;//默认缓存时间 单位秒 联网的状态下，间隔多少次请求同一个接口，返回来的数据是一样的。

    public NetworkInterceptor(){

    }
    public NetworkInterceptor(int cacheTime){
        this.maxAge=cacheTime;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request=chain.request();
        Response response =chain.proceed(request);
        String serverCache=response.header("Cache-Control");
        //如果服务器处理了换成策略，那么本地就不需要处理了。
        if (TextUtils.isEmpty(serverCache)){
            String cacheControl=request.cacheControl().toString();
            if (TextUtils.isEmpty(cacheControl)){//如果本地time没有设置
                LogUtil.loge("NetworkInterceptor: 请求没有设置 Cache-Control");
                return response.newBuilder()
                        .removeHeader("Pragma")
                        .removeHeader("Cache-Control")
                        .header("Cache-Control","public, max-age="+maxAge)
                        .build();
            }else {
                LogUtil.loge("NetworkInterceptor: 请求设置了---"+cacheControl);
                return response.newBuilder()
                        .header("Cache-Control",cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }
        }
        LogUtil.loge("NetworkInterceptor: 服务器有缓存"+serverCache);
        return response;
    }
}
