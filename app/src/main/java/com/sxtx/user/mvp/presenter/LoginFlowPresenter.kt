package com.sxtx.user.mvp.presenter

import android.content.Context
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.alibaba.sdk.android.oss.ClientException
import com.alibaba.sdk.android.oss.ServiceException
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.model.GetObjectRequest
import com.alibaba.sdk.android.oss.model.GetObjectResult
import com.google.gson.Gson
import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.app.LongshaoAPP
import com.like.base.base.inter.IRequestListener
import com.like.base.net.BaseHttpUtil
import com.like.base.net.disposable.PageDialogDisposable
import com.like.base.net.inter.IPageListener
import com.like.base.net.transform.RxAndroid
import com.like.utilslib.UtilApp
import com.like.utilslib.app.AppUtil
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.login.Api
import com.lyh.protocol.login.LoginServer
import com.sxtx.user.BuildConfig
import com.sxtx.user.ConfigData
import com.sxtx.user.api.IAccountApi
import com.sxtx.user.api.ICommonApi
import com.sxtx.user.inter.OSSClientBuilder
import com.sxtx.user.model.bean.LoginUrlBean
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.AppUtils
import com.sxtx.user.util.DecodeLoginBeanUtils
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody

/**
 * 修改服务器地址
 */
class LoginFlowPresenter {

    var activity: Context? = null


    val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                BEGIN_LOGIN -> {
                    if (TextUtils.isEmpty(ConfigData.OTHER_PHONE)) {
                        onLogin()
                    } else {
                        phoneLogin(ConfigData.OTHER_PHONE, ConfigData.OTHER_PASSW)
                    }
                }

            }

        }
    }
    fun getLoginUrl() {
        if (BuildConfig.BASE_URL_RELEASE) {
//                if (true) {
            OSSClientBuilder.Builder(activity).asyncGetObject(OSSClientBuilder.getObjectRequest(), object : OSSCompletedCallback<GetObjectRequest, GetObjectResult> {
                override fun onSuccess(request: GetObjectRequest, result: GetObjectResult) {
                    // 请求成功。
                    val bean = Gson().fromJson(RequestUtil.Response1(result.objectContent), LoginUrlBean::class.java)
                    LongshaoAPP.init(UtilApp.getIntance().applicationContext)
                            .withApiHost(DecodeLoginBeanUtils.decode(bean.loginIp) + ":" + DecodeLoginBeanUtils.decode(bean.loginPort))
                    handler.sendEmptyMessage(BEGIN_LOGIN)
                }

                override fun onFailure(request: GetObjectRequest, clientExcepion: ClientException?, serviceException: ServiceException?) {
                    clientExcepion?.printStackTrace()
                    if (serviceException != null) {
                        Log.e("ErrorCode", serviceException.errorCode)
                        Log.e("RequestId", serviceException.requestId)
                        Log.e("HostId", serviceException.hostId)
                        Log.e("RawMessage", serviceException.rawMessage)
                    }
                }
            })
        } else {
            LongshaoAPP.init(UtilApp.getIntance().applicationContext)
                    .withApiHost(ConfigData.BASE_URL)
            handler.sendEmptyMessage(BEGIN_LOGIN)
        }
    }


    var BEGIN_LOGIN = 1


    var isFirst = true

    /**
     * 获取第一次token  用于获取IP
     */
    fun onLogin() {
        BaseHttpUtil.getIntance().clearRetrofit()
        //关于oherAPi所有的api对象都要设置为空
        APPresenter.accountApi = null
        APPresenter.accountApi = BaseHttpUtil.getIntance().createApi(IAccountApi::class.java)
        //构建请求信息
        val loginRequest = LoginServer.CommonLoginReqeust.newBuilder()
                .setChannelId(ConfigData.CHANEL_ID)
                .setDeviceId(AppUtils.getDeviceId())
                .setDeviceType("1")
                .setDeviceName(AppUtil.getDiviceName())
                .setRealIP(AppUtil.getAndroidIp())
                .setCvId(AppUtils.getCopy(UtilApp.getIntance().applicationContext))
                .setPassword(AppUtils.getP())
                .build()


        onPageNoRequestData<ResponseBody>(object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                if (isFirst) {
                    isFirst = false
                    onLogin()
                }
                LogUtil.loge(message)
            }

            override fun onSuccess(s: ResponseBody) {
                isFirst = true
                val msg = RequestUtil.Response(s.byteStream())

                var response: LoginServer.CommonLoginResponse? = null
                try {
                    if (msg != null) {
                        response = LoginServer.CommonLoginResponse.parseFrom(msg!!.body)
                    } else {
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if (!ExceptionUtils.checkStates(response, msg)) return
                if (response!!.result != null && response.result.result == 1) {
                    if (TextUtils.isEmpty(AppUtils.getDeviceId()) && !TextUtils.isEmpty(AppPreference.getIntance().passwordOne)) AppPreference.getIntance().passwordOne = response.password
                    AppPreference.getIntance().setFilter(response.openSizer == 1)
                    getIp(response.result.token)
                } else if (response != null && response.result != null && response.result.result == 2) {
                    AppPreference.getIntance().deviceId = ""
                    AppPreference.getIntance().passwordTwo = ""
                    onLogin()
                } else {
                    if (!TextUtils.isEmpty(response.result.msg)) {
                        val toast = Toast.makeText(UtilApp.getIntance().applicationContext, response.result.msg, Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.duration = Toast.LENGTH_SHORT
                        toast.show()
                    }
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.accountApi.login(RequestUtil.requestBodyCommonLoginReqeust(loginRequest))
            }
        })
    }

    /**
     * 获取服务器IP
     */
    private fun getIp(token: String) {

        //构建请求信息
        val request = LoginServer.GetLogicIpRequest.newBuilder()
                .setToken(token)
                .build()

        onPageNoRequestData<ResponseBody>(object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: LoginServer.GetLogicIpResponse? = null
                try {
                    if (msg != null) {
                        response = LoginServer.GetLogicIpResponse.parseFrom(msg!!.body)
                    } else {
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if (!ExceptionUtils.checkStates(response, msg)) return
                if (response!!.result != null && response.result.result == 1) {
                    val url = response.sListList[0].hostName + ":" + response.sListList[0].portAndPath
                    AppPreference.getIntance().token1 = response.result.token
                    LongshaoAPP.init(UtilApp.getIntance().applicationContext)
                            .withApiOhterHost(url)

                    BaseHttpUtil.getIntance().clearOtherRetrofit()
                    //关于oherAPi所有的api对象都要设置为空
                    APPresenter.commonApi = null
                    APPresenter.commonApi = BaseHttpUtil.getIntance().createOhterApi(ICommonApi::class.java)

                    vertifyUserRequest()
                } else {
                    if (!TextUtils.isEmpty(response.result.msg)) {
                        val toast = Toast.makeText(UtilApp.getIntance().applicationContext, response.result.msg, Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.duration = Toast.LENGTH_SHORT
                        toast.show()
                    }
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.accountApi.login(RequestUtil.requestBodyGetLogicIpRequest(request))
            }
        })
    }


    /**
     * 获取验证登陆  验证成功后  发送心跳
     */
    private fun vertifyUserRequest() {

        //构建请求信息
        val request = Api.VertifyUserRequest.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData<ResponseBody>(object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.VertifyUserResponse? = null
                try {
                    if (msg != null) {
                        response = Api.VertifyUserResponse.parseFrom(msg!!.body)
                    } else {
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }

                if (response != null && response.result.result == 1) {//验证成功
                    // onStartCommand(intent,flags,startId)
                } else {
                    starLogin()
                    if (!TextUtils.isEmpty(response?.result?.msg)){
                        val toast = Toast.makeText(UtilApp.getIntance().applicationContext, response!!.result.msg, Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.duration = Toast.LENGTH_SHORT
                        toast.show()
                    }
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestBodyVertifyUserRequest(request))
            }
        })
    }


    /**
     * 手机登陆请求
     */
    fun phoneLogin(phone: String, psd: String) {

        //构建请求信息
        val request = LoginServer.MobileLoginRequest.newBuilder()
                .setMobile(phone)
                .setPassword(psd)
                .setRealIp(AppUtil.getAndroidIp())
                .setChannelId(ConfigData.CHANEL_ID)
                .build()

        onPageNoRequestData<ResponseBody>(object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: LoginServer.MobileLoginResponse? = null
                try {
                    response = LoginServer.MobileLoginResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }

                if (response!!.result != null && response.result.result == 1) {
                    ConfigData.OTHER_PHONE = phone
                    ConfigData.OTHER_PASSW = psd
                    LogUtil.loge("登陸成功返回信息----->>>>$response")
                    getIp(response.result.token)
                } else {
                    if (!TextUtils.isEmpty(response?.result?.msg)){
                        val toast = Toast.makeText(UtilApp.getIntance().applicationContext, response!!.result.msg, Toast.LENGTH_SHORT)
                        toast.setGravity(Gravity.CENTER, 0, 0)
                        toast.duration = Toast.LENGTH_SHORT
                        toast.show()
                    }
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.accountApi.login(RequestUtil.requestBodyMobileLoginRequest(request))
            }
        })
    }

    fun starLogin() {
        handler.sendEmptyMessage(BEGIN_LOGIN)
        /*  val intent = Intent(UtilApp.getIntance().applicationContext, AccountActivty::class.java)
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
          intent.putExtra("isCallBack", true)
          activity?.startActivity(intent)*/
    }


    /**
     * 基于页面来请求数据
     *
     * @param isShowDialog
     * @param listener
     * @param <T>
    </T> */
    protected fun <T> onPageNoRequestData(listener: IRequestListener<T>?) {

        val disposable = PageDialogDisposable(object : IPageListener<T> {
            override fun onStart(disposable: Disposable) {
                LogUtil.loge("开始发送心跳")
            }

            override fun onNext(value: T, disposable: Disposable) {
                listener?.onSuccess(value)
            }

            override fun onError(e: Throwable, disposable: Disposable) {
                listener?.onFail(e.message)
            }
        })
        listener!!.onCreateObservable()
                .compose<Any>(RxAndroid.newIntance().io_main<Any>())
                .subscribe(disposable)
    }

}