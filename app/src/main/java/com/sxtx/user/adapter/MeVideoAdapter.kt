package com.sxtx.user.adapter

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.util.GlideUtils

class MeVideoAdapter : BaseQuickAdapter<PublicData.ClassifyVideoData, BaseViewHolder>(R.layout.item_me_video) {

    var position = -1
    override fun convert(helper: BaseViewHolder?, item: PublicData.ClassifyVideoData?) {
        GlideUtils.GlideUtil(item!!.pictureAddress,helper!!.getView<ImageView>(R.id.img))
        helper.setText(R.id.tv_name,item.videoName)
    }
}