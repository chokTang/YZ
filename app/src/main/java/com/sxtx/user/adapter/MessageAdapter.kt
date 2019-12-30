package com.sxtx.user.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R

class MessageAdapter  : BaseQuickAdapter<PublicData.ApiUserMessage, BaseViewHolder>(R.layout.item_message) {



    override fun convert(helper: BaseViewHolder?, item: PublicData.ApiUserMessage?) {

        helper?.setText(R.id.tv_time, item!!.messageTime)
        helper?.setText(R.id.tv_message, item!!.content)

    }



}