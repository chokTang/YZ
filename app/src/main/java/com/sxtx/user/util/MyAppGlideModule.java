package com.sxtx.user.util;


import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.model.FileLoader;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ViewTarget;
import com.sxtx.user.ConfigData;
import com.sxtx.user.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


/**
 * @author BY
 * @date 2019-05-31
 */
@GlideModule(glideName = "ImgurGlide")
public class MyAppGlideModule extends AppGlideModule {

    private final long TIME_OUT = 5000;


    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        builder.setLogLevel(Log.VERBOSE);
        builder.setLogRequestOrigins(false);

        // 设置别的get/set tag id，以免占用View默认的
        ViewTarget.setTagId(R.id.content_id);
        RequestOptions options = new RequestOptions().format(DecodeFormat.PREFER_RGB_565);
        builder.setDefaultRequestOptions(options);
    }

    /**
     * 清单解析的开启
     * <p>
     * 这里不开启，避免添加相同的modules两次
     *
     * @return
     */
    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }


    @Override
    public void registerComponents(@NonNull Context context,
                                   @NonNull Glide glide, @NonNull Registry registry) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .writeTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .readTimeout(TIME_OUT, TimeUnit.MILLISECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .build();
                        return chain.proceed(request);
                    }
                }).build();
        //注册网络加载框架 设置只使用此框架
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(client));
        //执行加密解密之前 必须先在Application里面执行 .so初始化： SoLoader.init(this, false);
        registry.append(File.class, InputStream.class,
                new FileLoader.Factory<>(new FileLoader.FileOpener<InputStream>() {

                    @Override
                    public InputStream open(File file) throws FileNotFoundException {
                        // 可以在这里进行文件处理,比如解密等.
                        return DecryptFile.getInPustream(file, ConfigData.DECODE_IMG_KEY);
                    }

                    @Override
                    public void close(InputStream inputStream) throws IOException {
                        if (inputStream != null)
                            inputStream.close();
                    }

                    @Override
                    public Class<InputStream> getDataClass() {
                        return InputStream.class;
                    }
                }));
    }


}
