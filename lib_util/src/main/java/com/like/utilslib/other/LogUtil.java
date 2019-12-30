package com.like.utilslib.other;

import com.like.utilslib.BuildConfig;
import com.orhanobut.logger.Logger;

/**
 * 日志打印类
 * Created by longshao on 2017/3/31.
 */

public class LogUtil {

    private static final boolean isDeg = BuildConfig.LOG_DEBUG;//正式上线之后可关闭

    public static void logd(String str) {
        if (isDeg) {
            Logger.d(str);
        }
    }

    public static void loge(String str) {
        if (isDeg) {
            Logger.e(str);
        }
    }

    public static void logi(String str) {
        if (isDeg) {
            Logger.i(str);
        }
    }

    public static void logv(String str) {
        if (isDeg) {
            Logger.v(str);
        }
    }

    public static void logw(String str) {
        if (isDeg) {
            Logger.w(str);
        }
    }

}
