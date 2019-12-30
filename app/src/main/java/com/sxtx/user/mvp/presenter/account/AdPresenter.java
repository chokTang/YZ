package com.sxtx.user.mvp.presenter.account;

import com.google.protobuf.InvalidProtocolBufferException;
import com.like.base.base.inter.IRequestListener;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.login.Api;
import com.sxtx.user.model.request.RequestUtil;
import com.sxtx.user.mvp.APPresenter;
import com.sxtx.user.mvp.view.main.IAdView;
import com.sxtx.user.util.AppPreference;
import com.sxtx.user.util.ExceptionUtils;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * Created by Administrator on 2017/12/30.
 */

public class AdPresenter extends APPresenter<IAdView> {

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

}
