package com.sxtx.user.mvp.presenter.account;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.GetObjectRequest;
import com.alibaba.sdk.android.oss.model.GetObjectResult;
import com.fm.openinstall.OpenInstall;
import com.google.gson.Gson;
import com.google.protobuf.InvalidProtocolBufferException;
import com.like.base.app.LongshaoAPP;
import com.like.base.base.inter.IRequestListener;
import com.like.base.net.BaseHttpUtil;
import com.like.utilslib.UtilApp;
import com.like.utilslib.app.AppUtil;
import com.like.utilslib.other.LogUtil;
import com.lyh.protocol.BaseMessage;
import com.lyh.protocol.data.PublicData;
import com.lyh.protocol.login.Api;
import com.lyh.protocol.login.LoginServer;
import com.mcxiaoke.packer.helper.PackerNg;
import com.sxtx.user.BuildConfig;
import com.sxtx.user.CdLongShaoAppaction;
import com.sxtx.user.ConfigData;
import com.sxtx.user.api.IAccountApi;
import com.sxtx.user.api.ICommonApi;
import com.sxtx.user.fragment.update.HttpRequestDialog;
import com.sxtx.user.inter.OSSClientBuilder;
import com.sxtx.user.model.bean.LoginUrlBean;
import com.sxtx.user.model.request.RequestUtil;
import com.sxtx.user.mvp.APPresenter;
import com.sxtx.user.mvp.view.account.ILoginView;
import com.sxtx.user.util.AppPreference;
import com.sxtx.user.util.AppUtils;
import com.sxtx.user.util.DecodeLoginBeanUtils;
import com.sxtx.user.util.MultiAsyncTask;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import io.reactivex.Observable;
import okhttp3.ResponseBody;

import static com.sxtx.user.ConfigData.CHANEL_ID;

/**
 * 修改服务器地址
 * Created by Administrator on 2017/12/30.
 */

public class LoginPresenter extends APPresenter<ILoginView> {
    HttpRequestDialog dialog = new HttpRequestDialog();
    private String sid = "";
    public static final int HandlerCODE = 65335;
    private boolean isCanUse = true;//本地存的服务器域名是否能用，默认能用
    private static final int NOT_CAN_USE = 665;//本地存的服务器域名不能用的时候 发消息
    private static final int OSS = 6656;//OSS上哪数据
    ExecutorService singleExecutorService = Executors.newCachedThreadPool(new ThreadFactory() {
        AtomicInteger integer = new AtomicInteger();

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "RoomDataThread:" + integer.getAndIncrement());
        }
    });

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerCODE:
                    if (TextUtils.isEmpty(ConfigData.OTHER_PHONE)) {
                        onLogin(context);
                    } else {
                        phoneLogin(ConfigData.OTHER_PHONE, ConfigData.OTHER_PASSW);
                    }
                    break;
                case NOT_CAN_USE:
                    getLoginUrl(false, sid);
                    break;
                case OSS:
                    singleExecutorService.execute(new Runnable() {
                        @Override
                        public void run() {
                            getOssData();
                        }
                    });
                    break;
            }
        }
    };


    public void getLoginUrl(final Boolean isShowDialog, String ssid) {
        sid = ssid;
        if (isShowDialog) {
            if (dialog != null && fragment != null) {
                assert fragment.getFragmentManager() != null;
                dialog.show(fragment.getFragmentManager(), "show_request");
            }
        }
        if (BuildConfig.BASE_URL_RELEASE) {  //
            if (!TextUtils.isEmpty(AppPreference.getIntance().getOSS1Url())) {//本地存的登陆域名不为空就用本地的
                if (isCanUse) {//本地的能用就用本地的
                    LoginUrlBean bean = new Gson().fromJson(AppPreference.getIntance().getOSS1Url(), LoginUrlBean.class);
                    LongshaoAPP.init(UtilApp.getIntance().getApplicationContext())
                            .withApiHost(DecodeLoginBeanUtils.decode(bean.getLoginIp()) + ":" + DecodeLoginBeanUtils.decode(bean.getLoginPort()));
                    AppPreference.getIntance().setOSSUrl(DecodeLoginBeanUtils.decode(bean.getCrashUrl()));
                    handler.sendEmptyMessage(HandlerCODE);
                } else {//不能用  不能连接就又在OSS上啦 并且重新赋值本地的域名
                    handler.sendEmptyMessage(OSS);
                }
            } else {
                handler.sendEmptyMessage(OSS);
            }
//            handler.sendEmptyMessage(OSS);
        } else {
            LongshaoAPP.init(UtilApp.getIntance().getApplicationContext())
                    .withApiHost(ConfigData.BASE_URL);
            handler.sendEmptyMessage(HandlerCODE);
        }

    }

    /**
     * 从OSS上获取数据
     */
    private void getOssData() {
        LogUtil.loge("---->>>999");
        OSSClientBuilder.Builder(context).asyncGetObject(OSSClientBuilder.getObjectRequest(), new OSSCompletedCallback<GetObjectRequest, GetObjectResult>() {
            @Override
            public void onSuccess(GetObjectRequest request, GetObjectResult result) {
                LoginUrlBean bean = new Gson().fromJson(RequestUtil.Response1(result.getObjectContent()), LoginUrlBean.class);
                AppPreference.getIntance().setOSS1Url(new Gson().toJson(bean));
                LongshaoAPP.init(UtilApp.getIntance().getApplicationContext())
                        .withApiHost(DecodeLoginBeanUtils.decode(bean.getLoginIp()) + ":" + DecodeLoginBeanUtils.decode(bean.getLoginPort()));
                AppPreference.getIntance().setOSSUrl(DecodeLoginBeanUtils.decode(bean.getCrashUrl()));
                handler.sendEmptyMessage(HandlerCODE);
            }

            @Override
            public void onFailure(GetObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                if (clientExcepion != null) {
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    Log.e("ErrorCode", serviceException.getErrorCode());
                    Log.e("RequestId", serviceException.getRequestId());
                    Log.e("HostId", serviceException.getHostId());
                    Log.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });
    }


    /**
     * 获取第一次token  用于获取IP
     */
    public void onLogin(final Context context) {
        BaseHttpUtil.getIntance().clearRetrofit();
        //关于oherAPi所有的api对象都要设置为空
        accountApi = null;
        accountApi = BaseHttpUtil.getIntance().createApi(IAccountApi.class);
        AppPreference.getIntance().setHeart(false);
        //构建请求信息
        String cvid = "";
        if (!TextUtils.isEmpty(PackerNg.getChannel(context))) {
            cvid = "0,"+ PackerNg.getChannel(context);
//            showFragmentToast("当前的渠道信息" + PackerNg.getChannel(context));
        } else {
//            showFragmentToast("当前的渠道信息为空");
            if (TextUtils.isEmpty(sid)) {
                cvid = AppUtils.getCopy(context);
            } else {
                cvid = sid;
            }
        }

        final LoginServer.CommonLoginReqeust loginRequest = LoginServer.CommonLoginReqeust.newBuilder()
                .setChannelId(CHANEL_ID)
                .setDeviceId(AppUtils.getDeviceId())
                .setDeviceType("1")
                .setDeviceName(AppUtil.getDiviceName())
                .setRealIP(AppUtil.getAndroidIp())
                .setCvId(cvid)
                .setPassword(AppUtils.getP())
                .build();


        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {
                LogUtil.loge(message);
                isCanUse = false;
                handler.sendEmptyMessage(NOT_CAN_USE);
            }

            @Override
            public void onSuccess(ResponseBody s) {
                isCanUse = true;
                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                LoginServer.CommonLoginResponse response = null;
                try {
                    if (msg != null) {
                        response = LoginServer.CommonLoginResponse.parseFrom(msg.getBody());
                    } else {
                        return;
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    if (TextUtils.isEmpty(AppUtils.getDeviceId()) && !TextUtils.isEmpty(AppPreference.getIntance().getPasswordOne()))
                        AppPreference.getIntance().setPasswordOne(response.getPassword());
                    AppPreference.getIntance().setFilter(response.getOpenSizer() == 1);
                    getIp(response.getResult().getToken());
                    //用户注册成功后调用openinstall
                    OpenInstall.reportRegister();
                    OpenInstall.reportEffectPoint("key", 0);
                } else if (response.getResult() != null && response.getResult().getResult() == 2) {
                    AppPreference.getIntance().setDeviceId("");
                    AppPreference.getIntance().setPasswordTwo("");
                    onLogin(context);
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return accountApi.login(RequestUtil.requestBodyCommonLoginReqeust(loginRequest));
            }
        });
    }

    public void onLoadingImage() {
        final Api.GetAdvertisementLoadingImgReqeust reqeust = Api.GetAdvertisementLoadingImgReqeust.newBuilder().build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {
                LogUtil.loge(message);
            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.GetAdvertisementLoadingImgResponse response = null;
                try {
                    if (msg != null) {
                        response = Api.GetAdvertisementLoadingImgResponse.parseFrom(msg.getBody());
                    } else {
                        return;
                    }

                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                getView().getDataSucc(response.getAppBannerList());
            }

            @Override
            public Observable onCreateObservable() {
                return accountApi.login(RequestUtil.requestBodyLauncherImageRequest(reqeust));
            }
        });
    }

    /**
     * 获取服务器IP
     */
    private void getIp(String token) {

        //构建请求信息
        final LoginServer.GetLogicIpRequest request = LoginServer.GetLogicIpRequest.newBuilder()
                .setToken(token)
                .build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                LoginServer.GetLogicIpResponse response = null;
                try {
                    if (msg != null) {
                        response = LoginServer.GetLogicIpResponse.parseFrom(msg.getBody());
                    } else {
                        return;
                    }
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    AppPreference.getIntance().setToken1(response.getResult().getToken());
                    if (response.getSListList().size() == 1) {//只有一个地址的时候不ping直接用
                        LongshaoAPP.init(UtilApp.getIntance().getApplicationContext())
                                .withApiOhterHost(response.getSListList().get(0).getHostName() + ":" + response.getSListList().get(0).getPortAndPath());

                        BaseHttpUtil.getIntance().clearOtherRetrofit();
                        //关于oherAPi所有的api对象都要设置为空
                        commonApi = null;
                        commonApi = BaseHttpUtil.getIntance().createOhterApi(ICommonApi.class);
                        if (dialog != null && fragment != null) {
                            dialog.dismiss();
                        }
                        vertifyUserRequest();
                    } else {//在集合中拿到最小值 然后根据最小值来拿到地址
                        checkSpeedList(response.getSListList());
                    }
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return accountApi.login(RequestUtil.requestBodyGetLogicIpRequest(request));
            }
        });
    }

    List<Long> time = new ArrayList<>();
    Map<Long, String> map = new HashMap<>();
    MultiAsyncTask task;

    /**
     * 多个链接测速比较
     */
    private void checkSpeedList(List<PublicData.ServerListDataProto> list) {

        final List<String> urlList = new ArrayList<>();
        task = new MultiAsyncTask(MultiAsyncTask.InvokeMode.ASYNC, new MultiAsyncTask.OnTasksFinishListener() {
            @Override
            public void onFinish() {
                if (dialog != null && fragment != null) {
                    dialog.dismiss();
                }
                //将时间装在集合中去  然后根据最小值来拿到地址
                time.addAll(map.keySet());

                if (!map.isEmpty()) {
                    //在集合中拿到最小值 然后根据最小值来拿到地址
                    LongshaoAPP.init(UtilApp.getIntance().getApplicationContext())
                            .withApiOhterHost(map.get(Collections.min(time)));

                    BaseHttpUtil.getIntance().clearOtherRetrofit();
                    //关于oherAPi所有的api对象都要设置为空
                    commonApi = null;
                    commonApi = BaseHttpUtil.getIntance().createOhterApi(ICommonApi.class);
                    task.stop();
                    task = null;
                    vertifyUserRequest();
                }

            }

            @Override
            public void onTimeOut() {
                if (dialog != null) {
                    dialog.dismiss();
                }
                task.stop();
                LogUtil.loge("------------》》》超时");
            }
        });

        for (int i = 0; i < list.size(); i++) {
            String url = list.get(i).getHostName() + ":" + list.get(i).getPortAndPath();
            urlList.add(url);
            final int finalI = i;

            task.addTask(new MultiAsyncTask.AsyncTask() {
                @Override
                public boolean taskRun() {
                    return pingServer(urlList.get(finalI), 10000);
                }
            });
        }
        task.execute();
    }

    public void stopTask() {
        if (task != null) task.stop();
    }

    /**
     * 这个是专门哪来测速的一个请求
     */
    public boolean pingServer(String server, int timeout) {
        long startTime = System.currentTimeMillis();
        boolean isConnected = false;
        URL url;
        try {
            url = new URL(server);
            URLConnection co = url.openConnection();
            co.setConnectTimeout(timeout);
            co.connect();
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            isConnected = true;
            //这里加个I 是预防
            map.put(duration, server);
            task.onTaskFinish();
            return true;
        } catch (Exception e1) {
            isConnected = false;
            url = null;
            task.onTaskFinish();
            return false;
        }
    }

    /**
     * 手机登陆请求
     */
    public void phoneLogin(final String phone, final String psd) {
        dialog.show(fragment.getFragmentManager(), "show_request");
        //构建请求信息
        final LoginServer.MobileLoginRequest request = LoginServer.MobileLoginRequest.newBuilder()
                .setMobile(phone)
                .setPassword(psd)
                .setRealIp(AppUtil.getAndroidIp())
                .setChannelId(CHANEL_ID)
                .build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {
                isCanUse = false;
                handler.sendEmptyMessage(NOT_CAN_USE);
            }

            @Override
            public void onSuccess(ResponseBody s) {
                isCanUse = true;
                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                LoginServer.MobileLoginResponse response = null;
                try {
                    response = LoginServer.MobileLoginResponse.parseFrom(msg.getBody());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if (response.getResult() != null && response.getResult().getResult() == 1) {
                    ConfigData.OTHER_PHONE = phone;
                    ConfigData.OTHER_PASSW = psd;
                    LogUtil.loge("登陸成功返回信息----->>>>" + response.toString());
                    getIp(response.getResult().getToken());
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return accountApi.login(RequestUtil.requestBodyMobileLoginRequest(request));
            }
        });
    }


    /**
     * 获取验证登陆  验证成功后  发送心跳
     */
    private void vertifyUserRequest() {

        //构建请求信息
        final Api.VertifyUserRequest request = Api.VertifyUserRequest.newBuilder()
                .setToken(AppPreference.getIntance().getToken1())
                .build();

        onPageNoRequestData(true, new IRequestListener<ResponseBody>() {
            @Override
            public void onFail(String message) {

            }

            @Override
            public void onSuccess(ResponseBody s) {

                BaseMessage.NetMessage msg = RequestUtil.Response(s.byteStream());

                Api.VertifyUserResponse response = null;
                try {
                    response = Api.VertifyUserResponse.parseFrom(msg.getBody());
                } catch (InvalidProtocolBufferException e) {
                    e.printStackTrace();
                }
                if (response != null && response.getResult().getResult() == 1) {//验证成功
                    getView().sendHeart();
                } else {
                    showFragmentToast(response.getResult().getMsg());
                }
            }

            @Override
            public Observable onCreateObservable() {
                return commonApi.request(RequestUtil.requestBodyVertifyUserRequest(request));
            }
        });
    }


}
