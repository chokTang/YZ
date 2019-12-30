package com.sxtx.user.mvp.view.main

import com.lyh.protocol.login.Api

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface ITopicView {

    fun getTopicData(pageNum:Int,refresh: Boolean, response: Api.GetSpecialPageDataResponse)

    fun finishRefresh()

}