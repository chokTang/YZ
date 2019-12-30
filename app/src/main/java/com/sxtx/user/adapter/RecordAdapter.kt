package com.sxtx.user.adapter

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sxtx.user.R
import com.sxtx.user.model.bean.RecordBean
import com.sxtx.user.util.GlideUtils

/**
 *
 * 介紹: 播放記錄適配器
 * 作者:CHOK
 */
class RecordAdapter : BaseQuickAdapter<RecordBean, BaseViewHolder>(R.layout.item_record) {

    var type =  1   //1  是不顯示編輯按鈕   2是要顯示編輯按鈕  3 是顯示收藏按鈕 不顯示標籤

    override fun convert(helper: BaseViewHolder?, item: RecordBean?) {
        when(type){
            1->{
                helper?.setGone(R.id.ll_select,false)
                helper?.setGone(R.id.ll_collection,false)
                helper?.setGone(R.id.tv_tag,true)
            }
            2->{
                helper?.setGone(R.id.ll_select,true)
                helper?.setGone(R.id.ll_collection,false)
                helper?.setGone(R.id.tv_tag,true)
            }
            3->{
                helper?.setGone(R.id.ll_select,false)
                helper?.setGone(R.id.ll_collection,true)
                helper?.setGone(R.id.tv_tag,false)
            }
        }
        helper?.getView<ImageView>(R.id.img_select)!!.isSelected = item?.isSelected!!
        helper.getView<ImageView>(R.id.img_collection)!!.isSelected = item.isCollected

        GlideUtils.GlideUtil(item.pic, helper.getView<ImageView>(R.id.img_pic))
        if (TextUtils.isEmpty(item.playNum))helper.setText(R.id.tv_watch, "0") else helper.setText(R.id.tv_watch, item.playNum)
        if (TextUtils.isEmpty(item.giveMark))helper.setText(R.id.tv_star, "0")else helper.setText(R.id.tv_star, item.giveMark)

        helper.setText(R.id.tv_name,item.name)
        helper.setText(R.id.tv_time,item.uptime)
        helper.addOnClickListener(R.id.ll_select)
        helper.addOnClickListener(R.id.ll_collection)
        helper.setGone(R.id.tv_tag, item.isFreeOfCharge == 1)
        helper.getView<TextView>(R.id.tv_video_time).text=item.time
    }
}