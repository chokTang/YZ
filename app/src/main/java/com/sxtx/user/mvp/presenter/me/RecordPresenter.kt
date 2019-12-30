package com.sxtx.user.mvp.presenter.me


import com.google.protobuf.InvalidProtocolBufferException
import com.like.base.base.inter.IRequestListener
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.model.bean.RecordBean
import com.sxtx.user.model.request.RequestUtil
import com.sxtx.user.mvp.APPresenter
import com.sxtx.user.mvp.view.me.IRecordView
import com.sxtx.user.util.AppPreference
import com.sxtx.user.util.ExceptionUtils
import io.reactivex.Observable
import okhttp3.ResponseBody

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class RecordPresenter : APPresenter<IRecordView>(){



    /**
     * 观看历史查询
     *
     */
    fun getCheckHistroyRecrdReqeust() {

        //构建请求信息
        val request = Api.CheckHistroyRecrdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.CheckHistroyRecrdResponse? = null
                try {
                    if (msg!=null){
                        response = Api.CheckHistroyRecrdResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    view.getCheckHistroyRecrdData( response.videoList)
                 } else {
                    showFragmentToast(response.result.msg)
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestCheckHistroyRecrdReqeust(request))
            }
        })
    }


    /**
     * 删除历史记录
     *
     */
    fun deleteHistroyRecrdReqeust(list: MutableList<RecordBean>) {
       val elements: MutableList<PublicData.LongData> = arrayListOf()
         for (element in list){
            var longData = PublicData.LongData.newBuilder().setVal(element.videoId).build()
             elements.add(longData)
        }


        //构建请求信息
        val request = Api.DeleteHistroyRecrdReqeust.newBuilder()
                .setToken(AppPreference.getIntance().token1)
                .addAllVideoId(elements)
                .build()

        onPageNoRequestData(true, object : IRequestListener<ResponseBody> {
            override fun onFail(message: String) {

            }

            override fun onSuccess(s: ResponseBody) {

                val msg = RequestUtil.Response(s.byteStream())

                var response: Api.DeleteHistroyRecrdResponse? = null
                try {
                    if (msg!=null){
                        response = Api.DeleteHistroyRecrdResponse.parseFrom(msg!!.body)
                    }else{
                        return
                    }
                  } catch (e: InvalidProtocolBufferException) {
                    e.printStackTrace()
                }
                if(!ExceptionUtils.checkStates(response,msg))return
                if (response!!.result != null && response.result.result == 1) {//验证成功
                    showFragmentToast(response.result.msg)
                    view.deleteSucceed()
                }
            }


            override fun onCreateObservable(): Observable<*> {
                return APPresenter.commonApi.request(RequestUtil.requestDeleteHistroyRecrdReqeust(request))
            }
        })
    }



}