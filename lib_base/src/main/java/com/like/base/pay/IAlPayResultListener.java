package com.like.base.pay;

/**
 * 支付宝支付监听接口
 * Created by longshao on 2017/8/28.
 */

public interface IAlPayResultListener {

    void onPaySuccess();

    void onPaying();

    void onPayFail();

    void onPayCancel();

    void onPayConnectError();
}
