package com.sxtx.user.activity;

import com.like.base.base.BaseActivity;
import com.like.base.base.BaseFragment;
import com.sxtx.user.fragment.account.LoginFragment;

/**
 * 用户注册登录找回密码页面
 * Created by Administrator on 2017/10/15.
 */

public class AccountActivty extends BaseActivity {

    @Override
    public BaseFragment setRootFragment() {
        return  new LoginFragment().newInstance(getIntent().getBooleanExtra("isCallBack",false),false,"");
    }
}
