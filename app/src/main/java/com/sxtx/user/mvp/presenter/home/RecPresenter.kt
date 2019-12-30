package com.sxtx.user.mvp.presenter.home

import android.view.View
import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.IRecView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class RecPresenter : APPresenter<IRecView>() {
    fun clickAd(id:Long) {
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

    /**
     *请求推荐页数据
     */
    fun GetRecommendPageDataReqeust() {

        //构建请求信息
        val request = Api.GetRecommendPageDataReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {
                view.finishRefresh()
                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetRecommendPageDataResponse? = null
                try {
                    if (msg!=null){
                        response = Api.GetRecommendPageDataResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getData(response.recommendList)
                    view.getBanner(response.bannerList)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestGetRecommendPageDataReqeust(request))
            }
        })
    }

    /**
     *请求换一批
     */
    fun GetRecommendInABatchReqeust(position:Int,recommendId:Long) {

        //构建请求信息
        val request = Api.GetRecommendInABatchReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setRecommendId(recommendId)
                .build()

        onPageNoRequestData(false, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {
                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetRecommendInABatchResponse? = null
                try {
                    if(msg!=null){
                        response = Api.GetRecommendInABatchResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.refreshData(position,response)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestGetRecommendInABatchReqeust(request))
            }
        })
    }


    /**
     * 获取公告
     *
     */
    fun getTrumpetReqeust() {
        //构建请求信息
        val request = Api.GetTrumpetReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {
                view.finishRefresh()
                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetTrumpetResponse? = null
                try {
                    if(msg!=null){
                        response = Api.GetTrumpetResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
               } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getAnnouncement(response.trumpet)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetTrumpetReqeust(request))
            }
        })
    }


    /**
     * 收藏视频
     *
     */
    fun collection(mview: View, isFirst:Boolean, videoId: Long) {
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
                    if(msg!=null){
                       response = Api.ClickCollectResponse.parseFrom(msg!!.body)
                     }else{
                        return
                    }
                 } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.collection(mview,isFirst,response.type)
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