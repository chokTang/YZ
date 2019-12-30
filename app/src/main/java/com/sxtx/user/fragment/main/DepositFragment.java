package com.sxtx.user.fragment.main;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.gyf.immersionbar.ImmersionBar;
import com.like.base.base.BaseFragment;
import com.lyh.protocol.login.Api;
import com.sxtx.user.R;
import com.sxtx.user.fragment.deposit.DepositType1Fragment;
import com.sxtx.user.mvp.presenter.main.DepositPresenter;
import com.sxtx.user.mvp.view.main.IDepositView;

import org.jetbrains.annotations.NotNull;

/**
 * 創建日期：2019/9/23 on 20:35
 * 介紹:
 * 作者:CHOK
 */
public class DepositFragment extends BaseFragment<DepositPresenter> implements IDepositView, View.OnClickListener {

    TextView textView1, textView2;
    private BaseFragment parentFragment;
    public boolean isRequest = false;
    View title;

    public static final String KEY_ISREQUEST = "key_isrequesrt";

    public static  DepositFragment newInstance(boolean isRequest) {
        Bundle args = new Bundle();
        DepositFragment fragment = new DepositFragment();
        args.putBoolean(KEY_ISREQUEST,isRequest);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public Object getResId() {
        return R.layout.fragment_deposit;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImmersionBar.with(this).transparentStatusBar().init();
    }


    @Override
    public void onInitView(@Nullable Bundle savedInstanceState) {
        super.onInitView(savedInstanceState);
        Bundle bundle = getArguments();
        isRequest = bundle.getBoolean(KEY_ISREQUEST,false);

        parentFragment = (BaseFragment) getParentFragment();
        textView1 = $(R.id.tv_type1);
        title = $(R.id.title);
        ImmersionBar.setTitleBar(getActivity(),ImmersionBar.getStatusBarHeight(getActivity()),title);
        textView2 = $(R.id.tv_type2);

        textView1.setOnClickListener(this);
        textView2.setOnClickListener(this);
    }

    @Override
    public void onInitData(@Nullable Bundle savedInstanceState) {
        super.onInitData(savedInstanceState);
    }


    @Override
    public void onSupportVisible() {
        if (isRequest) {
            mPresenter.getPayDataResponse();
        }
    }


    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_type1:
                break;
            case R.id.tv_type2:
                break;
        }
    }

    @Override
    public void getPayData(@NotNull Api.GetPayDataResponse respons) {
        isRequest = true;
        switch (respons.getTopYpType()) {//1-选普通 2-卡密
            case 1:
                parentFragment.start(DepositType1Fragment.Companion.newIncetance(1));
                isRequest = false;
                break;
            case 2:
                parentFragment.start(DepositType1Fragment.Companion.newIncetance(2));
                isRequest = false;
                break;
        }
    }
}
