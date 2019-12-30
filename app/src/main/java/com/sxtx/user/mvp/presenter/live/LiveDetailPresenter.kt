package com.sxtx.user.mvp.presenter.live

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.live.ILiveDetailView
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class LiveDetailPresenter : APPresenter<ILiveDetailView>() {

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
                    if(msg!=null){
                        response = Api.GetPayDataResponse.parseFrom(msg!!.body)
                      }else{
                        return
                    }
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
     * 上传视频播放时间
     */
    fun uploadPlayTime(minite:Long) {

        //构建请求信息
        val request = Api.WatichLiveReqeust.newBuilder()
                .setToken(token)
                .setTime(minite)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                showFragmentToast(message)
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.WatichLiveResponse? = null
                try {
                    if (msg!=null){
                        response = Api.WatichLiveResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {
                    view.uploadTime(response)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestWatichLiveReqeust(request))
            }
        })
    }



    /**
     * 請求直播免費時間
     */
    fun GetLiveFreeTimeReqeust() {

        //构建请求信息
        val request = Api.GetLiveFreeTimeReqeust.newBuilder()
                .setToken(token)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                showFragmentToast(message)
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetLiveFreeTimeResponse? = null
                try {
                    if (msg!=null){
                        response = Api.GetLiveFreeTimeResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {
                    view.getFreeTime(response)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestGetLiveFreeTimeReqeust(request))
            }
        })
    }
}