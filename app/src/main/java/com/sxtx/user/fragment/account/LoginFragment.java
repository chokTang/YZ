package com.sxtx.user.fragment.account;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.like.base.base.BaseStatusToolbarFragment;
import com.like.base.base.rxjava.CommonEvent;
import com.like.base.widget.ClearEditText;
import com.like.utilslib.UtilApp;
import com.like.utilslib.other.RegexUtil;
import com.lyh.protocol.data.PublicData;
import com.sxtx.user.R;
import com.sxtx.user.fragment.me.BingdingPhoneFragment;
import com.sxtx.user.mvp.presenter.account.LoginPresenter;
import com.sxtx.user.mvp.view.account.ILoginView;
import com.sxtx.user.service.HeartService;
import com.sxtx.user.util.AppUtils;
import com.sxtx.user.util.NetWorkUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import event.HeartServiceEvents;

/**
 * 登录页面
 */

public class LoginFragment extends BaseStatusToolbarFragment<LoginPresenter> implements ILoginView, View.OnClickListener {

    public static final String KEY_TOKEN = "key_token";
    public static final String KEY_PHONE = "key_phone";
    TextView tv_login;
    //    TextView tv_register;
    TextView tv_bind;
    ClearEditText edt_account;
    EditText edt_psd;
    boolean isBind = false;
    String phone = "";


    public static LoginFragment newInstance(boolean isCallBack, boolean isBind, String phone) {

        Bundle args = new Bundle();
        args.putBoolean("isCallBack", isCallBack);
        args.putBoolean("isBind", isBind);
        args.putString(KEY_PHONE, phone);
        LoginFragment fragment = new LoginFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Integer getMainResId() {
        return R.layout.fragment_login_layout;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        onBack();
        setToolbarTitle("用戶登錄");
        EventBus.getDefault().register(this);
        Bundle args = getArguments();
        if (args != null) {
            isBind = args.getBoolean("isBind");
            phone = args.getString(KEY_PHONE);
        }


        tv_login = $(R.id.tv_login);
//        tv_register = $(R.id.tv_register);
        tv_bind = $(R.id.tv_bind);
        edt_account = $(R.id.edt_account);
        if (TextUtils.isEmpty(phone))
            edt_account.setText(phone);
        edt_psd = $(R.id.edt_psd);

        showView(MAIN_VIEW);

        tv_login.setOnClickListener(this);
        tv_bind.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }


    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {


    }

    @Override
    public void onLoadData() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void eventBusHandle(HeartServiceEvents message) {
        if (message.getEventId() == HeartServiceEvents.TO_RETURN) {
            pop();
        }
    }

    @Override
    public void sendHeart() {
        showToast("登錄成功");
        if (!AppUtils.isWorked("com.sxtx.user.service.HeartService",getActivity())){
            Intent startIntent = new Intent(UtilApp.getIntance().getApplicationContext(), HeartService.class);
            UtilApp.getIntance().getApplicationContext().startService(startIntent);
        }
        pop();
    }

    @Override
    public void getDataSucc(List<PublicData.AdvertisingData> list) {

    }

    @Override
    public void loginSucc() {
//        pop();
    }


    @Override
    public void handleEvent(CommonEvent event) {
    }


    @Override
    public boolean isRxBus() {
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_login://登陸
                if (TextUtils.isEmpty(edt_account.getText().toString().trim())){
                    edt_account.requestFocus();
                    edt_account.setSelection(edt_account.getText().length());
                    showToast("請輸入手機號");
                    return;
                }
                if (!RegexUtil.checkMobile(edt_account.getText().toString().trim())){
                    edt_account.requestFocus();
                    edt_account.setSelection(edt_account.getText().length());
                    showToast("請輸入正確的手機號");
                    return;
                }
                if (TextUtils.isEmpty(edt_psd.getText().toString().trim())){
                    edt_psd.requestFocus();
                    edt_psd.setSelection(edt_psd.getText().length());
                    showToast("請輸入密碼");
                    return;
                }

                if (NetWorkUtils.isNetConnect(getActivity())) mPresenter.phoneLogin(edt_account.getText().toString().trim(),edt_psd.getText().toString().trim());
                else showToast("请检查网络连接");
                break;
            case R.id.tv_bind://绑定手機
//                start(BingdingPhoneFragment.Companion.newInstance(isBind, phone));
                break;
        }
    }




    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
