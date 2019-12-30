package com.sxtx.user.mvp.view.live

import com.lyh.protocol.login.Api

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface IRecLiverView {

    fun getRecommedData(refresh: Boolean, response: Api.GetRecommedThrHostResponse?)

    fun getLivePlatformData(refresh: Boolean, response: Api.GetLivePlatformResponse)

    fun finishRefresh()

    fun noData()
}