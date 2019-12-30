package com.sxtx.user.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.facebook.stetho.common.LogUtil;
import com.king.app.updater.AppUpdater;
import com.king.app.updater.callback.UpdateCallback;
import com.sxtx.user.CdLongShaoAppaction;






public class UpdateManager {

    private static UpdateManager instance;
    private static String version;



    private UpdateManager() {
    }

    /**
     * 获取单例
     *
     * @return UpdateManager
     */
    public static UpdateManager getInstance() {
        if (instance == null) {
            instance = new UpdateManager();
        }
        return instance;
    }

    public static String getVersion() {
        if (version == null) {
            version = getLocalVersion();
        }
        return version;
    }


    /**
     * 获取本地包的版本
     *
     * @return 版本
     */
    private static String getLocalVersion() {
        try {
            PackageManager packageManager = CdLongShaoAppaction.context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(CdLongShaoAppaction.context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 版本比对
     *
     * @param server 服务器版本
     * @return 是否需要更新
     */
    String local=getLocalVersion();  //本地版本
    public boolean isNeedUpdate( String server) {
        LogUtil.e("VERSION",local+"==="+server);
        if (TextUtils.isEmpty(local)) {
            return false;
        }

        String[] v1 = TextUtils.split(local, "\\.");
        String[] v2 = TextUtils.split(server, "\\.");

        for (int i = 0, l = v2.length; i < l; ++i) {
            if (Integer.parseInt(v2[i]) > Integer.parseInt(v1[i])) {
                return true;
            }else if(Integer.parseInt(v2[i]) < Integer.parseInt(v1[i])){
                return false;
            }
        }

        return false;
    }

    /**
     * 下载并自动安装apk
     *
     * @param context  上下文
     * @param url      下载地址
     * @param callback 进度回调
     */
    public void download(Context context, String url, UpdateCallback callback) {

        new AppUpdater.Builder()
                .setShowNotification(false)
                .setSound(false)
                .setVibrate(false)
                .setReDownload(false)
                .serUrl(url)
                // .setPath(Environment.getDownloadCacheDirectory().getAbsolutePath() + "/RedPacketTopOneDownload")
                .build(context)
                .setUpdateCallback(callback)
                .start();
    }


}
