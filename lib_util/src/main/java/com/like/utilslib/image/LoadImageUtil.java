package com.like.utilslib.image;

import android.animation.ObjectAnimator;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.like.utilslib.R;
import com.like.utilslib.UtilApp;
import com.like.utilslib.image.helper.GlideCircleTransform;
import com.like.utilslib.image.helper.GlideRoundTransform;
import com.like.utilslib.screen.DensityUtil;

/**
 * 加载图片工具类
 * Created by longshao on 2017/5/10.
 */

public class LoadImageUtil {

    private static final boolean isMeoryCache = false;//是否缓存

    /**
     * 加载图片
     *
     * @param img
     * @param imageRes
     */
    public static void loadImage(ImageView img, Object imageRes) {
        loadImage(img, imageRes, R.drawable.image_load_error);
    }



    /**
     * 加载图片
     *
     * @param img
     * @param imageRes
     * @param defaltImg
     */
    public static void loadImage(final ImageView img, Object imageRes, Integer defaltImg) {
        final ImageView.ScaleType scaleType = img.getScaleType();
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        final ObjectAnimator anim = ObjectAnimator.ofInt(img, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(UtilApp.getIntance().getApplicationContext())
                .load(imageRes)
                .placeholder(R.drawable.glide_rotateimage)
                .error(defaltImg)
                .dontAnimate()
                .fitCenter()
                .skipMemoryCache(isMeoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }
                })
                .into(img);
    }

    /**
     * 加载特定宽度网络图片
     *
     * @param img
     * @param imageUrl
     * @param width    dp
     * @param height   dp
     */
    public static void loadSizeImage(final ImageView img, Object imageUrl, int width, int height) {
        loadSizeImage(img, imageUrl, width, height, R.drawable.image_load_error);
    }

    /**
     * 加载特定宽度网络图片
     *
     * @param img
     * @param imageUrl
     * @param width     dp
     * @param height    dp
     * @param defaltImg
     */
    public static void loadSizeImage(final ImageView img, Object imageUrl, int width, int height, Integer defaltImg) {
        width = DensityUtil.dpTopx(width);
        height = DensityUtil.dpTopx(height);

        final ImageView.ScaleType scaleType = img.getScaleType();
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        final ObjectAnimator anim = ObjectAnimator.ofInt(img, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(UtilApp.getIntance().getApplicationContext())
                .load(imageUrl)
                .override(width, height)
                .placeholder(R.drawable.glide_rotateimage)
                .error(defaltImg)
                .dontAnimate()
                .skipMemoryCache(isMeoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }
                })
                .into(img);
    }

    /**
     * 默认的加载圆角图片
     *
     * @param img
     * @param imageUrl
     * @param radius   圆角度数 dp
     */
    public static void loadRadiusImage(final ImageView img, Object imageUrl, int radius) {
        loadRadiusImage(img, imageUrl, radius, R.drawable.image_load_error);
    }

    /**
     * 默认的加载圆角图片
     *
     * @param img
     * @param imageUrl
     * @param radius    圆角度数 dp
     * @param defaltImg
     */
    public static void loadRadiusImage(final ImageView img, Object imageUrl, int radius, Integer defaltImg) {
        radius = DensityUtil.dpTopx(radius);

        final ImageView.ScaleType scaleType = img.getScaleType();
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        final ObjectAnimator anim = ObjectAnimator.ofInt(img, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(UtilApp.getIntance().getApplicationContext())
                .load(imageUrl)
                .transform(new GlideRoundTransform(radius))
                .placeholder(R.drawable.glide_rotateimage)
                .error(defaltImg)
                .dontAnimate()
                .skipMemoryCache(isMeoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }
                })
                .into(img);
    }

    /**
     * 默认的加载固定大小的固定圆角图片
     *
     * @param img
     * @param imageUrl
     * @param width
     * @param height
     * @param radius   圆角度数 dp
     */
    public static void loadSizeRadiusImage(final ImageView img, Object imageUrl, int width, int height, int radius) {
        width = DensityUtil.dpTopx(width);
        height = DensityUtil.dpTopx(height);
        radius = DensityUtil.dpTopx(radius);

        final ImageView.ScaleType scaleType = img.getScaleType();
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        final ObjectAnimator anim = ObjectAnimator.ofInt(img, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(UtilApp.getIntance().getApplicationContext())
                .load(imageUrl)
                .transform(new GlideRoundTransform(radius))
                .override(width, height)
                .placeholder(R.drawable.glide_rotateimage)
                .error(R.drawable.image_load_error)
                .dontAnimate()
                .skipMemoryCache(isMeoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        img.setScaleType(scaleType);
                        anim.cancel();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        img.setScaleType(scaleType);
                        anim.cancel();
                        return false;
                    }
                })
                .into(img);
    }

    /**
     * 默认的加载固定大小圆角图片
     *
     * @param img
     * @param imageUrl
     * @param widthorHeight 图片大小 dp
     */
    public static void loadRoundSizeImage(final ImageView img, Object imageUrl, int widthorHeight) {
        loadRoundSizeImage(img, imageUrl, widthorHeight, R.drawable.image_load_error);
    }

    /**
     * 默认的加载固定大小圆角图片
     *
     * @param img
     * @param imageUrl
     * @param widthorHeight 图片大小 dp
     * @param defaltImg
     */
    public static void loadRoundSizeImage(final ImageView img, Object imageUrl, int widthorHeight, Integer defaltImg) {
        int widthorHeightother = DensityUtil.dpTopx(widthorHeight);

        final ImageView.ScaleType scaleType = img.getScaleType();
        img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        final ObjectAnimator anim = ObjectAnimator.ofInt(img, "ImageLevel", 0, 10000);
        anim.setDuration(800);
        anim.setRepeatCount(ObjectAnimator.INFINITE);
        anim.start();

        Glide.with(UtilApp.getIntance().getApplicationContext())
                .load(imageUrl)
                .transform(new GlideCircleTransform())
                .override(widthorHeightother, widthorHeightother)
                .placeholder(R.drawable.glide_rotateimage)
                .error(defaltImg)
                .dontAnimate()
                .skipMemoryCache(isMeoryCache)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        anim.cancel();
                        img.setScaleType(scaleType);
                        return false;
                    }
                })
                .into(img);
    }

    /**
     * 清理内存缓存
     */
    public static void clearGlideMemory() {
        Glide.get(UtilApp.getIntance().getApplicationContext()).clearMemory();//  可以在UI主线程中进行
    }

    /**
     * 清理磁盘缓存
     */
    public static void clearGlideDiskCache() {
        Glide.get(UtilApp.getIntance().getApplicationContext()).clearDiskCache();//清理磁盘缓存 需要在子线程中执行
    }
}