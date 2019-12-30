package com.like.base.wechat.template;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.like.base.BuildConfig;
import com.like.base.wechat.LongWeChat;

/**
 * Created by longshao on 2017/8/14.
 */

public class AppRegisterTemplate extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //注册到微信里面
        LongWeChat.getIntance().getWXAPI().registerApp(BuildConfig.WX_APPID);
    }
}
