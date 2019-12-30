package com.sxtx.user.mvp.view.main

import com.lyh.protocol.data.PublicData

/**
 * 創建日期：2019/9/23 on 20:14
 * 介紹:
 * 作者:CHOK
 */
interface IHomeView{
    fun getAnnouncement(announcement: PublicData.NoticeData)
}
