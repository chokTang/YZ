package com.sxtx.user.mvp.view.main

import android.view.View
import com.lyh.protocol.data.PublicData

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface IGcView {


    fun getVideoData(refresh: Boolean, list: MutableList<PublicData.ThreeBigVideoData>, total: Long, page:Int)

    fun collection( view:View,type:Int)

    fun finishRefresh()
}