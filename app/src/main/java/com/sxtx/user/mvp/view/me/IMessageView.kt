package com.sxtx.user.mvp.view.me

import com.lyh.protocol.data.PublicData

interface IMessageView {

    abstract fun getClickMyMessageResponseData(refresh:Boolean,videoList: List<PublicData.ApiUserMessage>)
}