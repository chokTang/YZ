package com.like.utilslib.image.config;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.AppGlideModule;
import com.bumptech.glide.request.RequestOptions;

/**
 * gilde 配置
 * 注意：GildeApp 渲染不出来，需要make Project 才可以出来
 */
@GlideModule
public final class MyAppGlideModule extends AppGlideModule {

    private final int cacheSizeBytes = 1024 * 1024 * 500;//500MB

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {
        /*设置缓存地址 ：context.getFilesDir() 目录为： /data/data/app_package_name/files */
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context,
                context.getFilesDir() + "/img/glide", cacheSizeBytes));

        /*设置内存缓存大小，根据手机的屏幕和内存大小，自动获取*/
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .setMemoryCacheScreens(2)
                .build();
        builder.setMemoryCache(new LruResourceCache(calculator.getMemoryCacheSize()));

        /*设置Bitmap池大小，根据手机的屏幕和内存大小，自动获取*/
        MemorySizeCalculator bimapCalculater = new MemorySizeCalculator.Builder(context)
                .setBitmapPoolScreens(3)
                .build();
        builder.setBitmapPool(new LruBitmapPool(bimapCalculater.getBitmapPoolSize()));

        /*设置图片格式转换 可在此全局配置*/
        builder.setDefaultRequestOptions(new RequestOptions()
                .format(DecodeFormat.PREFER_RGB_565)
                .disallowHardwareConfig());
    }




}
