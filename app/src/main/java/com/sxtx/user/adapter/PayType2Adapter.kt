package com.sxtx.user.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R

/**
 *
 * 介紹: 支付方式2適配器
 * 作者:CHOK
 */
class PayType2Adapter : BaseQuickAdapter<PublicData.PayTypeData, BaseViewHolder>(R.layout.item_pay_type_2) {
    var position = -1
    override fun convert(helper: BaseViewHolder?, item: PublicData.PayTypeData?) {
        item.run {
            helper!!.setText(R.id.tv_name, this!!.name)
            helper.getView<ImageView>(R.id.img_select)!!.isSelected = helper.adapterPosition ==position
        }
    }
}