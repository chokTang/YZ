package com.sxtx.user.mvp.view.main

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface INewView {

    fun getNewVideoData(refresh: Boolean,list: MutableList<com.lyh.protocol.data.PublicData.NewestVideoData>, total: Long, page: Int)

    fun collection(position:Int,type:Int)

    fun finishRefresh()


}