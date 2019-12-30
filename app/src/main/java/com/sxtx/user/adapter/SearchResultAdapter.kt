package com.sxtx.user.adapter

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sxtx.user.R
import com.sxtx.user.model.bean.RecordBean
import com.sxtx.user.util.GlideUtils

class SearchResultAdapter : BaseQuickAdapter<RecordBean, BaseViewHolder>(R.layout.item_search_result) {

    var position = -1
     override fun convert(helper: BaseViewHolder?, item: RecordBean?) {
         helper?.setGone(R.id.ll_collection, true)
        helper?.setGone(R.id.tv_tag, false)
        if (helper != null&&item!=null) {
            if (helper.adapterPosition == position) {
                helper?.getView<ImageView>(R.id.img_collection)!!.isSelected = true
            } else {
                helper?.getView<ImageView>(R.id.img_collection)!!.isSelected = item!!.isCollected
            }

            item?.run {
                GlideUtils.GlideUtil(pic, helper.getView<ImageView>(R.id.img_pic))

                helper.setText(R.id.tv_name, name)
                helper.setText(R.id.tv_time,uptime)
                if (TextUtils.isEmpty(playNum))helper.setText(R.id.tv_watch, "0") else helper.setText(R.id.tv_watch, playNum)
                if (TextUtils.isEmpty(giveMark))helper.setText(R.id.tv_star, "0")else helper.setText(R.id.tv_star, giveMark)
                helper.addOnClickListener(R.id.ll_collection)
                helper.getView<TextView>(R.id.tv_video_time).text=time
            }
        }
    }


    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        val upNum = payloads[0]
        holder.getView<ImageView>(R.id.img_collection)!!.isSelected = upNum as Boolean
    }




}
