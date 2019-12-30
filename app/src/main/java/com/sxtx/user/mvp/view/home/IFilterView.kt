package com.sxtx.user.mvp.view.home

import com.lyh.protocol.login.Api

/**
 *
 * 介紹:
 * 作者:CHOK
 */
interface IFilterView {

    fun getFilterData(response: Api.GetFilterDataResponse)

    fun finishRefresh()

    fun getVideoData(refresh: Boolean, response: Api.GetFilterVideoDataResponse)

}