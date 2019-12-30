package com.sxtx.user.mvp.view

import com.lyh.protocol.data.PublicData

/**
 *
 * 介紹:
 * 作者:CHOK
 */

interface IBuyHistoryView {
    fun getDataSucc(refresh:Boolean,list: MutableList<PublicData.RechargeRecordData>, total: Long, page: Int)
}