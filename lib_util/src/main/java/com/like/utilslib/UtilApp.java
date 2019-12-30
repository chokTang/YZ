package com.like.utilslib;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

/**
 * 工具类初始化
 */
public final class UtilApp {

    private Context mContext;

    private static final class Holder {
        private static final UtilApp INSTANCE = new UtilApp();
    }

    public static UtilApp getIntance() {
        return Holder.INSTANCE;
    }

    public void init(Context context) {
        mContext = context;

        /*初始化log打印器*/
        Logger.addLogAdapter(new AndroidLogAdapter());
    }

    public Context getApplicationContext() {
        if (mContext == null)
            throw new RuntimeException("UtilApp is not init");
        return mContext;
    }
}
