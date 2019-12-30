package com.sxtx.user.adapter

import android.graphics.Typeface
import android.widget.ImageView
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.sxtx.user.R
import com.sxtx.user.model.bean.InviteBean

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class InviteAdapter : BaseQuickAdapter<InviteBean, BaseViewHolder>(R.layout.item_invite_type) {
    override fun convert(helper: BaseViewHolder?, item: InviteBean?) {
        helper!!.setText(R.id.tv_position, (helper.adapterPosition+1).toString())
        helper.getView<TextView>(R.id.tv_position).setTypeface(Typeface.createFromAsset(mContext.assets,"FZCCHFW.TTF"))
        item?.run {
            when(type){
                0->{
                    helper?.setText(R.id.tv_title,"通過微信群分享")
                    helper?.setText(R.id.tv_detail,"把${mContext.resources.getString(R.string.app_name)}APP二維碼或者邀請鏈接分享到微信群")
                    helper.getView<ImageView>(R.id.img_type).setImageResource(R.mipmap.icon_task_wx)
                }
                1->{
                    helper?.setText(R.id.tv_title,"通過QQ群分享")
                    helper?.setText(R.id.tv_detail,"把${mContext.resources.getString(R.string.app_name)}APP二維碼或者邀請鏈接分享到QQ群")
                    helper.getView<ImageView>(R.id.img_type).setImageResource(R.mipmap.icon_task_qq)
                }
                2->{
                    helper?.setText(R.id.tv_title,"通過微信QQ好友分享")
                    helper?.setText(R.id.tv_detail,"把${mContext.resources.getString(R.string.app_name)}APP二維碼或者邀請鏈接發送給好友分享")
                    helper.getView<ImageView>(R.id.img_type).setImageResource(R.mipmap.icon_task_wx)
                }
                3->{
                    helper?.setText(R.id.tv_title,"通過附近的人分享")
                    helper?.setText(R.id.tv_detail,"可以通過QQ附近的人、陌陌、探探、Soul等推廣")
                    helper.getView<ImageView>(R.id.img_type).setImageResource(R.mipmap.icon_task_nearby)
                }
                4->{
                    helper?.setText(R.id.tv_title,"通過百度貼吧、各大論壇推廣")
                    helper?.setText(R.id.tv_detail,"把${mContext.resources.getString(R.string.app_name)}APP二維碼圖片分享到貼吧機各大論壇社區")
                    helper.getView<ImageView>(R.id.img_type).setImageResource(R.mipmap.icon_task_postit)
                }
                5->{
                    helper?.setText(R.id.tv_title,"通過新聞APP論壇推廣")
                    helper?.setText(R.id.tv_detail,"可以通過網易新聞、今日頭條、皮皮蝦等新聞APP評論區推廣您的邀請碼")
                    helper.getView<ImageView>(R.id.img_type).setImageResource(R.mipmap.icon_task_wy)
                }

                else -> {

                }
            }
        }
    }
}