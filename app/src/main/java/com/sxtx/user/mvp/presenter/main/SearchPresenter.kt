package com.sxtx.user.mvp.presenter.main

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.model.request.RequestUtil.Response

import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.ISearchView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

class SearchPresenter :  APPresenter<ISearchView>() {



    /**
     * 搜索查詢
     *
     */
    fun ClickSearchReqeust() {

        //构建请求信息
        val request = Api.ClickSearchReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = Response(s.byteStream())

                var response: Api.ClickSearchResponse? = null
                try {
                    if (msg != null) {
                        response = Api.ClickSearchResponse.parseFrom(msg!!.body)

                    } else {
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                  view.clickSearchReqeustSucceed(response.sysKeyWordList,response.apiUserKeyWordList)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestClickSearchReqeust(request))
            }
        })
    }


    /**
     * 清除搜索
     *
     */
    fun clickRemoveRecordReqeust() {

        //构建请求信息
        val request = Api.ClickRemoveRecordReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = Response(s.byteStream())

                var response: Api.ClickRemoveRecordResponse? = null
                try {
                    if (msg != null) {
                        response = Api.ClickRemoveRecordResponse.parseFrom(msg!!.body)
                    } else {
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.clickRemoveSucceed();

                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestClickRemoveRecordReqeust(request))
            }
        })
    }
}