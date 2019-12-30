package com.sxtx.user.adapter

import android.text.TextUtils
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.util.GlideUtils

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class RecVideoAdapter : BaseQuickAdapter<PublicData.ClassifyVideoData, BaseViewHolder>(R.layout.item_rec_video) {

    override fun convert(helper: BaseViewHolder?, item: PublicData.ClassifyVideoData?) {
        item?.run {

            GlideUtils.GlideUtil(pictureAddress,helper!!.getView<ImageView>(R.id.img_pic))

            helper.setText(R.id.tv_name,videoName)
            helper.setText(R.id.tv_time,upTime)
            helper.setText(R.id.tv_length,videoTime)
            if (TextUtils.isEmpty(playNum))helper.setText(R.id.tv_watch, "0") else helper.setText(R.id.tv_watch, playNum)
            if (TextUtils.isEmpty(giveMark))helper.setText(R.id.tv_star, "0")else helper.setText(R.id.tv_star, giveMark)
            helper.getView<ImageView>(R.id.img_collection)!!.isSelected = isCollect==1
            helper.addOnClickListener(R.id.ll_collection)
            helper.setGone(R.id.tv_tag,item.isFreeOfCharge == 1)
        }
    }
}