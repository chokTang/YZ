package com.sxtx.user.mvp.presenter.home

import android.view.View
import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.IGcView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class GcPresenter : APPresenter<IGcView>(){

    var pageNum = 1

    /**
     * 请求首页 请求三大数据
     * type //1-国产 2日韩 3欧美
     */
    fun getGetThreeBigVideoPageDataReqeust(refresh: Boolean, type: Int) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.GetThreeBigVideoPageDataReqeust.newBuilder()
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

                var response: Api.GetThreeBigVideoPageDataResponse? = null
                try {
                    response = Api.GetThreeBigVideoPageDataResponse.parseFrom(msg!!.body)
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
                return commonApi.request(RequestUtil.requestGetThreeBigVideoPageDataReqeust(request))
            }
        })
    }


    /**
     * 收藏视频
     *
     */
    fun collection(mview: View,videoId: Long) {
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
                    view.collection(mview,response.type)
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