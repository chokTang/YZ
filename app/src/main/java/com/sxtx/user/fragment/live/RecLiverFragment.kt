package com.sxtx.user.fragment.live

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.like.base.base.BaseActivity
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.adapter.LiveRoomAdapter
import com.sxtx.user.adapter.RecLiveAdapter
import com.sxtx.user.model.bean.LiveBean
import com.sxtx.user.mvp.presenter.live.RecLiverPresenter
import com.sxtx.user.mvp.view.live.IRecLiverView
import com.sxtx.user.util.UrlCheckUtils
import kotlinx.android.synthetic.main.fragment_recliver.*


/**
 *
 * 介紹://1推薦主播  ，  2直播平台
 * 作者:CHOK
 */
class RecLiverFragment : BaseFragment<RecLiverPresenter>(), IRecLiverView {


    var adapter: RecLiveAdapter? = null
    var adapter1: LiveRoomAdapter? = null
    var list: MutableList<PublicData.AnchorData> = arrayListOf()
    var list1: MutableList<PublicData.PlatformData> = arrayListOf()
    var type = 1  //1推薦主播  ，  2直播平台

    companion object {
        const val KEY_LIVE_TYPE = "key_live_type"
        fun newIncetance(type: Int): RecLiverFragment {
            val fragment = RecLiverFragment()
            val bundle = Bundle()
            bundle.putInt(KEY_LIVE_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getResId(): Any {
        return R.layout.fragment_recliver
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        type = bundle!!.getInt(KEY_LIVE_TYPE, 1)
    }


    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        val gradLayoutManager: GridLayoutManager? = GridLayoutManager(activity, 2)
        rv_rec_liver.layoutManager = gradLayoutManager
        if (type == 1) {
            adapter = RecLiveAdapter()
            rv_rec_liver.adapter = adapter
        } else {
            adapter1 = LiveRoomAdapter()
            rv_rec_liver.adapter = adapter1
            adapter1!!.activity = activity
        }
        rv_rec_liver.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_white_16))

    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)

        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter!!.data[position] as PublicData.AnchorData
            val liveBean = LiveBean()
            liveBean.address = bean.address
            liveBean.bannerImg = bean.bannerImg
            liveBean.isFree = bean.isFree
            liveBean.name = bean.name
            liveBean.isVip = bean.isVip
            liveBean.watchTv = bean.watchTv
            val parent = parentFragment as BaseFragment<*>
            val parent1 = parent.parentFragment as BaseFragment<*>
            parent1.start(LiveDetailFragment.newIncetance(liveBean))
        }

        adapter1?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter!!.data[position] as PublicData.PlatformData
            val parent = parentFragment as BaseFragment<*>
            val parent1 = parent.parentFragment as BaseFragment<*>
            parent1.start(LiverListFragment.newIncetance(bean.platform, bean.name))
        }

        if (type == 1) {//推荐主播
            refresh_layout.setOnRefreshListener {
                mPresenter.getGetRecommedThrHostReqeust(true, "")
            }
            refresh_layout.setOnLoadMoreListener {
                mPresenter.getGetRecommedThrHostReqeust(false, "")
            }
        } else {//直播平台
            refresh_layout.setOnRefreshListener {
                mPresenter.getGetLivePlatformReqeust(true)
            }
            refresh_layout.setOnLoadMoreListener {
                mPresenter.getGetLivePlatformReqeust(false)
            }
        }


        adapter?.invoke(object : RecLiveAdapter.BannerCLick {
            override fun click(position: Int, bean: PublicData.AdvertisingData) {
                val parent = parentFragment as BaseFragment<*>
                val parent1 = parent.parentFragment as BaseFragment<*>
                if (bean.goWhere == 1) {
                    if (UrlCheckUtils.checkUrlIsEffective(bean.externalAddress)) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(bean.externalAddress))))
                        mPresenter.clickAd(bean.id)
                    } else
                        showToast("無效鏈接")
                } else {
                    try {
                        val result = Class.forName(bean.androidAdress) as Class<*>
                        val ob = result.newInstance()
                        if (ob is BaseFragment<*>) {
                            val baseFragment = ob
                            parent1.start(baseFragment)
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
        adapter1?.invoke(object : LiveRoomAdapter.BannerCLick {
            override fun click(position: Int, bean: PublicData.AdvertisingData) {
                val parent = parentFragment as BaseFragment<*>
                val parent1 = parent.parentFragment as BaseFragment<*>
                if (bean.goWhere == 1) {
                    if (UrlCheckUtils.checkUrlIsEffective(bean.externalAddress)) {
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(bean.externalAddress))))
                        mPresenter.clickAd(bean.id)
                    } else
                        showToast("無效鏈接")

                } else {
                    try {
                        val result = Class.forName(bean.androidAdress) as Class<*>
                        val ob = result.newInstance()
                        if (ob is BaseFragment<*>) {
                            val baseFragment = ob
                            parent1.start(baseFragment)
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

        refresh_layout.autoRefresh()
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
//        if (type == 1) {
//            mPresenter.getGetRecommedThrHostReqeust(true, "")
//        } else {
//            mPresenter.getGetLivePlatformReqeust(true)
//        }
    }


    override fun finishRefresh() {
        refresh_layout.finishRefresh()
    }


    override fun noData() {
        refresh_layout.finishRefresh()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
        if (type == 1) {
            adapter!!.emptyView = emptyView
        } else {
            adapter1!!.emptyView = emptyView
        }
    }


    /**
     * 推荐主播回调
     */
    override fun getRecommedData(refresh: Boolean, response: Api.GetRecommedThrHostResponse?) {
        if (refresh) {//刷新
            refresh_layout.finishRefresh()
            this.list.clear()
            this.list.addAll(response!!.dataList)
        } else {//加载
            refresh_layout.finishLoadMore()
            this.list.addAll(response!!.dataList)
            if (response.total == response.page.toLong()) {//添加完数据
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

    /**
     * 直播平台回调
     */
    override fun getLivePlatformData(refresh: Boolean, response: Api.GetLivePlatformResponse) {
        if (refresh) {//刷新
            refresh_layout.finishRefresh()
            this.list1.clear()
            this.list1.addAll(response.dataList)
        } else {//加载
            refresh_layout.finishLoadMore()
            this.list1.addAll(response.dataList)
            if (response.total == response.page.toLong()) {//添加完数据
                refresh_layout.finishLoadMoreWithNoMoreData()
            }
        }
        if (this.list1.size == 0) {//没有数据
            adapter1!!.setNewData(this.list1)
            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
            adapter1!!.emptyView = emptyView
        } else {//有数据
            adapter1!!.setNewData(this.list1)
        }
    }
}