package com.sxtx.user.mvp.presenter.main

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.IHomeView
import com.sxtx.user.util.AppPreference
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 * 創建日期：2019/9/23 on 20:12
 * 介紹:
 * 作者:CHOK
 */
class HomePresenter : APPresenter<IHomeView>(){
    /**
     * 获取公告
     *
     */
    fun getTrumpetReqeust() {
        //构建请求信息
        val request = Api.ClickNoticeReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.ClickNoticeResponse? = null
                try {
                    if(msg!=null){
                        response = Api.ClickNoticeResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }

                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }

//                RequestUtil.checkStates(response,msg)
                if (response != null) {
                    if (response.result != null && response.result.result == 1) {//验证成功
                        view.getAnnouncement(response.notice)
                    } else {
        //                    showFragmentToast(response.result.msg)
                    }
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.requestClickNoticeReqeust(request))
            }
        })
    }

}
