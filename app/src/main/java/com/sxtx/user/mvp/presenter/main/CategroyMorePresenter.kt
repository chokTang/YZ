package com.sxtx.user.mvp.presenter.main

import android.view.View
import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.like.utilslib.other.LogUtil
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.ICategoryMoreView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class CategroyMorePresenter : APPresenter<ICategoryMoreView>() {


    var pageNum = 1

    /**
     * 点击三大分类的更多
     */
    fun getGetThreeBigMoreVideoReqeust(refresh: Boolean, threeBigDataId: Long?) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.GetThreeBigMoreVideoReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setThreeBigDataId(threeBigDataId!!)
                .setPage(pageNum)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetThreeBigMoreVideoResponse? = null
                try {
                    response = Api.GetThreeBigMoreVideoResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getVideoData(refresh, response.threeBigVideoList,response.total,response.page)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetThreeBigMoreVideoReqeust(request))
            }
        })
    }


    var pageNumS = 1

    /**
     * 点击专题进入专题的更多
     */
    fun getGetSpecialMoreVideoReqeust(refresh: Boolean, specialId: Long?) {
        if (refresh) {
            pageNumS = 1
        } else {
            pageNumS++
        }
        //构建请求信息
        val request = Api.GetSpecialMoreVideoReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setSpecialId(specialId!!)
                .setPage(pageNumS)
                .build()
        var start = System.currentTimeMillis()
        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetSpecialMoreVideoResponse? = null
                try {
                    response = Api.GetSpecialMoreVideoResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    var end = System.currentTimeMillis()
                    LogUtil.logd("------>>>"+(end - start))
                    view.getVideoData(refresh, response.threeBigVideoList,response.total,response.page)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetSpecialMoreVideoReqeust(request))
            }
        })
    }


    /**
     * 收藏视频
     *
     */
    fun collection(mview: View, videoId: Long) {
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


}