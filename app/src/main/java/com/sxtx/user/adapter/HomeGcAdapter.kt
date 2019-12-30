package com.sxtx.user.adapter

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.sxtx.user.R
import kotlinx.android.synthetic.main.item_home_gc.view.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class HomeGcAdapter : BaseQuickAdapter<com.lyh.protocol.data.PublicData.ThreeBigVideoData, HomeGcAdapter.GcViewHolder>(R.layout.item_home_gc) {


    override fun convert(helper: GcViewHolder?, item: com.lyh.protocol.data.PublicData.ThreeBigVideoData?) {
        val gridLayoutManager = GridLayoutManager(mContext, 2)
        helper?.getView<RecyclerView>(R.id.rv_gc_content)!!.layoutManager = gridLayoutManager
        helper.getView<RecyclerView>(R.id.rv_gc_content)!!.adapter = helper.adapter
        helper.getView<RecyclerView>(R.id.rv_gc_content)!!.isFocusable = false
        item?.run {
            helper.setText(R.id.tv_title, recommendName)
            helper.adapter!!.setNewData(videoDataList)
            helper.addOnClickListener(R.id.tv_more)
        }

        helper.adapter?.setOnItemChildClickListener { _, view, position ->
            clickListener?.click(helper.adapterPosition, position,view,false)
        }
        helper.adapter?.setOnItemClickListener { _, view, position ->
            clickListener?.click(helper.adapterPosition, position,view,true)
        }
    }

    class GcViewHolder(view: View) : BaseViewHolder(view) {
        var adapter: HomeGcContentAdapter? = null
        init {
            adapter = HomeGcContentAdapter()
            view.rv_gc_content.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_white_16))
        }
    }


    interface ClickListener {
        fun click(firstPosion: Int, secondPosion: Int,view: View,isItemCLick: Boolean)
    }

    var clickListener: ClickListener? = null

    operator fun invoke(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }

}