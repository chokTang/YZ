package com.sxtx.user.fragment.account;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.like.base.base.BaseStatusToolbarFragment;
import com.like.base.base.rxjava.CommonEvent;
import com.like.base.widget.ClearEditText;
import com.like.utilslib.other.RegexUtil;
import com.sxtx.user.R;
import com.sxtx.user.mvp.presenter.account.ModifyPasswordPresenter;
import com.sxtx.user.mvp.view.account.IModifyPasswordView;
import com.sxtx.user.util.NetWorkUtils;

public class ModifyPasswordFragment extends BaseStatusToolbarFragment<ModifyPasswordPresenter> implements IModifyPasswordView, View.OnClickListener {


    TextView tv_login;


    ClearEditText edt_account;
    EditText edt_psd,edt_psd_again;



    public static ModifyPasswordFragment newInstance() {


        ModifyPasswordFragment fragment = new ModifyPasswordFragment();

        return fragment;
    }


    @Override
    public Integer getMainResId() {
        return R.layout.fragment_modify_password;
    }

    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        onBack();
        setToolbarTitle("修改密碼");

        tv_login = $(R.id.tv_login);
       edt_account = $(R.id.edt_account);

        edt_psd = $(R.id.edt_psd);
        edt_psd_again=$(R.id.edt_psd_again);

        showView(MAIN_VIEW);

        tv_login.setOnClickListener(this);


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



    @Override
    public void loginSucc() {
        showToast("修改成功");
        pop();
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
            case R.id.tv_login://賬號切換
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
                    showToast("請輸入舊密碼");
                    return;
                }
                if (TextUtils.isEmpty(edt_psd_again.getText().toString().trim())){
                    edt_psd_again.requestFocus();
                    edt_psd_again.setSelection(edt_psd_again.getText().length());
                    showToast("請輸入新密碼");
                    return;
                }
                if (NetWorkUtils.isNetConnect(getActivity())) mPresenter.modifyPassword(edt_account.getText().toString().trim(),edt_psd.getText().toString().trim(),edt_psd_again.getText().toString().trim());
                else showToast("请检查网络连接");
                break;

        }
    }





}

