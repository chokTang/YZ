package com.sxtx.user.adapter

import android.widget.ImageView
import android.widget.LinearLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R

/**
 *
 * 介紹: 聯繫人適配器
 * 作者:CHOK
 */

class LxrAdapter : BaseQuickAdapter<PublicData.TouchData, BaseViewHolder>(R.layout.item_lxr) {

    val lxrIconList = arrayListOf(R.mipmap.icon_qq, R.mipmap.icon_wx)

    override fun convert(helper: BaseViewHolder?, item: PublicData.TouchData?) {
        item?.run {
            helper?.setText(R.id.tv_account, data)
            when (type) {//1-qq 2-wx
                1 -> {
                    helper?.getView<LinearLayout>(R.id.ll_bg_shape)!!.setBackgroundResource(R.drawable.bg_shape_qq)
                    helper.getView<ImageView>(R.id.img_type)!!.setBackgroundResource(lxrIconList[0])
                    helper.setText(R.id.tv_position, "QQ")
                }
                2 -> {
                    helper?.getView<LinearLayout>(R.id.ll_bg_shape)!!.setBackgroundResource(R.drawable.bg_shape_wx)
                    helper.getView<ImageView>(R.id.img_type)!!.setBackgroundResource(lxrIconList[1])
                    helper.setText(R.id.tv_position, "微信")
                }
                else -> {
                    helper?.getView<ImageView>(R.id.img_type)!!.setBackgroundResource(lxrIconList[0])
                    helper.setText(R.id.tv_position, "QQ")
                }
            }
        }
    }
}