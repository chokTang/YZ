package com.sxtx.user.mvp.presenter.main

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.main.ISearchResultView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

class SearchResultPresenter :  APPresenter<ISearchResultView>() {

    /**
     * 確認搜索
     *
     */
    fun SureSearchKeyWordReqeust(keyWord: String) {

        //构建请求信息
        val request = Api.SureSearchKeyWordReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setKeyWord(keyWord)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {
                view.error()
            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.SureSearchKeyWordResponse? = null
                try {
                    if (msg!=null){
                        response = Api.SureSearchKeyWordResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                 } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.sureSearchList(response.videoList)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestSureSearchKeyWordReqeust(request))
            }
        })
    }


    /**
     * 添加收藏
     *
     */
    fun ClickCollectReqeust(position: Int, videoId: Long) {

        //构建请求信息
        val request = Api.ClickCollectReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setVideoId(videoId)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.ClickCollectResponse? = null
                try {
                    if (msg!=null){
                        response = Api.ClickCollectResponse.parseFrom(msg.body)
                    }else{
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.collection(position, response.type)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestClickCollectReqeust(request))
            }
        })
    }




}