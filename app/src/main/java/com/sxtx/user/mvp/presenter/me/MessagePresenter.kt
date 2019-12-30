package com.sxtx.user.mvp.presenter.me

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil

import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.me.IMessageView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

class MessagePresenter :APPresenter<IMessageView>(){

    var pageNum = 1

    /**
     * 获取我的消息
     *
     */
    fun getClickMyMessageReqeust(refresh:Boolean) {
        if (refresh) {
            pageNum = 1
        } else {
            pageNum++
        }
        //构建请求信息
        val request = Api.ClickMyMessageReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setPage(pageNum)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.ClickMyMessageResponse? = null
                try {
                    if (msg!=null){
                        response = Api.ClickMyMessageResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                 } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                  view.getClickMyMessageResponseData(refresh, response.listMessageList)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestClickMyMessageReqeust(request))
            }
        })
    }
}