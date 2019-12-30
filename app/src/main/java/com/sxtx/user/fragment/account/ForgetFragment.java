package com.sxtx.user.fragment.account;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.like.base.base.BaseToolbarFragment;
import com.sxtx.user.R;
import com.sxtx.user.mvp.presenter.account.ForgetPresenter;
import com.sxtx.user.mvp.view.account.IForgetView;
import com.sxtx.user.util.timer.BaseTimerTask;
import com.sxtx.user.util.timer.ITimerListener;

import java.util.Timer;

/**
 * 忘记密码
 * Created by Administrator on 2017/12/30.
 */

public class ForgetFragment extends BaseToolbarFragment<ForgetPresenter> implements IForgetView,
        ITimerListener {

    //传过来的参数
    private static final String PARAMS_PHONE = "PARAMS_PHONE";
    private String mPhone = "";

    //倒计时
    private Timer mTimer = null;
    private int mCount = 60;

    public static ForgetFragment newIntance(String phone) {
        ForgetFragment fragment = new ForgetFragment();
        Bundle args = new Bundle();
        args.putString(PARAMS_PHONE, phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        onBack();
        setToolbarTitle("修改密碼");
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args != null) {
            mPhone = args.getString(PARAMS_PHONE);
        }
    }

    @Override
    public Object getResId() {
        return R.layout.fragment_forget_layout;
    }


    @Override
    public void onShowSendCodeResult(boolean result) {
        if (result) {
            mCount = 60;
            final BaseTimerTask task = new BaseTimerTask(this);
            mTimer.schedule(task, 0, 1000);
        } else {
//            send_code_bt.setText("发送失败");
        }
    }

    @Override
    public void onModityPasswordResult(boolean result) {
        if (result) {
//            AppPreference.getIntance().setAccountPassword(newPasswordContent);
            pop();
        }
    }

    @Override
    public void onTimer() {
//        getBaseActivity().runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                if (send_code_bt != null) {
//                    send_code_bt.setClickable(false);
//                    send_code_bt.setText(MessageFormat.format("重发{0}s", mCount));
//                    mCount--;
//                    if (mCount < 0) {
//                        if (mTimer != null) {
//                            mTimer.cancel();
//                            mTimer = null;
//                            send_code_bt.setClickable(true);
//                            send_code_bt.setText("发送验证码");
//                        }
//                    }
//                }
//            }
//        });
    }
}
