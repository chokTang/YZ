package com.sxtx.user.adapter

import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.AbsoluteSizeSpan
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.like.utilslib.screen.DensityUtil
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R


/**
 *
 * 介紹: 充值套餐適配器
 * 作者:CHOK
 */
class DepositTcAdapter : BaseQuickAdapter<PublicData.PayMoneyData, BaseViewHolder>(R.layout.item_deposit_tc) {

    var type = 1 // 1 是直接充值的套餐适配器  2是卡密充值套餐适配器
    var position = -1

    override fun convert(helper: BaseViewHolder?, item: PublicData.PayMoneyData?) {
        item?.run {
            helper?.getView<TextView>(R.id.tv_old_price)?.paint!!.flags = Paint.STRIKE_THRU_TEXT_FLAG
            helper.setText(R.id.tv_time, dayDescribe)

            when (lable) {//标签 1, "体验" 2, "尝鲜" 3, "推荐" 4, "最火" 5, "优惠" 6, "超值"
                0->{
                    helper.setGone(R.id.tv_qp,false)
                }
                1 -> {
                    helper.setGone(R.id.tv_qp,true)
                    helper.setText(R.id.tv_qp, "體驗")
                }
                2 -> {
                    helper.setGone(R.id.tv_qp,true)
                    helper.setText(R.id.tv_qp, "嘗鮮")
                }
                3 -> {
                    helper.setGone(R.id.tv_qp,true)
                    helper.setText(R.id.tv_qp, "推薦")
                }
                4 -> {
                    helper.setGone(R.id.tv_qp,true)
                    helper.setText(R.id.tv_qp, "最火")
                }
                5 -> {
                    helper.setGone(R.id.tv_qp,true)
                    helper.setText(R.id.tv_qp, "優惠")
                }
                6 -> {
                    helper.setGone(R.id.tv_qp,true)
                    helper.setText(R.id.tv_qp, "超值")
                }
            }
            helper.setText(R.id.tv_des1, buyDescribe)
            helper.setText(R.id.tv_des2, introduce)
            helper.setGone(R.id.tv_des2,false)
            if (type == 1) {
                helper.setGone(R.id.ll_type_1, true)
                helper.setGone(R.id.tv_ka_time, false)
                val string = "¥$originalPrice"
                val string1 = "¥$topUpPrice"
                //设置字体(default,default-bold,monospace,serif,sans-serif)
                val msp = SpannableString(string)
                val msp1 = SpannableString(string1)
                msp.setSpan(AbsoluteSizeSpan(25), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                msp.setSpan(AbsoluteSizeSpan(25), 1, string.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                msp1.setSpan(AbsoluteSizeSpan(40), 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                msp1.setSpan(AbsoluteSizeSpan(75), 1, string1.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                helper.getView<TextView>(R.id.tv_old_price).text = msp
                helper.getView<TextView>(R.id.tv_new_price).text = msp1
            } else {
                helper.setGone(R.id.ll_type_1, false)
                helper.setGone(R.id.tv_ka_time, true)
                helper.setText(R.id.tv_ka_time, topUpPrice)
            }
            val layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            layoutParams.setMargins(DensityUtil.dpTopx(2f), 0, DensityUtil.dpTopx(2f), DensityUtil.dpTopx(2f))//4个参数按顺序分别是左上右下
            helper.getView<TextView>(R.id.tv_des1).layoutParams = layoutParams

            if (position==-1){
                helper.getView<LinearLayout>(R.id.ll_item).isSelected = option==1
                helper.getView<TextView>(R.id.tv_des1).isSelected = option==1
            }else{
                helper.getView<LinearLayout>(R.id.ll_item).isSelected = helper.adapterPosition == position
                helper.getView<TextView>(R.id.tv_des1).isSelected = helper.adapterPosition == position
            }
        }
    }
}