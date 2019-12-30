package com.like.base.wechat.template;

import com.like.base.wechat.BaseWXEntryActivity;
import com.like.base.wechat.LongWeChat;

/**
 * Created by longshao on 2017/8/14.
 */

public class WXEntryTemplate extends BaseWXEntryActivity {

    @Override
    protected void onLoginSuccess(String userInfo) {
        LongWeChat.getIntance().getLoginCallback().onLoginSuccess(userInfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0, 0);
    }
}
