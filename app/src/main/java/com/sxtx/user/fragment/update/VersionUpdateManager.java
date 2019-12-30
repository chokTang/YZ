package com.sxtx.user.fragment.update;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.support.v4.app.FragmentManager;

import com.like.utilslib.app.ActivityUtil;
import com.like.utilslib.app.AppUtil;
import com.like.utilslib.file.FileUtil;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * ba
 * Created by 55 on 2017/7/19.
 */

public class VersionUpdateManager {
    private Context context;

    private VersionUpdateManager(Context context) {
        this.context = context;
    }

    public static VersionUpdateManager versionUpdateManager;

    public static VersionUpdateManager getInstance(Context context) {
        if (versionUpdateManager == null) {
            synchronized (VersionUpdateManager.class) {
                if (versionUpdateManager == null) {
                    versionUpdateManager = new VersionUpdateManager(context);
                }
            }
        }
        return versionUpdateManager;
    }

    public void showUpdateHint(FragmentManager fragmentManager, final UpdateInfoModel model) {
        //判断是否升级
        if (AppUtil.getVersionCode() < model.getVersionCode()) {
            final UpdateHintDialog dialog = new UpdateHintDialog();
            dialog.setContent(model.getVersionDesc());
            dialog.setOnUpdateListener(new UpdateHintDialog.OnUpdateListener() {
                @Override
                public void onUpdate() {
                    String apkName = "xsqq_" + model.getVersionCode() + ".apk";
                    final File file = FileUtil.createFile("xsqq", apkName);
                    if (file.exists()) file.delete();
                    if (!file.exists()) {
                        new Thread() {
                            @Override
                            public void run() {
                                try {
                                    String apkName = "xsqq_" + model.getVersionCode() + ".apk";
                                    final File file = downloadFile(model.getDownloadUrl(), apkName, dialog);
                                    ((Activity) context).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            AppUtil.installAPK(context, file);
//                                            ActivityUtil.getAppManager().AppExit(context);
                                        }
                                    });
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    } else {
                        dialog.setMax(100);
                        dialog.setCurrentProgress(100);
                        AppUtil.installAPK(context, file);
//                        ActivityUtil.getAppManager().AppExit(context);
                    }
                }

                @Override
                public void onCancle() {
                    //如果低于最低版本,强制升级
                    if (AppUtil.getVersionCode() < model.getForceUpdateLimit()) {
                        ActivityUtil.getAppManager().AppExit(context);
                    }
                }
            });
            dialog.show(fragmentManager, "updatedhintialog");
        }
    }

    /**
     * 从服务器下载最新更新文件
     *
     * @param path 下载路径
     * @param pd   进度条
     * @return
     * @throws Exception
     */
    private static File downloadFile(String path, String appName, UpdateHintDialog pd) throws Exception {
        // 如果相等的话表示当前的sdcard挂载在手机上并且是可用的
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5000);
            // 获取到文件的大小
            pd.setMax(conn.getContentLength());
            InputStream is = conn.getInputStream();
            File file = FileUtil.createFile("xsqq", appName);
            // 目录不存在创建目录
            if (!file.exists()) {
                FileOutputStream fos = new FileOutputStream(file);
                BufferedInputStream bis = new BufferedInputStream(is);
                byte[] buffer = new byte[1024];
                int len;
                int total = 0;
                while ((len = bis.read(buffer)) != -1) {
                    fos.write(buffer, 0, len);
                    total += len;
                    // 获取当前下载量
                    pd.setCurrentProgress(total);
                }
                fos.close();
                bis.close();
                is.close();
            }
            return file;
        } else {
            throw new IOException("未發現有SD卡");
        }
    }
}
