package com.sxtx.user.mvp.presenter.deposit

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.IBuyHistoryView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody


/**
 *
 * 介紹:
 * 作者:CHOK
 */

class BuyHistoryPresonter : APPresenter<IBuyHistoryView>() {
    var pageNum = 1

    fun getData(refresh: Boolean){
        if (refresh)
            pageNum = 1
        else
            pageNum++

        //构建请求信息
        val request = Api.RechargeRecordReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setPage(pageNum)
                .build()
        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.RechargeRecordResponse? = null
                try {
                    response = Api.RechargeRecordResponse.parseFrom(msg!!.body)
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                ExceptionUtils.checkStates(response,msg)
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getDataSucc(refresh, response.recordList,response.total,response.page)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestBodyRechargeRecodeRequest(request))
            }
        })
    }
}