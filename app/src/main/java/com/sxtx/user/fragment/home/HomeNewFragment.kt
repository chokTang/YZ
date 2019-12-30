package com.sxtx.user.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.like.base.base.BaseActivity
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.activity.VideoPlayActivity
import com.sxtx.user.adapter.HomeNewAdapter
import com.sxtx.user.mvp.presenter.home.NewPresenter
import com.sxtx.user.mvp.view.main.INewView
import com.sxtx.user.util.UrlCheckUtils
import kotlinx.android.synthetic.main.fragment_new.*

/**
 *
 * 介紹: 最新 排行的
 * 作者:CHOK
 */

class HomeNewFragment : BaseFragment<NewPresenter>(), INewView {


    var adapter: HomeNewAdapter? = null
    var list: MutableList<PublicData.NewestVideoData> = arrayListOf()

    var type = 1//type //1-最新的 2排行的

    companion object {
        const val KEY_NEW_TYPE = "key_new_type"
        fun newIncetance(type: Int): HomeNewFragment {
            val homeNewFragment = HomeNewFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_NEW_TYPE, type)
            homeNewFragment.arguments = bundle
            return homeNewFragment
        }
    }


    override fun getResId(): Any {
        return R.layout.fragment_new
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            type = bundle.getInt(KEY_NEW_TYPE)
        }
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)

        val linearLayoutManager = LinearLayoutManager(activity)
        rv_new.layoutManager = linearLayoutManager
        adapter = HomeNewAdapter()
        rv_new.adapter = adapter


    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)

        refresh_layout.setOnRefreshListener {
            mPresenter.getNewestPageDataReqeust(true, type)
        }
        refresh_layout.setOnLoadMoreListener {
            mPresenter.getNewestPageDataReqeust(false, type)
        }

        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as PublicData.NewestVideoData
            when (view.id) {
                R.id.img_collection -> {
                    mPresenter.collection(position, bean.videoId)
                }
            }
        }
        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as PublicData.NewestVideoData
//            childDoubleStart(VideoPlayFragment.newIncetance(bean.videoId))
            startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID,bean.videoId))

        }

        adapter?.invoke(object : HomeNewAdapter.BannerCLick {
            override fun click(position: Int, bean: PublicData.AdvertisingData) {
                if (bean.goWhere == 1) {
                    if (UrlCheckUtils.checkUrlIsEffective(bean.externalAddress)){
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(bean.externalAddress))))
                        mPresenter.clickAd(bean.id)
                    }else{
                        showToast("無效鏈接")
                    }
                } else {
                    try {
                        val result = Class.forName(bean.androidAdress) as Class<*>
                        val ob = result.newInstance()
                        if (ob is BaseFragment<*>) {
                            val baseFragment = ob
                            childDoubleStart(baseFragment)
                        } else if (ob is BaseActivity<*>) {
                            val intent = Intent(activity, result)
                            startActivity(intent)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }

            }
        })
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
    override fun getNewVideoData(refresh: Boolean, list: MutableList<PublicData.NewestVideoData>, total: Long, page: Int) {

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
    override fun collection(position: Int, type: Int) {
        adapter?.isColle = type
        adapter?.position = position
        adapter?.notifyItemChanged(position)
    }


}