package com.sxtx.user.mvp.view

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface INewView {

    fun getNewVideoData(refresh: Boolean,list: MutableList<com.lyh.protocol.data.PublicData.NewestVideoData>)

    fun collection(position:Int,type:Int)

}