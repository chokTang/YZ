package com.sxtx.user.mvp.view

import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api

/**
 *
 * 介紹:
 * 作者:CHOK
 */

interface IDeoisutType1View {

    fun getPayData(respons: Api.GetPayDataResponse)

    fun getPayUrl(string: String)

    fun getPayFinish(string: String)

    fun getSuccess(response:Api.ExchageKaMiResponse)

     fun getUserInfoSucc(data: PublicData.ApiUserData)

}