package com.sxtx.user.mvp.presenter.home

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.INewView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class NewPresenter : APPresenter<INewView>() {

    fun clickAd(id : Long) {
        val reqeust = Api.ClickAdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setId(id)
                .build()
        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestBodyClickAdRequest(reqeust))
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

    var pageNum = 1

    /**
     * 请求最新/排行的视频数
     * type //1-最新的 2排行的
     */
    fun getNewestPageDataReqeust(refresh: Boolean, type: Int) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.GetNewestPageDataReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setType(type)
                .setPage(pageNum)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetNewestPageDataResponse? = null
                try {
                    response = Api.GetNewestPageDataResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getNewVideoData(refresh, response.newestVideoDataList,response.total,response.page)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetNewestPageDataReqeust(request))
            }
        })
    }


    /**
     * 收藏视频
     *
     */
    fun collection(position:Int,videoId: Long) {
        //构建请求信息
        val request = Api.ClickCollectReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setVideoId(videoId)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.ClickCollectResponse? = null
                try {
                    response = Api.ClickCollectResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.collection(position,response.type)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestClickCollectReqeust(request))
            }
        })
    }

}