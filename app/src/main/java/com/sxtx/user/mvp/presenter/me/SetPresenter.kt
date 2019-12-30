package com.sxtx.user.mvp.presenter.me

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener

import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.me.ISetView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */

class SetPresenter : APPresenter<ISetView>(){


    /**
     * 修改自動播放
     *
     */
    fun ModifyAutoPlayReqeust() {

        //构建请求信息
        val request = Api.ModifyAutoPlayReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.ModifyAutoPlayResponse? = null
                try {
                    if (msg!=null){
                        response = Api.ModifyAutoPlayResponse.parseFrom(msg!!.body)
                   }else{
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                   showFragmentToast("設置成功")
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestModifyAutoPlayReqeust(request))
            }
        })
    }




    /**
     * 修改自動播放
     *
     */
    fun AutoPlayReqeust() {

        //构建请求信息
        val request = Api.AutoPlayReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.AutoPlayResponse? = null
                try {
                    if (msg!=null){
                        response = Api.AutoPlayResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.AutoPlayReqeustSucceed(response.autoPlay)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.AutoPlayReqeust(request))
            }
        })
    }


}