package com.sxtx.user.fragment.update;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import com.like.utilslib.file.FileUtil;

import java.io.File;

/**
 * 下载安装后接受到的广播
 */
public class InstalledReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //系统下载管理器,下载完成完成安装广播
        if (intent.getAction() != null && intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadApkId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            DownloadManager downManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
            if (downManager != null) {
                Uri downloadFileUri = downManager.getUriForDownloadedFile(downloadApkId);
                Intent install = new Intent(Intent.ACTION_VIEW);
                install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
                install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(install);
            }
        }

        //接收安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            String packageName = intent.getDataString();
            if (isAddedOrReplacedMyApp(context, packageName)) {
                deleteInstallPackage();
            }
        }
        //接收覆盖安装广播
        if (intent.getAction().equals("android.intent.action.PACKAGE_REPLACED")) {
            String packageName = intent.getDataString();
            if (isAddedOrReplacedMyApp(context, packageName)) {
                deleteInstallPackage();
            }
        }
//        //接收卸载广播
//        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
//            String packageName = intent.getDataString();
//            Log.e("zzzzzz", "卸载了:" + packageName + " version: " + VersionUtils.getVersion());
//        }
    }

    //判断是否安装或更新了当前应用【从intent中获取的packageName中的内容为 <package:com.nbeebank.licaishi>】
    private boolean isAddedOrReplacedMyApp(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName))
            return false;
        if (!packageName.contains(":")) {
            return false;
        }
        if (packageName.split(":")[1].equals(context.getApplicationContext().getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 删除无用安装包
     * 虽然并不推荐在BroadcastReceiver中执行或开启线程执行耗时操作
     * 但考虑到操作并不是前台必须的操作，即使线程中止也不会有任何的影响，经测试该线程执行时间均为0ms【！！】，且开启服务开销较大
     * 如有需要可后期优化
     */
    private void deleteInstallPackage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    File installDir = FileUtil.createDir("xsqq");
                    if (installDir.exists() && installDir.isDirectory()) {
                        long startTime = SystemClock.currentThreadTimeMillis();
                        File[] apk = installDir.listFiles();
                        for (int i = 0; i < apk.length; i++) {
                            apk[i].delete();
                        }
                    }
                } catch (Exception ex) {
                    Log.e("deleteInstallPackage", "deleteInstallPackage.Exception >> " + ex.getMessage());
                }
            }
        }).start();
    }
}
