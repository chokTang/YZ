package com.sxtx.user.adapter

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import kotlinx.android.synthetic.main.item_filter_type.view.*

/**
 *
 * 介紹:
 * 作者:CHOK
 */
class FilterAdapter : BaseQuickAdapter<PublicData.FilterListData, FilterAdapter.RecViewHolder>(R.layout.item_filter_type) {


    override fun convert(helper: RecViewHolder?, item: PublicData.FilterListData?) {
        val linearLayoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)
        helper?.getView<RecyclerView>(R.id.rv_filter_type)!!.layoutManager = linearLayoutManager
        helper.getView<RecyclerView>(R.id.rv_filter_type)!!.adapter = helper.adapter
        helper.getView<RecyclerView>(R.id.rv_filter_type)!!.isFocusable = false
        helper.adapter?.setNewData(item?.listDataList)
        helper.adapter?.setOnItemClickListener { adapter, view, position ->
            helper.adapter?.position = position
            adapter.notifyDataSetChanged()
            clickListener?.click(helper.adapter!!.getItem(position)!!,helper.adapterPosition,position)
        }
    }

    class RecViewHolder(view: View) : BaseViewHolder(view) {
        var adapter: FilterTypeAdapter? = null
        init {
            adapter = FilterTypeAdapter()
            view.rv_filter_type.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_f4_10))
        }
    }

    interface ClickListener {
        fun click(tag: PublicData.FilterSilgeData,firstPosition:Int,secondPosition: Int)
    }

    var clickListener: ClickListener? = null

    operator fun invoke(clickListener: ClickListener?) {
        this.clickListener = clickListener
    }


}