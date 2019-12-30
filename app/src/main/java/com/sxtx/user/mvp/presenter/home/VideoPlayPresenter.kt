package com.sxtx.user.mvp.presenter.home

import android.view.View
import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.home.IVideoPlayView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class VideoPlayPresenter : APPresenter<IVideoPlayView>() {
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
     * 根據視頻ID獲取到視頻資料
     *
     */
    fun getpPlayVideoReqeust(videoId: Long) {

        //构建请求信息
        val request = Api.GetpPlayVideoReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setVideoId(videoId)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetPlayVideoResponse? = null
                try {
                    response = Api.GetPlayVideoResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getVideoData(response)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetpPlayVideoReqeust(request))
            }
        })
    }


    /**
     * 添加收藏
     *
     */
    fun clickCollectReqeust(mview: View, videoId: Long) {

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
                    view.collection(mview, response.type)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestClickCollectReqeust(request))
            }
        })
    }


    /**
     * 删除收藏
     *
     */
    fun deleteCollectRecrdReqeust(mview: View, videoId: Long) {

        //构建请求信息
        val request = Api.DeleteCollectRecrdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setVideoId(videoId)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.DeleteCollectRecrdResponse? = null
                try {
                    response = Api.DeleteCollectRecrdResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.cancleCollection(mview)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestDeleteCollectRecrdReqeustReqeust(request))
            }
        })
    }


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
     * 上传视频播放时间
     */
    fun uploadPlayTime(minite:Long) {

        //构建请求信息
        val request = Api.WatichVideoReqeust.newBuilder()
                .setToken(token)
                .setTime(minite)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.WatichVideoResponse? = null
                try {
                    response = Api.WatichVideoResponse.parseFrom(msg!!.body)
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
                return commonApi.request(RequestUtil.requestWatichVideoReqeust(request))
            }
        })
    }


    /**
     * 点赞视频
     */
    fun likeVideo(videoId: Long,type:Int) {
        //构建请求信息
        val request = Api.LikeToTrampleReqeust.newBuilder()
                .setToken(token)
                .setType(type)
                .setVideoId(videoId)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.LikeToTrampleResponse? = null
                try {
                    response = Api.LikeToTrampleResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {
                    view.likeVideo(response)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestLikeToTrampleReqeust(request))
            }
        })
    }

}