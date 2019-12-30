package com.sxtx.user.mvp.presenter.account;


import com.google.protobuf.InvalidProtocolBufferException;
import com.like.base.base.inter.IRequestListener;
import com.like.utilslib.other.LogUtil;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.login.Api;
import com.sxtx.user.model.request.RequestUtil;
import com.sxtx.user.mvp.APPresenter;
import com.sxtx.user.mvp.view.account.IForgetPasswordView;
import com.sxtx.user.util.AppPreference;
import com.sxtx.user.util.ExceptionUtils;

import io.reactivex.Observable;
import okhttp3.ResponseBody;


public class ForgetPasswordPresenter extends APPresenter<IForgetPasswordView> {
    /**
     * 找回賬號
     */
    public void getMyTopAcount(final String phone, final String psd) {

        //构建请求信息
        final Api.GetMyTopAcountReqeust request = Api.GetMyTopAcountReqeust.newBuilder()
                .setToken(getToken())
                .setMobile(phone)
                .setPassword(psd)
                 .build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetMyTopAcountResponse response = null;
                try {
                    response = Api.GetMyTopAcountResponse.parseFrom(msg.getBody());
                } catch (InvalidProtocolBufferException e) {
                    showFragmentToast("找回失敗");
                    e.printStackTrace();
                }
                if (response==null){
                    return;
                }
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    LogUtil.loge("賬號找回返回信息----->>>>" + response.toString());
                    AppPreference app=AppPreference.getIntance();
                    app.setPhone(phone);
                    app.setPasswordTwo(response.getPassword());
                    app.setDeviceId(response.getDeviceId());
                    getView().loginSucc();
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return APPresenter.commonApi.request(RequestUtil.getMyTopAcount(request));
            }
        });
    }

    /**
     * 请求二维码找回密码
     */
    public void setAcountKey(String key) {

        //构建请求信息
        final Api.GetRrCodeRetrievePasswordReqeust request = Api.GetRrCodeRetrievePasswordReqeust.newBuilder()
                .setToken(getToken())
                .setKey(key)
                .build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetRrCodeRetrievePasswordResponse response = null;
                try {
                    response = Api.GetRrCodeRetrievePasswordResponse.parseFrom(msg.getBody());
                } catch (InvalidProtocolBufferException e) {
                    showFragmentToast("找回失敗");
                    e.printStackTrace();
                }
                if (response==null){
                    return;
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    getView().loginSucc();
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return APPresenter.commonApi.request(RequestUtil.setAccountKey(request));
            }
        });
    }



}
