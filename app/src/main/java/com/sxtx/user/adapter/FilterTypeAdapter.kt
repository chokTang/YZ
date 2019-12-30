package com.sxtx.user.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class FilterTypeAdapter : BaseQuickAdapter<PublicData.FilterSilgeData, BaseViewHolder>(R.layout.item_filter_single) {
    var position = 0
    override fun convert(helper: BaseViewHolder?, item: PublicData.FilterSilgeData?) {
        item?.run {
            helper?.setText(R.id.tv_filter, item.name)
            helper?.getView<TextView>(R.id.tv_filter)?.isSelected = helper?.adapterPosition == position
        }
    }
}