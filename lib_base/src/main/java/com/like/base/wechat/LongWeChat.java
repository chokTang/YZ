package com.like.base.wechat;

import com.like.base.BuildConfig;
import com.like.base.wechat.callback.IWXChatLoginCallback;
import com.like.utilslib.app.ActivityUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * Created by Administrator on 2017/8/14.
 */

public class LongWeChat {
    private final IWXAPI WXAPI;
    private IWXChatLoginCallback loginCallback = null;

    private static final class Holder {
        private static final LongWeChat INTANCE = new LongWeChat();
    }

    private LongWeChat() {
        WXAPI = WXAPIFactory.createWXAPI(ActivityUtil.getAppManager().currentActivity(), BuildConfig.WX_APPID, true);
        WXAPI.registerApp(BuildConfig.WX_APPID);
    }

    public static LongWeChat getIntance() {
        return Holder.INTANCE;
    }

    public final IWXAPI getWXAPI() {
        return WXAPI;
    }

    public LongWeChat onWXLoginSuccessCallback(IWXChatLoginCallback callback) {
        this.loginCallback = callback;
        return this;
    }

    public IWXChatLoginCallback getLoginCallback() {
        return loginCallback;
    }

    public final void WXLogin() {
        final SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo";
        req.state = "random_state";
        WXAPI.sendReq(req);
    }
}
