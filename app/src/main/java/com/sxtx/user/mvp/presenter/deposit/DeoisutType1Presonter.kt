package com.sxtx.user.mvp.presenter.deposit

import android.text.TextUtils
import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.IDeoisutType1View
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import com.sxtx.user.util.ImageUrlRequestUtil
import io.reactivex.Observable
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody


/**
 *
 * 介紹:
 * 作者:CHOK
 */

class DeoisutType1Presonter : APPresenter<IDeoisutType1View>() {

    /**
     * 獲取充值信息
     */
    fun getPayDataResponse() {

        //构建请求信息
        val request = Api.GetPayDataReqeust.newBuilder()
                .setToken(token)
                .setCount(1)
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
                ExceptionUtils.checkStates(response, msg)
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
     * 获取直冲 相信信息
     * type  1 是获取直冲二维码，2是获取直冲回调
     */
    fun getPayUrl(url: String,type:Int,orderNo:String) {
        if (TextUtils.isEmpty(url)){
            return
        }
        if (type==1){
            showDialog()
        }
        //1.初始化OKHttp
        val okHttpClient = OkHttpClient()
                .newBuilder()
                .build()
        //2.构建request
        val builder = FormBody.Builder()
        if (type==2){
            builder.add("orderNo",orderNo)
        }
        val request = Request.Builder()
                .url(url)
                .post(builder.build())
                .build()

        ImageUrlRequestUtil.singleExecutorService.execute {
            //待会儿在这里实现OkHttp请求
            //3.发送请求（这里并没有执行，只是构建了`RealCall`对象）
            try {
                val call = okHttpClient.newCall(request)
                //4.接收响应（请求执行，并得到响应结果）
                val execute = call.execute()
                hideDialog()
                if (type==1){
                    view.getPayUrl(RequestUtil.Response1(execute.body()!!.byteStream()))
                }else{
                    view.getPayFinish(RequestUtil.Response1(execute.body()!!.byteStream()))
                }
            } catch (e: Exception) {
                LogUtil.loge("错误$e")
                hideDialog()
                fragment.baseActivity.runOnUiThread {
                    showFragmentToast(e.message)
                }
            }

        }

    }


    /**
     * 请求兑换卡密
     */
    fun exchageKaMiReqeust(kami: String) {

        //构建请求信息
        val request = Api.ExchageKaMiReqeust.newBuilder()
                .setToken(token)
                .setKaMi(kami)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.ExchageKaMiResponse? = null
                try {
                    response = Api.ExchageKaMiResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                ExceptionUtils.checkStates(response, msg)
                if (response!!.result != null && response.result.result == 1) {
                    view.getSuccess(response)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestExchageKaMiReqeust(request))
            }
        })
    }


    /**
     * 获取用户信息  這裡主要獲取客服URL
     */
    fun getUserInfo() {
        val reqeust = Api.GetApiUserInfoReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestBodySaveApiUserInfoRequest(reqeust))
            }

            override fun onSuccess(responseBody: ResponseBody) {
                val msg = RequestUtil.Response(responseBody.byteStream())

                var response: Api.GetApiUserInfoResponse? = null
                try {
                    response = Api.GetApiUserInfoResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }

                ExceptionUtils.checkStates(response, msg)
                if (response != null && response.result.result == 1) {
                    view.getUserInfoSucc(response.info)
                } else {
                    showFragmentToast(response!!.result.msg)
                }
            }

            override fun onFail(message: String) {

            }
        })
    }

}