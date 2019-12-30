package com.sxtx.user.fragment.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.like.base.adapter.rvhelper.DividerItemDecoration
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.activity.VideoPlayActivity
import com.sxtx.user.adapter.HomeGcAdapter
import com.sxtx.user.mvp.presenter.home.GcPresenter
import com.sxtx.user.mvp.view.main.IGcView
import kotlinx.android.synthetic.main.fragment_home_gc.*

/**
 *
 * 介紹:
 * 作者:CHOK
 * 如果Tablayout+viewpager中的子view中初始化顯示的時候不是最頂部 應該是 子view中有view獲取到了焦點
 */
class HomeGcFragment : BaseFragment<GcPresenter>(), IGcView {


    var adapter: HomeGcAdapter? = null
    var list: MutableList<PublicData.ThreeBigVideoData> = arrayListOf()

    var type = 1//1-国产 2日韩 3欧美

    companion object {
        const val KEY_THREE_TYPE = "key_three_type"
        fun newIncetance(type: Int): HomeGcFragment {
            val homeGcFragment = HomeGcFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_THREE_TYPE, type)
            homeGcFragment.arguments = bundle
            return homeGcFragment
        }
    }

    override fun getResId(): Any {
        return R.layout.fragment_home_gc
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            type = bundle.getInt(KEY_THREE_TYPE)
        }
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_gc.layoutManager = linearLayoutManager
        adapter = HomeGcAdapter()
        rv_gc.adapter = adapter
        rv_gc.addItemDecoration(DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, R.drawable.listdivider_white_40))
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)

        refresh_layout.setOnRefreshListener {
            mPresenter.getGetThreeBigVideoPageDataReqeust(true, type)
        }
        refresh_layout.setOnLoadMoreListener {
            mPresenter.getGetThreeBigVideoPageDataReqeust(false, type)
        }

        adapter?.invoke(object : HomeGcAdapter.ClickListener {
            override fun click(firstPosion: Int, secondPosion: Int, view: View, isItemCLick: Boolean) {
                val bean = adapter!!.data[firstPosion] as PublicData.ThreeBigVideoData
                if (isItemCLick) {//子適配器的item點擊
//                    childDoubleStart(VideoPlayFragment.newIncetance(bean.videoDataList[secondPosion].videoId))
                    startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID,bean.videoDataList[secondPosion].videoId))
                } else {//子適配器的item中的view點擊
                    mPresenter.collection(view, bean.videoDataList[secondPosion].videoId)
                }
            }
        })


        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as PublicData.ThreeBigVideoData
            val parent = parentFragment as BaseFragment<*>
            val parent1 = parent.parentFragment as BaseFragment<*>
            when (view.id) {
                R.id.tv_more -> {//更多
                    parent1.start(CategoryMoreFragment.newIncetance(bean.dataId, bean.recommendName, 2))
                }
            }
        }

    }


    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        refresh_layout.autoRefresh()
    }
    override fun finishRefresh() {
        refresh_layout.finishRefresh()
    }

    /**
     * 數據回調
     */
    override fun getVideoData(refresh: Boolean, list: MutableList<PublicData.ThreeBigVideoData>, total: Long, page: Int) {

        if (refresh) {//刷新
            refresh_layout.finishRefresh()
            this.list.clear()
            this.list.addAll(list)
        } else {//加载
            refresh_layout.finishLoadMore()
            this.list.addAll(list)
            if (total == page.toLong()) {//添加完数据
                refresh_layout.finishLoadMoreWithNoMoreData()
            }
        }
        if (this.list.size == 0) {//没有数据
            adapter!!.setNewData(this.list)
            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
            adapter!!.emptyView = emptyView
        } else {//有数据
            adapter!!.setNewData(this.list)
        }
    }

    //type 1-当前为收藏 2-当前为没有收藏
    override fun collection(view: View, type: Int) {
        val isChoice = view.getTag(R.id.tv_new_price)
        if ((view as ImageView).id == R.id.img_collection) {
            if (isChoice is Boolean) {
                view.isSelected = !isChoice
                view.setTag(R.id.tv_new_price, !isChoice)
            }
        }
    }


}