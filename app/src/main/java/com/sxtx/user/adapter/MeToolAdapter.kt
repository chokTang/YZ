package com.sxtx.user.adapter

import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sxtx.user.R
import com.sxtx.user.model.bean.MeToolBean

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class MeToolAdapter : BaseQuickAdapter<MeToolBean, BaseViewHolder>(R.layout.item_me_tool) {
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun convert(helper: BaseViewHolder?, item: MeToolBean?) {

        item?.run {
            helper?.setText(R.id.tv,text)
            helper?.getView<ImageView>(R.id.img)!!.setImageResource(topIcon)
            if (isNewMessage)
                helper?.setGone(R.id.red_point,true)
            else
                helper?.setGone(R.id.red_point,false)
        }
    }
}