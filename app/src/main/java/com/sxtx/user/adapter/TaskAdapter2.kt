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

class TaskAdapter2 : BaseQuickAdapter<PublicData.CommomData, BaseViewHolder>(R.layout.item_task_2) {
    override fun convert(helper: BaseViewHolder?, item: PublicData.CommomData?) {
        item?.run {
            //绑定手机=1,点击广告3次=2,保存二维码=3,观看视频时间=4,观看直播次数=5
            when(taskType){
                1->{
                    helper?.setImageResource(R.id.img_type,R.mipmap.icon_task_bind)
                    helper?.setText(R.id.tv_task,"綁定手機（"+alreadyNum+"/"+needNum+"）")
                }
                2->{
                    helper?.setImageResource(R.id.img_type,R.mipmap.icon_task_click)
                    helper?.setText(R.id.tv_task,"點擊廣告"+needNum+"次（"+alreadyNum+"/"+needNum+"）")
                }
                3->{
                    helper?.setImageResource(R.id.img_type,R.mipmap.icon_task_qrcode)
                    helper?.setText(R.id.tv_task,"保存二維碼（"+alreadyNum+"/"+needNum+"）")
                }
                4->{
                    helper?.setImageResource(R.id.img_type,R.mipmap.icon_task_video)
                    helper?.setText(R.id.tv_task,"纍計觀看視頻"+needNum+"分鐘（"+alreadyNum+"/"+needNum+"）")
                }
                5->{
//                    helper?.setImageResource(R.id.img_type,R.mipmap.icon_task_live)
                    helper?.setImageResource(R.id.img_type,R.mipmap.icon_task_video)
                    helper?.setText(R.id.tv_task,"纍計觀看直播"+needNum+"分鐘（"+alreadyNum+"/"+needNum+"）")
                }
            }

            helper?.setText(R.id.tv_vip_time,"獎勵VIP+"+vipTime+"小時")

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

