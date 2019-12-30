package com.sxtx.user.mvp.presenter.me

import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.login.Api
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.me.ICollectionView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class CollectionPresenter : APPresenter<ICollectionView>() {

    /**
     * 收藏历史查询
     *
     */
    fun getCheckCollectRecrdReqeust() {

        //构建请求信息
        val request = Api.CheckCollectRecrdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.CheckCollectRecrdResponse? = null
                try {
                    if (msg!=null){
                        response = Api.CheckCollectRecrdResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                 } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getCheckCollectRecrdReqeustData(response.videoList)
                } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestCheckCollectRecrdReqeust(request))
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
                        response = Api.ClickCollectResponse.parseFrom(msg!!.body)
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


    /**
     * 删除收藏
     *
     */
    fun DeleteCollectRecrdReqeust(position: Int, videoId: Long) {

        //构建请求信息
        val request = Api.DeleteCollectRecrdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .setVideoId(videoId)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.DeleteCollectRecrdResponse? = null
                try {
                    if (msg!=null){
                        response = Api.DeleteCollectRecrdResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    showFragmentToast(response.result.msg)
                    view.cancleCollection(position)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestDeleteCollectRecrdReqeustReqeust(request))
            }
        })
    }

}