package com.sxtx.user.mvp.view.home

import android.view.View
import com.lyh.protocol.login.Api

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface IVideoPlayView {

    fun getVideoData(response: Api.GetPlayVideoResponse)


    fun collection(view: View,type:Int)


    fun cancleCollection(view: View)


    fun getPayData(respons: Api.GetPayDataResponse)


    fun uploadTime(respons: Api.WatichVideoResponse)

    fun likeVideo(respons: Api.LikeToTrampleResponse)

}