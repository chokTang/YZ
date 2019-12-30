package com.sxtx.user.util;

import android.app.ActivityManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.TextUtils;

import com.like.utilslib.app.AppUtil;
import com.like.utilslib.other.LogUtil;

import java.util.ArrayList;

public class AppUtils {


    //系统剪贴板-获取:
    public static String getCopy(Context context) {
        String tag = "com.rongwan.dabaitu=";
        // 获取系统剪贴板
        try {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            // 返回数据
            ClipData clipData = clipboard.getPrimaryClip();
            if (clipData != null && clipData.getItemCount() > 0) {
                String string = "";
                try {
                    // 从数据集中获取（粘贴）第一条文本数据
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        if (clipData.getItemAt(i).getText().toString().startsWith(tag)) {
                            string = clipData.getItemAt(i).getText().toString().substring(tag.length());
                        }
                    }

                } catch (Exception e) {
                    LogUtil.loge(e.toString());
                    return "";
                }

                return string;
            }
        } catch (Exception e) {
            LogUtil.loge(e.toString());
            return "";
        }

        return "";
    }


    //系统剪贴板-复制:   s为内容
    public static void copy(Context context, String s) {
        // 获取系统剪贴板
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        // 创建一个剪贴数据集，包含一个普通文本数据条目（需要复制的数据）
        ClipData clipData = ClipData.newPlainText(null, s);
        // 把数据集设置（复制）到剪贴板
        clipboard.setPrimaryClip(clipData);

    }


    public static  String getP(){
        AppPreference appPreference= AppPreference.getIntance();
        String passTWO= appPreference.getPasswordTwo();
        String passOne=appPreference.getPasswordOne();
        return !TextUtils.isEmpty(passTWO)?passTWO:TextUtils.isEmpty(passOne)?"":passOne;
    }

    public static String getDeviceId(){
        String deviceId=AppPreference.getIntance().getDeviceId();
        return TextUtils.isEmpty(deviceId)? AppUtil.getIMEI():deviceId;
    }



    public static boolean isWorked(String className,Context mcontext) {
        ActivityManager myManager = (ActivityManager)mcontext
                .getApplicationContext().getSystemService(
                        Context.ACTIVITY_SERVICE);
        ArrayList<ActivityManager.RunningServiceInfo> runningService = (ArrayList<ActivityManager.RunningServiceInfo>) myManager
                .getRunningServices(30);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString()
                    .equals(className)) {
                return true;
            }
        }
        return false;

    }


}
