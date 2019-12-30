package com.like.base.app.config;

import com.joanzapata.iconify.IconFontDescriptor;
import com.joanzapata.iconify.Iconify;

import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.Interceptor;

/**
 * APP的配置文件
 * Created by longshao on 2017/7/22.
 */

public class Configurator {
    private static final HashMap<Object, Object> LONG_CONFIG = new HashMap<>();
    private static final ArrayList<IconFontDescriptor> ICONS = new ArrayList<>();//字体图片库
    private static final ArrayList<Interceptor> INTERCEPTORS = new ArrayList<>();//拦截器

    private Configurator() {
        LONG_CONFIG.put(ConfigType.CONFIG_REDAY, false);//APP还未配置完成
    }

    public static Configurator getIntance() {
        return Builder.INTANCE;
    }

    public final HashMap<Object, Object> getLongConfig() {
        return LONG_CONFIG;
    }

    /**
     * 初始化配置服务器公用的地址
     *
     * @param host
     * @return
     */
    public final Configurator withApiHost(String host) {
        LONG_CONFIG.put(ConfigType.API_HOST, host);
        return this;
    }

    /**
     * 初始化配置公司客服电话
     *
     * @param phoneNumber
     * @return
     */
    public final Configurator withServicePhone(String phoneNumber) {
        LONG_CONFIG.put(ConfigType.SERVICE_PHONE, phoneNumber);
        return this;
    }


    /**
     * 初始化配置公司客服电话
     *
     * @param phoneNumber
     * @return
     */
    public final Configurator withApiThreeHost(String phoneNumber) {
        LONG_CONFIG.put(ConfigType.THREE_HOST, phoneNumber);
        return this;
    }

    /**
     * 初始化配置服务器公用的地址3
     *
     * @param otherHost
     * @return
     */
    public final Configurator withApiOhterHost(String otherHost) {
        LONG_CONFIG.put(ConfigType.OTHER_HOST, otherHost);
        return this;
    }

    /**
     * 加载图片字体配置文件
     *
     * @param descriptor
     * @return
     */
    public final Configurator withIcon(IconFontDescriptor descriptor) {
        ICONS.add(descriptor);
        return this;
    }

    /**
     * 加入测试拦截器
     *
     * @param interceptor
     * @return
     */
    public final Configurator withInterceptor(Interceptor interceptor) {
        INTERCEPTORS.add(interceptor);
        LONG_CONFIG.put(ConfigType.INTERCEPTOR, INTERCEPTORS);
        return this;
    }


    /**
     * 加入测试拦截器
     *
     * @param interceptors
     * @return
     */
    public final Configurator withInterceptors(ArrayList<Interceptor> interceptors) {
        INTERCEPTORS.addAll(interceptors);
        LONG_CONFIG.put(ConfigType.INTERCEPTOR, INTERCEPTORS);
        return this;
    }

    /**
     * 配置完成
     */
    public final void configure() {
        initIcons();
        LONG_CONFIG.put(ConfigType.CONFIG_REDAY, true);//设置初始化完成
    }


    @SuppressWarnings("unused")
    public final <T> T getConfiguration(Object key) {
        checkConfiguration();
        final Object value = LONG_CONFIG.get(key);
        if (value == null)
            return null;
        return (T) LONG_CONFIG.get(key);
    }

    /**
     * 实现单例
     */
    private static class Builder {
        private static final Configurator INTANCE = new Configurator();
    }

    /**
     * 初始化图片配置，第一个是系统的默认配置
     */
    private void initIcons() {
        if (ICONS.size() > 0) {
            final Iconify.IconifyInitializer initializer = Iconify.with(ICONS.get(0));
            for (int i = 1; i < ICONS.size(); i++) {
                initializer.with(ICONS.get(i));
            }
        }
    }

    /**
     * 检查是否初始化配置
     */
    private void checkConfiguration() {
        final boolean isReady = (boolean) LONG_CONFIG.get(ConfigType.CONFIG_REDAY);
        if (!isReady) {
            throw new RuntimeException("Configurator is not ready,call Configurator");
        }
    }
}
