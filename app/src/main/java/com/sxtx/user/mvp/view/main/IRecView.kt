package com.sxtx.user.mvp.view.main

import android.view.View
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface IRecView {

    fun collection(view: View, isFirst:Boolean, type: Int)

    fun getData(list: MutableList<PublicData.RecommendData>)

    fun refreshData(position:Int,response: Api.GetRecommendInABatchResponse)

    fun getBanner(list: MutableList<PublicData.AdvertisingData>)

    fun getAnnouncement(announcement: PublicData.TrumpetData)

    fun finishRefresh()

}