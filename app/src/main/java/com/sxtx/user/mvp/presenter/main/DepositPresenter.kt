package com.sxtx.user.mvp.presenter.main

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.IDepositView
import com.sxtx.user.util.ExceptionUtils

import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * 創建日期：2019/9/23 on 20:36
 * 介紹:
 * 作者:升級
 */
class DepositPresenter : APPresenter<IDepositView>() {
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
}
