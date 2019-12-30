package com.sxtx.user.mvp.presenter.me

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.me.IBindingView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */

class BindingPresenter : APPresenter<IBindingView>(){


    /**
     * 请求绑定邀请码
     *
     */
    fun inviteCodeReqeust(inviteCode :String) {

        //构建请求信息
        val request = Api.InviteCodeReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setInviteCode(inviteCode)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.InviteCodeResponse? = null
                try {
                    if (msg!=null){
                        response = Api.InviteCodeResponse.parseFrom(msg!!.body)
                    }else{
                       return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    showFragmentToast("邀請碼綁定成功")
                    view.bindSucc()
                }else{
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.inviteCodeReqeust(request))
            }
        })
    }



}