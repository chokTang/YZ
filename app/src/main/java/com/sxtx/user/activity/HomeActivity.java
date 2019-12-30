package com.sxtx.user.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.like.utilslib.app.ActivityUtil;
import com.sxtx.user.ConfigData;
import com.sxtx.user.fragment.MainFragment;
import com.sxtx.user.mvp.presenter.LoginFlowPresenter;
import com.sxtx.user.util.AppPreference;
import com.sxtx.user.util.CheckNetworkProxyUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import event.HeartServiceEvents;


/**
 * 主页
 * Created by longshao on 2017/3/12.
 */

public class HomeActivity extends BaseActivity {

    private long exitTime = 0;
    private LoginFlowPresenter presenter;

    @Override
    public BaseFragment setRootFragment() {
        return new MainFragment();
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        EventBus.getDefault().register(this);
        presenter = new LoginFlowPresenter();
        presenter.setActivity(HomeActivity.this);
        openGueset();
    }

    Handler mhandle = new Handler();


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandle(HeartServiceEvents message) {
        switch (message.getEventId()) {
            case HeartServiceEvents.TO_START_LOGIN:
                if (!isClickTooFast()) presenter.getLoginUrl();
                break;
            case HeartServiceEvents.TO_GESTURE_LOGIN:
                openGueset();
                break;
            case HeartServiceEvents.TO_BREAK_LINE_RESPONSE:
                showToast("你的賬號已在別處登錄");
                mhandle.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        /*EventBus.getDefault().post(new );*/
                        ConfigData.OTHER_PHONE = "";
                        ConfigData.OTHER_PASSW = "";
                        ActivityUtil.getAppManager().finishAllActivity();
                        System.exit(0);
                    }
                }, 2000);
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusCheck(HeartServiceEvents message) {
        switch (message.getEventId()) {
            case HeartServiceEvents.TO_CHECK_PROXY:
                if (CheckNetworkProxyUtil.isWifiProxy(HomeActivity.this)) {
                    System.exit(0);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private static final int CLICK_TIME_DELAY = 100; // ms
    private static long lastClickTime = 0L;

    protected boolean isClickTooFast() {
        boolean isClickTooFast = false;
        if (SystemClock.elapsedRealtime() - lastClickTime < CLICK_TIME_DELAY) {
            isClickTooFast = true;
        }
        lastClickTime = SystemClock.elapsedRealtime();
        return isClickTooFast;
    }


    public void openGueset() {
        if (AppPreference.getIntance().getIsGeSture() && !TextUtils.isEmpty(AppPreference.getIntance().getGeSture())) {
            startActivity(new Intent(HomeActivity.this, GestureActivity.class));
        }
    }

    @Override
    public void onBackPressedSupport() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            pop();
        } else {
            long nowTime = System.currentTimeMillis();
            if (nowTime - exitTime > 2000) {
                showToast("再點擊一次退出登錄");
                exitTime = nowTime;
            } else {
                finish();
            }
        }
    }


}
