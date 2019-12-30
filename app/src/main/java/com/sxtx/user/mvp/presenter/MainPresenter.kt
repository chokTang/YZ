package com.sxtx.user.mvp.presenter


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import android.support.v4.app.ActivityCompat

import android.util.Log
import com.google.protobuf.InvalidProtocolBufferException

import com.king.app.updater.callback.UpdateCallback
import com.like.base.base.inter.IRequestListener
import com.like.utilslib.app.ActivityUtil

import com.lyh.protocol.login.Api
import com.sxtx.user.CdLongShaoAppaction
import com.sxtx.user.dialog.SettingJurisdictionDialog

import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.IMainView
import com.sxtx.user.util.ExceptionUtils
import com.sxtx.user.util.UpdateManager
import io.reactivex.Observable
import okhttp3.ResponseBody
import java.io.File

class MainPresenter : APPresenter<IMainView>(){


    /**
     * 獲取充值信息
     */
    fun getPayDataResponse() {

        //构建请求信息
        val request = Api.GetPayDataReqeust.newBuilder()
                .setToken(token)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetPayDataResponse? = null
                try {
                    response = Api.GetPayDataResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {
                    view.getPayData(response)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetPayDataReqeust(request))
            }
        })
    }


    /**
     * 获取版本號
     */
    fun GetAppVersion() {
        //构建请求信息
        val loginRequest = Api.GetAppVersionReqeust.newBuilder()
                .setToken(token)
                .build()


        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetAppVersionResponse? = null
                try {
                    if(msg!=null){
                        response = Api.GetAppVersionResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }

                if (response!!.result != null && response.result.result == 1) {
                   view.getAppVersionSucceed(response.androinVersion,response.androinAddress,response.content,response.title)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetAppVersionReqeust(loginRequest))
            }
        })
    }


    fun download(url: String) {
        UpdateManager.getInstance().download(CdLongShaoAppaction.context, url, object : UpdateCallback {
            override fun onDownloading(isDownloading: Boolean) {
                Log.d("SplashPresenter", "is downloading:$isDownloading")
            }

            override fun onStart(url: String) {
                view.showDownloadProgressDialog()
                Log.e("SplashPresenter", "download start from $url")
            }

            override fun onProgress(progress: Int, total: Int, isChange: Boolean) {
                view.updateDownloadProgress(progress, total)
                if (isChange) {
                    Log.d("SplashPresenter", "download progress:$progress/$total")
                }
            }

            override fun onFinish(file: File) {
                (context as Activity).finish()
               ActivityUtil.getAppManager().finishAllActivity()
            }

            override fun onError(e: Exception) {
                e.printStackTrace()
                view.showDownloadErrorDialog()
            }

            override fun onCancel() {}
        })
    }


    /**
     * 需要申请的存储权限
     */
    private val STORAGE_PERMISSIONS = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)

    /**
     * 检测是否有权限
     */
    public fun checkPermission(context: Context): Boolean {
        val readResult = ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        val writeResult = ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return if (readResult != PackageManager.PERMISSION_GRANTED || writeResult != PackageManager.PERMISSION_GRANTED) {
            false
        } else true

    }


    /**
     * 申请权限
     */
    public fun requestPermission(context: Activity) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.READ_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            SettingJurisdictionDialog().setContent("必須要存儲權限才能下載更新包").setOnCallBackListener({ button ->
                if (button === 2) { // 去设置
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    intent.data = Uri.fromParts("package", context.getPackageName(), null)
                    context.startActivity(intent)
                }
            }).show(context.getFragmentManager(), "request permission")
        } else {
            ActivityCompat.requestPermissions(context, STORAGE_PERMISSIONS, 1)
        }
    }




}