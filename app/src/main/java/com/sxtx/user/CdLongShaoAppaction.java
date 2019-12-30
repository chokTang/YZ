package com.sxtx.user;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.net.http.HttpResponseCache;
import android.support.multidex.MultiDex;

import com.facebook.stetho.Stetho;
import com.fm.openinstall.OpenInstall;
import com.joanzapata.iconify.fonts.FontAwesomeModule;
import com.like.base.app.BaseApplication;
import com.like.base.app.LongshaoAPP;
import com.like.utilslib.UtilApp;
import com.like.utilslib.other.LogUtil;
import com.sxtx.user.activity.HomeActivity;
import com.sxtx.user.service.HeartService;
import com.sxtx.user.util.AppFrontBackHelper;
import com.sxtx.user.util.CrashHandlerUtils;
import com.sxtx.user.util.icon.FontLongModule;
import com.zxy.recovery.callback.RecoveryCallback;
import com.zxy.recovery.core.Recovery;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.IOException;

import event.HeartServiceEvents;

/**
 * Created by longshao on 2017/3/12.
 */

public class CdLongShaoAppaction extends BaseApplication {

    public static Context context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        /*解决方法总数超过3665的问题*/
        MultiDex.install(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LongshaoAPP.init(this)
                .withApiHost("http://192.168.0.2:8089")
//                .withInterceptor(new DebugInterceptor("AUserAccount/IsRegister", R.raw.home))
//                .withInterceptor(new DebugInterceptor("AUserAccount/Register", R.raw.home))
//                .withInterceptor(new DebugInterceptor("test", R.raw.test))
                .withIcon(new FontAwesomeModule())//设置默认的字体图标
                .withIcon(new FontLongModule())//设置自定义的字体图库
                .configure();
        //初始化崩溃util
        CrashHandlerUtils.getInstance().init(this);

        /*初始化数据图图形化工具,上线之后不需要*/
        if (BuildConfig.DEBUG) {
            initStetho();

            //crash日志
            Recovery.getInstance()
                    .debug(true)
                    .recoverInBackground(false)
                    .recoverStack(true)
                    .mainPage(HomeActivity.class)
                    .callback(new MyCrashCallback())
                    .silent(false, Recovery.SilentMode.RECOVER_ACTIVITY_STACK)
                    .init(this);
        }

        initBackStack();
        //初始化openinstall
        if (isMainProcess()) {
            OpenInstall.init(this);
        }
        //初始化svga缓存器
        try {
            File cacheFile = new File(context.getApplicationContext().getCacheDir(),"http");
            HttpResponseCache.install(cacheFile,1024*1024*128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initBackStack() {
        AppFrontBackHelper helper = new AppFrontBackHelper();
        helper.register((Application) context, new AppFrontBackHelper.OnAppStatusListener() {
            @Override
            public void onFront() {
                //应用切到前台处理
                EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_GESTURE_LOGIN,null));
                EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.TO_CHECK_PROXY,null));
                EventBus.getDefault().post(new HeartServiceEvents(HeartServiceEvents.VIDEO_NOT_RETRY,null));
            }

            @Override
            public void onBack() {
                //应用切到后台处理

            }
        });

    }

    public boolean isMainProcess() {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return getApplicationInfo().packageName.equals(appProcess.processName);
            }
        }
        return false;
    }

    /**
     * 初始化数据库图形化
     * 地址：chrome://inspect/#devices
     */
    private void initStetho() {
        Stetho.initialize(Stetho.newInitializerBuilder(this)
                .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                .build());
    }

    static final class MyCrashCallback implements RecoveryCallback {
        @Override
        public void stackTrace(String exceptionMessage) {
        }

        @Override
        public void cause(String cause) {
            LogUtil.loge("cause:" + cause);
        }

        @Override
        public void exception(String exceptionType, String throwClassName, String throwMethodName, int throwLineNumber) {
            LogUtil.loge("exceptionClassName:" + exceptionType + "\n"
                    + "throwClassName:" + throwClassName + "\n"
                    + "throwMethodName:" + throwMethodName + "\n"
                    + "throwLineNumber:" + throwLineNumber);
        }

        @Override
        public void throwable(Throwable throwable) {

        }
    }

    @Override
    public void onTerminate() {
        // 程序终止的时候执行
        super.onTerminate();
        Intent startIntent = new Intent(UtilApp.getIntance().getApplicationContext(), HeartService.class);
        UtilApp.getIntance().getApplicationContext().stopService(startIntent);
    }

    @Override
    public void onTrimMemory(int level) {
        // 程序在内存清理的时候执行（回收内存）
        // HOME键退出应用程序、长按MENU键，打开Recent TASK都会执行
        Intent startIntent = new Intent(UtilApp.getIntance().getApplicationContext(), HeartService.class);
        UtilApp.getIntance().getApplicationContext().stopService(startIntent);
        super.onTrimMemory(level);
    }


}
