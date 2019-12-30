package com.sxtx.user.adapter

import android.view.View
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

class TaskAdapter1 : BaseQuickAdapter<PublicData.InviteData, BaseViewHolder>(R.layout.item_task_1) {
    override fun convert(helper: BaseViewHolder?, item: PublicData.InviteData?) {
        item?.run {
            helper?.setText(R.id.tv_task,"纍計邀請"+addUpNum+"位好友（"+inviteNum+"/"+addUpNum+")")
            helper?.setText(R.id.tv_vip_time,"獎勵VIP+"+vipTime+"天")
            when(type){
                1->{//未完成
                    helper!!.getView<TextView>(R.id.tv_btn).setBackgroundResource(R.drawable.bg_white_15_stock_grey)
                    helper.getView<TextView>(R.id.tv_btn).setTextColor(mContext.resources.getColor(R.color.c_gray_white))
                    helper.getView<TextView>(R.id.tv_btn).isEnabled = false
                    helper.setText(R.id.tv_btn,"未完成")
                }
                2->{//可领取
                    helper!!.getView<TextView>(R.id.tv_btn).setBackgroundResource(R.drawable.bg_theme_15)
                    helper.getView<TextView>(R.id.tv_btn).setTextColor(mContext.resources.getColor(R.color.white))
                    helper.getView<TextView>(R.id.tv_btn).isEnabled = true
                    helper.setText(R.id.tv_btn,mContext.resources.getString(R.string.task_recive))
                    helper.getView<TextView>(R.id.tv_btn).setOnClickListener(View.OnClickListener {
                        clickListener?.click(dataId)
                    })
                }
                3->{//已领取
                    helper!!.getView<TextView>(R.id.tv_btn).setBackgroundResource(R.drawable.bg_grey_15_stock_grey)
                    helper.getView<TextView>(R.id.tv_btn).setTextColor(mContext.resources.getColor(R.color.white))
                    helper.getView<TextView>(R.id.tv_btn).isEnabled = false
                    helper.setText(R.id.tv_btn,"已領取")
                }
                else ->{
                    helper!!.getView<TextView>(R.id.tv_btn).setBackgroundResource(R.drawable.bg_white_15_stock_grey)
                    helper.getView<TextView>(R.id.tv_btn).setTextColor(mContext.resources.getColor(R.color.c_gray_white))
                    helper.getView<TextView>(R.id.tv_btn).isEnabled = false
                    helper.setText(R.id.tv_btn,"未完成")
                }
            }
        }
    }

    interface ClickListener {
        fun click(dataId: Long)
    }

    var clickListener: ClickListener? = null

    operator fun invoke(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }
}

