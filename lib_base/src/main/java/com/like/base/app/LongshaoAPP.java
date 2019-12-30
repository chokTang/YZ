package com.like.base.app;

import android.content.Context;

import com.like.base.app.config.ConfigType;
import com.like.base.app.config.Configurator;

/**
 * 用于管理整个APP的类
 * Created by longshao on 2017/7/22.
 */

public final class LongshaoAPP {

    public static Configurator init(Context context) {
        getConfigurator().getLongConfig().put(ConfigType.APPLICATION_CONTEXT, context.getApplicationContext());
        return Configurator.getIntance();
    }

    public static Configurator getConfigurator() {
        return Configurator.getIntance();
    }

    public static <T> T getConfiguration(Object key) {
        return getConfigurator().getConfiguration(key);
    }

    public static Context getApplicationContext() {
        return getConfiguration(ConfigType.APPLICATION_CONTEXT);
    }
}
