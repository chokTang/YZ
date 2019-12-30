package com.sxtx.user.mvp.view.main

import com.lyh.protocol.login.Api

/**
 * 創建日期：2019/9/23 on 20:36
 * 介紹:
 * 作者:CHOK
 */
interface IDepositView{
    fun getPayData(respons: Api.GetPayDataResponse)
}
