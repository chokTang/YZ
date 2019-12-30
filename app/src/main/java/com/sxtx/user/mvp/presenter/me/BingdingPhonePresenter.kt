package com.sxtx.user.mvp.presenter.me

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.me.IBingdingPhoneView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */

class BingdingPhonePresenter : APPresenter<IBingdingPhoneView>(){

    fun bingdingPhone(phone :String,pass:String){
        //构建请求信息
        val request = Api.MobileCodeReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setMobile(phone)
                .setPassword(pass)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.MobileCodeResponse? = null
                try {
                    if (msg!=null){
                        response = Api.MobileCodeResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    showFragmentToast("手機綁定成功")
                    view.bingdSucc()
                }else{
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return commonApi.request(RequestUtil.BingdingPhoneReqeust(request))
            }
        })
    }

}