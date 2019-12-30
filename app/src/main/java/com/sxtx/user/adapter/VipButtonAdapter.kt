package com.sxtx.user.adapter

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sxtx.user.R
import com.sxtx.user.model.bean.VipButtonBean

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class VipButtonAdapter : BaseQuickAdapter<VipButtonBean, BaseViewHolder>(R.layout.item_vip_button) {
    val IconList = arrayListOf(R.mipmap.icon_hd, R.mipmap.icon_quanqiu, R.mipmap.icon_qiangxian, R.mipmap.icon_all_global)
    @SuppressLint("NewApi")
    override fun convert(helper: BaseViewHolder?, item: VipButtonBean?) {
        //充值编号的排序 1-支付宝 2-其他 3-微信 4-人工
        var drawtop: Drawable? = null
        when (helper?.adapterPosition) {
            0 -> {
                drawtop = mContext.resources.getDrawable(IconList[0],null)
                helper.setText(R.id.tv_button,"熱門搶先")
            }
            1 -> {
                drawtop = mContext.resources.getDrawable(IconList[1],null)
                helper.setText(R.id.tv_button,"獨家特供")
            }
            2 -> {
                drawtop = mContext.resources.getDrawable(IconList[2],null)
                helper.setText(R.id.tv_button,"1080P")
            }
            3 -> {
                drawtop = mContext.resources.getDrawable(IconList[3],null)
                helper.setText(R.id.tv_button,"全球資源")
            }
        }
        drawtop!!.setBounds(0, 0, drawtop.minimumWidth, drawtop.minimumHeight)
        helper?.getView<TextView>(R.id.tv_button)!!.setCompoundDrawables(null, drawtop, null, null)
    }
}