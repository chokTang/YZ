package com.sxtx.user.service


import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.IBinder
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import com.google.gson.Gson
import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.like.base.net.disposable.PageDialogDisposable
import com.like.base.net.inter.IPageListener
import com.like.base.net.transform.RxAndroid
import com.like.utilslib.UtilApp
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.cmd.Command
import com.lyh.protocol.login.Api
import com.sxtx.user.activity.AccountActivty
import com.sxtx.user.activity.AllCheckActivity
import com.sxtx.user.dbdata.ResultInfo
import com.sxtx.user.model.encryption.Aes256Utils
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.presenter.LoginFlowPresenter
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.CrashHandlerUtils
import event.HeartServiceEvents
import event.HeartServiceEvents.TO_SYSTEM_OUT
import event.HeartServiceEvents.TO_UPLOAD_CRASH
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.IOException
import event.HeartServiceEvents.TO_START_LOGIN as TO_START_LOGIN1


/**
 *
 * 介紹: 修改服务器地址
 * 作者:CHOK
 */
class HeartService : Service() {

    val TAG = "HeartService"
     var presenter: LoginFlowPresenter? = null

    private val handler = object : Handler() {}

    private var runnable: Runnable? = null

    var SYSTEM_EXIT: Boolean = false

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate")
        presenter = LoginFlowPresenter()
        presenter!!.activity= applicationContext
        val mFilter = IntentFilter()
        // 屏幕灭屏广播
        mFilter.addAction(Intent.ACTION_SCREEN_OFF)
        // 屏幕亮屏广播
        mFilter.addAction(Intent.ACTION_SCREEN_ON)
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION)
        EventBus.getDefault().register(this)
        registerReceiver(mReceiver, mFilter)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(TAG, "onStartCommand")
        runnable = object : Runnable {
            override fun run() {
                //do something
               /*if(SYSTEM_EXIT)*/ sendHeart(AppPreference.getIntance().token1)
                handler.postDelayed(this, 60000)
            }
        }
        handler.postDelayed(runnable, 1000) // 开始Timer
        return super.onStartCommand(intent, flags, startId)

    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable) //停止Timergit
        Log.d(TAG, "onDestroy")
        unregisterReceiver(mReceiver)
        EventBus.getDefault().unregister(this)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }


    private val CLICK_TIME_DELAY = 6000 // ms
    private var lastClickTime = 0L

    protected fun isClickTooFast(): Boolean {
        var isClickTooFast = false
        if (SystemClock.elapsedRealtime() - lastClickTime < CLICK_TIME_DELAY) {
            isClickTooFast = true
        }
        lastClickTime = SystemClock.elapsedRealtime()
        return isClickTooFast
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun eventBusHandle(message: HeartServiceEvents) {
        when (message.getEventId()) {
            TO_START_LOGIN1 -> {
                handler.postDelayed(Runnable {
                    if (!isClickTooFast())
                        sendHeart(AppPreference.getIntance().token1)
                }, 60000)
            }
            TO_SYSTEM_OUT ->{

            }
            TO_UPLOAD_CRASH -> {
                //1.初始化OKHttp
                val okHttpClient = OkHttpClient()
                        .newBuilder()
                        .build()

                val request = message.data as Request
                okHttpClient.newCall(request).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        LogUtil.loge("----->Crash上传失败\n" + e.toString())
                      //  RestartAPPTool.restartAPP(UtilApp.getIntance().getApplicationContext(), 100)
                    }

                    override fun onResponse(call: Call, response: Response) {

                        if (response.isSuccessful()) {
                            var str = RequestUtil.Response1(response.body()!!.byteStream())
                            var info = Aes256Utils.decrypt(str,CrashHandlerUtils.AES_KEY,CrashHandlerUtils.AES_IV)
                            var result = Gson().fromJson(info,ResultInfo::class.java)
                            if (result.status == 1){
                                LogUtil.loge("----->Crash上传成功")
                            }else{
                                LogUtil.loge("----->Crash上传失败")
                            }
                            //RestartAPPTool.restartAPP(UtilApp.getIntance().getApplicationContext(), 100)
                        }
                    }
                })
            }

        }
    }

    /**
     * 发送心跳
     */
    fun sendHeart(token: String) {

        //构建请求信息
        val request = Api.HeartRequest.newBuilder()
                .setToken(token)
                .build()

        onPageNoRequestData<ResponseBody>(object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                LogUtil.loge("心跳发送失败$message")
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                if (msg == null) {
                    LogUtil.loge("心跳数据为空----->>>")
                   // if(SYSTEM_EXIT)presenter?.getLoginUrl()
                   return
                }
                if (msg.command == Command.CmdType.S_C_BREAK_LINE_RESPONSE.number) {
                    EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.TO_BREAK_LINE_RESPONSE, null))
                    return
                }
                var response: Api.HeartResponse? = null
                try {
                    response = Api.HeartResponse.parseFrom(msg.body)
                } catch (e: InvalidProtocolBufferException) {

                     LogUtil.loge(e.toString());
                    return
                }

                if (response != null && response.result != null && response.result.result == 1) {//验证成功
                    LogUtil.loge("心跳发送成功" + response.result.token)
                    AppPreference.getIntance().heart = true
                    AppPreference.getIntance().token1 = response.result.token
                } else {
                    AppPreference.getIntance().heart = false
                    presenter?.getLoginUrl()
                    if (response != null) LogUtil.loge(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestBodyHeartRequest(request))
            }
        })
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
                if (!TextUtils.isEmpty(e.message)){
                    listener?.onFail(e.message)
                }
            }
        })
        listener!!.onCreateObservable()
                .compose<Any>(RxAndroid.newIntance().io_main<Any>())
                .subscribe(disposable)
    }

    private var connectivityManager: ConnectivityManager? = null
    private var info: NetworkInfo? = null

    /**
     * 接受到网络重连的广播时重新执行startcommoned方法让client重连
     */
    val mReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == ConnectivityManager.CONNECTIVITY_ACTION) {
                connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                info = connectivityManager!!.activeNetworkInfo
                if (info != null && info!!.isAvailable) {
                    EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.TO_START_LOGIN, null))
                } else {
//                    starLogin()
                    toAllCheck()
                }
            } else if (Intent.ACTION_SCREEN_ON == action) run {
                EventBus.getDefault().post(HeartServiceEvents(HeartServiceEvents.TO_GESTURE_LOGIN, null))
                Log.e(TAG, "screen on")
            } else if (Intent.ACTION_SCREEN_OFF == action) run {
                Log.e(TAG, "screen off")
            }

        }
    }

    fun starLogin() {
        val intent = Intent(UtilApp.getIntance().applicationContext, AccountActivty::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("isCallBack", true)
        startActivity(intent)
    }


    fun toAllCheck() {
        val intent = Intent(UtilApp.getIntance().applicationContext, AllCheckActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("isCallBack", true)
        startActivity(intent)
    }




}
