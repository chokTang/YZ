package com.sxtx.user.fragment.home

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.ArrayMap
import android.view.View
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.activity.VideoPlayActivity
import com.sxtx.user.adapter.FilterAdapter
import com.sxtx.user.adapter.HomeGcContentAdapter
import com.sxtx.user.mvp.presenter.home.FilterPresenter
import com.sxtx.user.mvp.view.home.IFilterView
import kotlinx.android.synthetic.main.fragment_filter.*


/**
 *
 * 介紹:筛选
 * 作者:CHOK
 */
class HomeFilterFragment : BaseFragment<FilterPresenter>(), IFilterView {


    var list: MutableList<PublicData.ClassifyVideoData> = arrayListOf()
    var adapter: FilterAdapter? = null
    var videoAdapter: HomeGcContentAdapter? = null
    var tags: MutableList<PublicData.LongData> = arrayListOf()

    companion object {
        const val KEY_FILTER = "key_filter"
        fun newIncetance(): HomeFilterFragment {
            val homefilterFragment = HomeFilterFragment()
            val bundle = Bundle()
//            bundle.putInt(KEY_FILTER, type)
            homefilterFragment.arguments = bundle
            return homefilterFragment
        }
    }


    override fun getResId(): Any {
        return R.layout.fragment_filter
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_filter.layoutManager = linearLayoutManager
        adapter = FilterAdapter()
        rv_filter.adapter = adapter

        val gridLayoutManager = GridLayoutManager(activity, 2)
        rv_filter_video.layoutManager = gridLayoutManager
        videoAdapter = HomeGcContentAdapter()
        videoAdapter?.isShowSvga = false
        rv_filter_video.adapter = videoAdapter
        rv_filter_video.isNestedScrollingEnabled = false
        rv_filter_video.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_white_16))

    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)

        refresh_layout.autoRefresh()

        refresh_layout.setOnRefreshListener {
            mPresenter.getGetFilterDataReqeust()
        }

        refresh_layout.setOnLoadMoreListener {
            mPresenter.getGetFilterVideoDataReqeust(false, tags)
        }


        adapter?.invoke(object : FilterAdapter.ClickListener {
            override fun click(tag: PublicData.FilterSilgeData, firstPosition: Int, secondPosition: Int) {
                mPresenter.getGetFilterVideoDataReqeust(true, filterTags(tag, firstPosition, secondPosition))
            }
        })

        videoAdapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter!!.data[position] as PublicData.ClassifyVideoData
            startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID, bean.videoId))
        }
    }

    @SuppressLint("UseSparseArrays")
    var map = ArrayMap<Int, Long>()

    /**
     * 从二维数组中选出选中项的tags
     */
    fun filterTags(tag: PublicData.FilterSilgeData, firstPosition: Int, secondPosition: Int): MutableList<PublicData.LongData> {
        map[firstPosition] = tag.id.toLong()
        tags.clear()
        val iterator = map.entries.iterator()
        while (iterator.hasNext()) {
            val mmap = iterator.next()
            val bean = PublicData.LongData.newBuilder()
            bean.`val` = mmap.value
            tags.add(bean.build())
        }
        return tags
    }

    /**
     * 筛选标签数据回调
     */
    override fun getFilterData(response: Api.GetFilterDataResponse) {
        adapter?.setNewData(response.listFilterDataList)
        if (tags.size==0){
            for (i in 0 until response.listFilterDataList!!.size) {
                map[i] = 0
                val bean = PublicData.LongData.newBuilder()
                bean.`val` = 0
                tags.add(bean.build())
            }
        }
        mPresenter.getGetFilterVideoDataReqeust(true, tags)
    }


    override fun finishRefresh() {
        refresh_layout.finishRefresh()
    }

    /**
     * 筛选结果数据回调
     */
    override fun getVideoData(refresh: Boolean, response: Api.GetFilterVideoDataResponse) {
        if (refresh) {//刷新
            refresh_layout.finishRefresh()
            this.list.clear()
            this.list.addAll(response.videoDataList)
        } else {//加载
            refresh_layout.finishLoadMore()
            this.list.addAll(response.videoDataList)
            if (response.total == response.page.toLong()) {//添加完数据
                refresh_layout.finishLoadMoreWithNoMoreData()
            }
        }
        if (this.list.size == 0) {//没有数据
            videoAdapter!!.setNewData(this.list)
//            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
//            videoAdapter!!.emptyView = emptyView
            view_empty.visibility = View.VISIBLE
        } else {//有数据
            view_empty.visibility = View.GONE
            videoAdapter!!.setNewData(this.list)
        }
    }
}