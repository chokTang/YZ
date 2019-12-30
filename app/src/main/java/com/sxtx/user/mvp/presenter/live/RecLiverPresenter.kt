package com.sxtx.user.mvp.presenter.live

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.like.utilslib.app.CommonUtil
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.live.IRecLiverView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class RecLiverPresenter : APPresenter<IRecLiverView>() {

    var pageNum = 1

    /**
     * 獲取主播列表
     */
    fun getGetRecommedThrHostReqeust(refresh: Boolean, platform: String) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.GetRecommedThrHostReqeust.newBuilder()
                .setToken(token)
                .setPage(pageNum)
                .setPlatform(platform)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetRecommedThrHostResponse? = null
                try {
                    if (!CommonUtil.isNull(msg)) {
                        response = Api.GetRecommedThrHostResponse.parseFrom(msg!!.body)
                    } else {
                        view.noData()
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {
                    view.getRecommedData(refresh, response)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestGetRecommedThrHostReqeust(request))
            }
        })
    }

    /**
     * 獲取平台列表
     */
    fun getGetLivePlatformReqeust(refresh: Boolean) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.GetLivePlatformReqeust.newBuilder()
                .setToken(token)
                .setPage(pageNum)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetLivePlatformResponse? = null
                try {
                    if (!CommonUtil.isNull(msg)) {
                        response = Api.GetLivePlatformResponse.parseFrom(msg!!.body)
                    } else {
                        view.noData()
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {
                    view.getLivePlatformData(refresh, response)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestGetLivePlatformReqeust(request))
            }
        })
    }


    /**
     * 点击广告
     */
    fun clickAd(id:Long) {
        val reqeust = Api.ClickAdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setId(id)
                .build()
        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestBodyClickAdRequest(reqeust))
            }

            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.ClickAdResponse? = null
                try {
                    if (msg != null) {
                        response = Api.ClickAdResponse.parseFrom(msg.body)
                    } else {
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }

                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {

                } else {
                    showFragmentToast(response.result.msg)
                }
            }
        })
    }

}