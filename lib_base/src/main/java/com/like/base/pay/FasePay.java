package com.like.base.pay;

import android.app.Activity;
import android.os.AsyncTask;

import com.like.base.BuildConfig;
import com.like.base.base.BaseFragment;
import com.like.base.wechat.LongWeChat;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;

/**
 * 快速支付，集成了支付宝和微信支付
 * <p>
 * Created by longshao on 2017/8/28.
 */

public class FasePay {
    private Activity mActivity;
    private IAlPayResultListener mIAlPayResultListener = null;
    private IWXPayResultListener mIwxPayResultListener = null;

    public static class Holder {
        private static final FasePay INTANCE = new FasePay();
    }

    public static FasePay getIntance() {
        return Holder.INTANCE;
    }

    /**
     * 生成实例
     *
     * @param fragment
     * @return
     */
    public FasePay create(BaseFragment fragment) {
        this.mActivity = fragment.getActivity();
        return this;
    }

    /**
     * 生成实例
     *
     * @param activity
     * @return
     */
    public FasePay create(Activity activity) {
        this.mActivity = activity;
        return this;
    }

    /**
     * 设置支付宝回调监听
     *
     * @param listener
     * @return
     */
    public FasePay setPayReultListener(IAlPayResultListener listener) {
        this.mIAlPayResultListener = listener;
        return this;
    }

    /**
     * 设置微信回调
     *
     * @param listener
     * @return
     */
    public FasePay setWXPayReultListener(IWXPayResultListener listener) {
        this.mIwxPayResultListener = listener;
        return this;
    }

    /**
     * 调起支付宝支付
     *
     * @param paySign 由服务器生成了订单的参数
     */
    public void alPay(String paySign) {
        final PayAsyncTask payAsyncTask = new PayAsyncTask(mActivity, mIAlPayResultListener);
        payAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, paySign);
    }

    /**
     * 调起微信支付
     *
     * @param model
     */
    public void weChatPay(WXPayReq model) {
        final IWXAPI iwxapi = LongWeChat.getIntance().getWXAPI();
        final PayReq payReq = new PayReq();
        payReq.appId = BuildConfig.WX_APPID;
        payReq.prepayId = model.getPrepayId();
        payReq.partnerId = model.getPartnerId();
        payReq.packageValue = model.getPackageValue();
        payReq.timeStamp = model.getTimestamp();
        payReq.nonceStr = model.getNonceStr();
        payReq.sign = model.getPaySign();

        iwxapi.sendReq(payReq);
    }

    public IWXPayResultListener getmIwxPayResultListener() {
        return mIwxPayResultListener;
    }
}
