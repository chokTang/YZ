package com.sxtx.user.mvp.presenter.home

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.ITopicView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class TopicPresenter : APPresenter<ITopicView>() {
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
    var pageNum = 1

    /**
     * 請求專題數據
     */
    fun getSpecialPageDataReqeust(refresh: Boolean) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.GetSpecialPageDataReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setPage(pageNum)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.finishRefresh()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.GetSpecialPageDataResponse? = null
                try {
                    if (msg!=null){
                        response = Api.GetSpecialPageDataResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getTopicData(pageNum,refresh, response)
                } else {
                    view.finishRefresh()
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestGetSpecialPageDataReqeust(request))
            }
        })
    }


}