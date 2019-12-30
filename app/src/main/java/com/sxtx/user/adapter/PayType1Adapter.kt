package com.sxtx.user.adapter

import android.graphics.drawable.Drawable
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R

/**
 *
 * 介紹: 支付方式1適配器
 * 作者:CHOK
 */
class PayType1Adapter : BaseQuickAdapter<PublicData.IntData, BaseViewHolder>(R.layout.item_pay_type_1) {
    val payTypeIconList = arrayListOf(R.mipmap.icon_zfb, R.mipmap.icon_yl, R.mipmap.icon_wx_1, R.mipmap.icon_rg)
    var position = -1
    override fun convert(helper: BaseViewHolder?, item: PublicData.IntData?) {
        item?.run {
            //充值编号的排序 1-支付宝 2-其他 3-微信 4-人工
            var drawleft: Drawable? = null
            when(`val`){
                1->{
                    drawleft = mContext.resources.getDrawable(payTypeIconList[0])
                    helper?.setText(R.id.tv_pay, "支付寶")
                }
                2->{
                    drawleft = mContext.resources.getDrawable(payTypeIconList[1])
                    helper?.setText(R.id.tv_pay, "其他")
                }
                3->{
                    drawleft = mContext.resources.getDrawable(payTypeIconList[2])
                    helper?.setText(R.id.tv_pay, "微信")
                }
                4->{
                    drawleft = mContext.resources.getDrawable(payTypeIconList[3])
                    helper?.setText(R.id.tv_pay, "人工")
                }
            }
            drawleft!!.setBounds(0, 0, drawleft.minimumWidth, drawleft.minimumHeight)
            helper?.getView<TextView>(R.id.tv_pay)!!.setCompoundDrawables(drawleft, null, null, null)
            helper.getView<LinearLayout>(R.id.ll_pay)!!.isSelected = helper.adapterPosition == position
        }
    }
}