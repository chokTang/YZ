package com.sxtx.user.adapter

import android.text.TextUtils
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sxtx.user.R
import com.sxtx.user.model.bean.RecordBean
import com.sxtx.user.util.GlideUtils

class CollectionAdapter : BaseQuickAdapter<RecordBean, BaseViewHolder>(R.layout.item_collection) {

    var position = -1
    override fun convert(helper: BaseViewHolder?, item: RecordBean?) {
        helper?.setGone(R.id.ll_select,false)
        helper?.setGone(R.id.ll_collection,true)
        helper?.getView<ImageView>(R.id.img_select)!!.isSelected = item?.isSelected!!
       if (helper != null&&item!=null) {
            if (helper.adapterPosition == position) {
                helper?.getView<ImageView>(R.id.img_collection)!!.isSelected = true
            } else {
                helper?.getView<ImageView>(R.id.img_collection)!!.isSelected = item!!.isCollected
            }
        }
        GlideUtils.GlideUtil(item.pic,helper!!.getView<ImageView>(R.id.img_pic))
        helper.setGone(R.id.tv_tag, item.isFreeOfCharge == 1)
        helper.setText(R.id.tv_name,item.name)
        helper.setText(R.id.tv_time,item.uptime)
        if (TextUtils.isEmpty(item.playNum))helper.setText(R.id.tv_watch, "0") else helper.setText(R.id.tv_watch, item.playNum)
        if (TextUtils.isEmpty(item.giveMark))helper.setText(R.id.tv_star, "0")else helper.setText(R.id.tv_star, item.giveMark)
        helper.addOnClickListener(R.id.ll_select)
        helper.addOnClickListener(R.id.ll_collection)
        helper.getView<TextView>(R.id.tv_video_time).text=item.time
//        helper.setIsRecyclable(false)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
            return
        }

        val upNum = payloads[0]
        holder?.getView<ImageView>(R.id.img_collection)!!.isSelected = upNum as Boolean
    }

    //  删除数据
    fun removeData(position:Int) {
       this. position=position
        this.data.removeAt(position);
        //删除动画
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

}