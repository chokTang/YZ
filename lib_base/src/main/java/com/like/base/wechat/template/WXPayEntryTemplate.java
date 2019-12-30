package com.like.base.wechat.template;

import com.like.base.pay.FasePay;
import com.like.base.wechat.BaseWXActivity;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;

/**
 * Created by longshao on 2017/8/14.
 */

public class WXPayEntryTemplate extends BaseWXActivity {
    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        if (baseResp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int resultCode = baseResp.errCode;
            if (resultCode == BaseResp.ErrCode.ERR_OK) {
                FasePay.getIntance().create(this).getmIwxPayResultListener().onPaySucess(baseResp.errStr);
            } else {
                FasePay.getIntance().create(this).getmIwxPayResultListener().onPayFail(baseResp.errStr + "" + resultCode);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        finish();
        overridePendingTransition(0, 0);
    }
}
