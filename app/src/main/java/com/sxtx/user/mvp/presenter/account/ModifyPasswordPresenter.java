package com.sxtx.user.mvp.presenter.account;

import com.google.protobuf.InvalidProtocolBufferException;
import com.like.base.base.inter.IRequestListener;
import com.like.utilslib.other.LogUtil;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.login.Api;
import com.sxtx.user.model.request.RequestUtil;
import com.sxtx.user.mvp.APPresenter;
import com.sxtx.user.mvp.view.account.IModifyPasswordView;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

public class ModifyPasswordPresenter extends APPresenter<IModifyPasswordView> {






    /**
     * 修改密碼
     */
    public void modifyPassword(final String phone, final String oldPsd,String newPas) {

        //构建请求信息
        final Api.ModifyPasswordReqeust request = Api.ModifyPasswordReqeust.newBuilder()
                .setToken(getToken())
                .setMobile(phone)
                .setPassword(oldPsd)
                .setNewPassword(newPas)
                .build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetModifyPasswordResponse response = null;
                try {
                    response = Api.GetModifyPasswordResponse.parseFrom(msg.getBody());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    LogUtil.loge("修改密碼返回信息----->>>>" + response.toString());
                    getView().loginSucc();
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return APPresenter.commonApi.request(RequestUtil.modifyPassword(request));
            }
        });
    }





}
