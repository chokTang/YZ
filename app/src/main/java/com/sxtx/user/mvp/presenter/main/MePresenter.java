package com.sxtx.user.mvp.presenter.main;

import com.google.protobuf.InvalidProtocolBufferException;
import com.like.base.base.inter.IRequestListener;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.login.Api;
import com.sxtx.user.model.request.RequestUtil;
import com.sxtx.user.mvp.APPresenter;
import com.sxtx.user.mvp.view.main.IMeView;
import com.sxtx.user.util.AppPreference;
import com.sxtx.user.util.ExceptionUtils;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 創建日期：2019/9/23 on 20:39
 * 介紹:
 * 作者:CHOK
 */
public class MePresenter extends APPresenter<IMeView> {
    /**
     * 獲取充值信息
     */
    public void getPayDataResponse() {
        final Api.GetPayDataReqeust reqeust = Api.GetPayDataReqeust.newBuilder()
                .setToken(AppPreference.getIntance().getToken1())
                .build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestGetPayDataReqeust(reqeust));
            }
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetPayDataResponse response = null;
                try {
                    if(msg!=null){
                        response = Api.GetPayDataResponse.parseFrom(msg.getBody());
                    }else {
                        return;
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    getView().getPayData(response);
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }
        });
    }
    /**
     * 点击广告
     */
    public void clickAd(long id){
        final Api.ClickAdReqeust reqeust = Api.ClickAdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().getToken1())
                .setId(id)
                .build();
        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestBodyClickAdRequest(reqeust));
            }
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.ClickAdResponse response = null;
                try {
                    if(msg!=null){
                        response = Api.ClickAdResponse.parseFrom(msg.getBody());
                    }else {
                        return;
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response.getResult() != null && response.getResult().getResult() == 1) {

                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }
        });
    }

    /**
     * 获取用户信息
     */
   public void getUserInfo(){
       final Api.GetApiUserInfoReqeust reqeust = Api.GetApiUserInfoReqeust.newBuilder()
               .setToken(AppPreference.getIntance().getToken1())
               .build();

       onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
           @Override
           public Observable onCreateObservable() {
               return commonApi.request(RequestUtil.requestBodySaveApiUserInfoRequest(reqeust));
           }

           @Override
           public void onSuccess(ResponseBody responseBody) {
               BaseMessage.NetMessage msg = RequestUtil.Response(responseBody.byteStream());

               Api.GetApiUserInfoResponse response = null;
               try {
                   response = Api.GetApiUserInfoResponse.parseFrom(msg.getBody());
               } catch (InvalidProtocolBufferException e) {
                   e.printStackTrace();
               }
               if(!ExceptionUtils.checkStates(response,msg))return;
               if (response != null && response.getResult().getResult() == 1){
                    getView().getUserInfoSucc(response.getInfo());
                    getView().getBannerSucc(response.getInfoBannerList());
               }else{
                   showFragmentToast(response.getResult().getMsg());
               }
           }

           @Override
           public void onFail(String message) {

           }
       });
   }
}
