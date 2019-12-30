package com.sxtx.user.mvp.view.live

import com.lyh.protocol.login.Api

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface ILiveDetailView {

    fun getPayData(respons: Api.GetPayDataResponse)


    fun uploadTime(respons: Api.WatichLiveResponse)

    fun getFreeTime(respons: Api.GetLiveFreeTimeResponse)

}