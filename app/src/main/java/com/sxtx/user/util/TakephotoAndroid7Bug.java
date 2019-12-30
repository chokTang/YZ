package com.sxtx.user.util;

import android.os.StrictMode;

public class TakephotoAndroid7Bug {
    public static void dealWithAndroid7Bug(){
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }
}
