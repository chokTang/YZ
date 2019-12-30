package com.sxtx.user.mvp.view.me

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface IRecordView {

    fun getCheckHistroyRecrdData(list: MutableList<com.lyh.protocol.data.PublicData.ClassifyVideoData>)

    fun deleteSucceed();

}