package com.sxtx.user.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.fm.openinstall.OpenInstall;
import com.fm.openinstall.listener.AppWakeUpAdapter;
import com.fm.openinstall.model.AppData;
import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.sxtx.user.fragment.launcher.LauncherFragment;
import com.sxtx.user.inter.ILoginState;

/**
 * 闪屏引导页
 * Created by Administrator on 2017/10/15.
 */

public class GuideActivity extends BaseActivity implements ILoginState {
    @Override
    public BaseFragment setRootFragment() {
        return new LauncherFragment();
    }
    Handler handler = new Handler(Looper.getMainLooper());
    @Override
    public void enterLoginPage() {
        startActivity(new Intent(this, AccountActivty.class));
        GuideActivity.this.finish();
    }

    @Override
    public void enterHomePage() {
        startActivity(new Intent(this, HomeActivity.class));
//        startActivity(new Intent(this, AccountActivty.class));
         GuideActivity.this.finish();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        wakeUpAdapter = null;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        // 此处要调用，否则App在后台运行时，会无法截获
        OpenInstall.getWakeUp(intent, wakeUpAdapter);
    }
    AppWakeUpAdapter wakeUpAdapter = new AppWakeUpAdapter() {
        @Override
        public void onWakeUp(AppData appData) {
            //获取渠道数据
            String channelCode = appData.getChannel();
            //获取绑定数据
            String bindData = appData.getData();

        }
    };
}
