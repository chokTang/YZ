package com.sxtx.user.mvp.presenter.home

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.home.IFilterView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class FilterPresenter : APPresenter<IFilterView>() {


    /**
     * 获取筛选标签数据
     */
    fun getGetFilterDataReqeust() {

        //构建请求信息
        val request = Api.GetFilterDataReqeust.newBuilder()
                .setToken(token)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetFilterDataResponse? = null
                try {
                    if (msg != null) {
                        response = Api.GetFilterDataResponse.parseFrom(msg.body)
                    } else {
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {
                    view.getFilterData(response)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }

            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestGetFilterDataReqeust(request))
            }
        })
    }


    var pageNum = 1
    /**
     * 获取筛选的结果 视频数据
     */
    fun getGetFilterVideoDataReqeust(refresh: Boolean, tags: MutableList<PublicData.LongData>) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.GetFilterVideoDataReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .addAllVideoId(tags)
                .setPage(pageNum)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetFilterVideoDataResponse? = null
                try {
                    response = Api.GetFilterVideoDataResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getVideoData(refresh, response)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestGetFilterVideoDataReqeust(request))
            }
        })
    }
}