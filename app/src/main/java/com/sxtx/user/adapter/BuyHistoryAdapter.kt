package com.sxtx.user.adapter

import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R

class BuyHistoryAdapter : BaseQuickAdapter<PublicData.RechargeRecordData,BaseViewHolder>(R.layout.item_buy_history){
    override fun convert(helper: BaseViewHolder?, item: PublicData.RechargeRecordData?) {
        item?.run {
            helper?.setText(R.id.tv_time,name)
            if (TextUtils.isEmpty(money))
                helper?.setGone(R.id.tv_money,false)
            else
                helper?.setGone(R.id.tv_money,true)
            helper?.setText(R.id.tv_money, "￥$money")
            when(type){
                //1-支付宝 2-银联 3-微信 4-人工 5卡密
                1->{
                    helper?.setText(R.id.tv_pay_type,"支付寶")
                }
                2->{
                    helper?.setText(R.id.tv_pay_type,"銀聯")
                }
                3->{
                    helper?.setText(R.id.tv_pay_type,"微信")
                }
                4->{
                    helper?.setText(R.id.tv_pay_type,"人工")
                }
                5->{
                    helper?.setText(R.id.tv_pay_type,"卡密")
                }
                else->{
                    helper?.setText(R.id.tv_pay_type,"其它")
                }
            }
            if (status == 1){
                helper?.setText(R.id.tv_pay_result,"支付成功")
                helper?.setTextColor(R.id.tv_pay_result,mContext.resources.getColor(R.color.color_green))
            }else{
                helper?.setText(R.id.tv_pay_result,"支付失敗")
                helper?.setTextColor(R.id.tv_pay_result,mContext.resources.getColor(R.color.color_red))
            }
            helper?.setText(R.id.tv_order_number, "訂單編號：$serialNumber")
            helper?.setText(R.id.tv_order_time, "交易時間：$time")
        }
    }
}