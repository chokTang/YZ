package com.sxtx.user.mvp.presenter.main;

import com.google.protobuf.InvalidProtocolBufferException;
import com.like.base.base.inter.IRequestListener;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.login.Api;
import com.sxtx.user.model.request.RequestUtil;
import com.sxtx.user.mvp.APPresenter;
import com.sxtx.user.mvp.view.main.ITaskView;
import com.sxtx.user.util.AppPreference;
import com.sxtx.user.util.ExceptionUtils;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

/**
 * 創建日期：2019/9/23 on 20:37
 * 介紹:
 * 作者:CHOK
 */
public class TaskPresenter extends APPresenter<ITaskView> {

    public void saveQrCode(){
        final Api.SaveQrCodeReqeust reqeust = Api.SaveQrCodeReqeust.newBuilder()
                .setToken(AppPreference.getIntance().getToken1())
                .build();

        onPageNoRequestData(false, new IRequestListener<ResponseBody>() {
            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestBodySaveQrCodeRequest(reqeust));
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                BaseMessage.NetMessage msg = RequestUtil.Response(responseBody.byteStream());

                Api.SaveQrCodeResponse response = null;
                try {
                    response = Api.SaveQrCodeResponse.parseFrom(msg.getBody());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response != null && response.getResult().getResult() == 1){

                }else{
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public void onFail(String message) {

            }
        });
    }

    /**
     * 获取二维码
     */
    public void getQrCode(){
        final Api.GetQrCodeReqeust reqeust = Api.GetQrCodeReqeust.newBuilder()
                .setToken(AppPreference.getIntance().getToken1())
                .build();
        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestBodyGetQrCodeRequest(reqeust));
            }

            @Override
            public void onSuccess(ResponseBody responseBody) {
                BaseMessage.NetMessage msg = RequestUtil.Response(responseBody.byteStream());

                Api.GetQrCodeResponse response = null;
                try {
                    if (msg!=null){
                        response = Api.GetQrCodeResponse.parseFrom(msg.getBody());
                    }else {
                        return;
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response != null && response.getResult().getResult() == 1){
                    getView().getQrCodeSucc(response);
                }else{
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public void onFail(String message) {
            }
        });
    }

    public void getTask2(){
        final Api.GetTaskReqeust taskReqeust = Api.GetTaskReqeust.newBuilder()
                .setToken(AppPreference.getIntance().getToken1())
                .build();

        onPageNoRequestData(false, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetTaskResponse response = null;
                try {
                    if (msg!=null){
                        response = Api.GetTaskResponse.parseFrom(msg.getBody());
                    }else {
                        return;
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    getView().getTaskCommonSucc(response.getCommomTaskList());
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestBodyGetTaskRequest(taskReqeust));
            }
        });
    }

    public void getTask(){
        final Api.GetTaskReqeust taskReqeust = Api.GetTaskReqeust.newBuilder()
                .setToken(AppPreference.getIntance().getToken1())
                .build();

        onPageNoRequestData(false, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetTaskResponse response = null;
                try {
                    if (msg!=null){
                        response = Api.GetTaskResponse.parseFrom(msg.getBody());
                    }else {
                        return;
                    }

                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    getView().getTaskSucc(response.getInviteTaskList());
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestBodyGetTaskRequest(taskReqeust));
            }
        });
    }

    public void getTaskAward(final int taskType, long dataId){
        final Api.GetTaskAwardReqeust reqeust = Api.GetTaskAwardReqeust.newBuilder()
                .setDataId(dataId)
                .setToken(AppPreference.getIntance().getToken1())
                .build();

        onPageNoRequestData(false, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetTaskAwardResponse response = null;
                try {
                    if (msg!=null){
                        response = Api.GetTaskAwardResponse.parseFrom(msg.getBody());
                     }else {
                        return;
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if(!ExceptionUtils.checkStates(response,msg))return;
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    showFragmentToast(response.getResult().getMsg());
                    if (taskType == 1)
                        getTask();
                    else
                        getTask2();
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestBodyGetTaskAwardRequest(reqeust));
            }
        });
    }
}
