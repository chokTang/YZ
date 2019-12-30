package com.sxtx.user.fragment.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.like.base.base.BaseStatusToolbarFragment;
import com.like.base.widget.ClearEditText;
import com.sxtx.user.R;
import com.sxtx.user.model.account.UserModel;
import com.sxtx.user.mvp.presenter.account.RegisterPresenter;
import com.sxtx.user.mvp.view.account.IRegisterView;
import com.sxtx.user.util.timer.BaseTimerTask;
import com.sxtx.user.util.timer.ITimerListener;

import java.text.MessageFormat;
import java.util.Timer;

/**
 * 注册页面
 * Created by Administrator on 2017/12/30.
 */

public class RegisterFragment extends BaseStatusToolbarFragment<RegisterPresenter> implements IRegisterView
        , ITimerListener, View.OnClickListener {


    TextView tv_binding;
    TextView send_code_bt;
    ClearEditText edt_account;
    EditText edt_psd;

    //倒计时
    private Timer mTimer = null;
    private int mCount = 60;

    @Override
    public Integer getMainResId() {
        return R.layout.fragment_register_layout;
    }

    @Override
    public void onInitView(Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        onBack();
        setToolbarTitle("註冊");


        tv_binding = $(R.id.tv_binding);
        edt_account = $(R.id.edt_account);
        edt_psd = $(R.id.edt_psd);
        send_code_bt = $(R.id.tv_send_code);

        showView(MAIN_VIEW);

        tv_binding.setOnClickListener(this);
        send_code_bt.setOnClickListener(this);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        mTimer = new Timer();
//        mPresenter.getTestData();


    }



    @Override
    public void onShowSendCodeResult(boolean iSuccess) {
        if (iSuccess) {
            mCount = 60;
            final BaseTimerTask task = new BaseTimerTask(this);
            mTimer.schedule(task, 0, 1000);
        } else {

        }
    }

    @Override
    public void onRegisterResult(UserModel model) {
        if (model == null) {
            showToast("用户注册失败");
        } else {
//            AppPreference.getIntance().setAccountData(model);
//            AppPreference.getIntance().setAccountPassword(passwordContent);
//            AppPreference.getIntance().setAccountName(phoneNumber);
//            AppPreference.getIntance().setAccountLonginState(true);
//            startActivity(new Intent(getBaseActivity(), HomeActivity.class));
//            getBaseActivity().finish();
        }
    }

    @Override
    public void onGetTestData(String content) {

    }

    @Override
    public void onTimer() {
        getBaseActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (send_code_bt != null) {
                    send_code_bt.setClickable(false);
                    send_code_bt.setText(MessageFormat.format("{0}s", mCount));
                    mCount--;
                    if (mCount < 0) {
                        if (mTimer != null) {
                            mTimer.cancel();
                            mTimer = null;
                            send_code_bt.setClickable(true);
                            send_code_bt.setText("发送验证码");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_send_code://發送驗證碼
                mTimer = new Timer();
                getBaseActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (send_code_bt != null) {
                            send_code_bt.setClickable(false);
                            send_code_bt.setText(MessageFormat.format("{0}s", mCount));
                            mCount--;
                            if (mCount < 0) {
                                if (mTimer != null) {
                                    mTimer.cancel();
                                    mTimer = null;
                                    send_code_bt.setClickable(true);
                                    send_code_bt.setText("發送驗證碼");
                                }
                            }
                        }
                    }
                });
                break;
            case R.id.tv_binding://綁定手機
                showToast("綁定手機");
                break;
        }
    }
}
