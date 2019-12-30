package com.sxtx.user.fragment.live

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.like.base.base.BaseActivity
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.adapter.RecLiveAdapter
import com.sxtx.user.model.bean.LiveBean
import com.sxtx.user.mvp.presenter.live.RecLiverPresenter
import com.sxtx.user.mvp.view.live.IRecLiverView
import com.sxtx.user.util.UrlCheckUtils
import kotlinx.android.synthetic.main.fragment_recliver.*

/**
 *
 * 介紹:平台主播
 * 作者:CHOK
 */
class LiverListFragment : BaseStatusToolbarFragment<RecLiverPresenter>(), IRecLiverView {


    var adapter: RecLiveAdapter? = null
    var list: MutableList<PublicData.AnchorData> = arrayListOf()
    var platform = ""
    var platformName = ""

    companion object {
        const val KEY_PLATFORM = "key_live_platform"
        const val KEY_PLATFORM_NAME = "key_live_platform_name"
        fun newIncetance(platform: String, platformName: String): LiverListFragment {
            val fragment = LiverListFragment()
            val bundle = Bundle()
            bundle.putString(KEY_PLATFORM, platform)
            bundle.putString(KEY_PLATFORM_NAME, platformName)
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getMainResId(): Int {
        return R.layout.fragment_recliver
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        platform = bundle!!.getString(KEY_PLATFORM)
        platformName = bundle!!.getString(KEY_PLATFORM_NAME)
    }


    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        setToolbarTitle(platformName)
        onBack()
        val gradLayoutManager: GridLayoutManager? = GridLayoutManager(activity, 2)
        rv_rec_liver.layoutManager = gradLayoutManager
        adapter = RecLiveAdapter()
        rv_rec_liver.adapter = adapter
        rv_rec_liver.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_white_16))
        showView(BaseFragment.MAIN_VIEW)
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
            start(LiveDetailFragment.newIncetance(liveBean))
        }


        refresh_layout.setOnRefreshListener {
            mPresenter.getGetRecommedThrHostReqeust(true, platform)
        }
        refresh_layout.setOnLoadMoreListener {
            mPresenter.getGetRecommedThrHostReqeust(false, platform)
        }

        refresh_layout.autoRefresh()


        adapter?.invoke(object :RecLiveAdapter.BannerCLick{
            override fun click(position: Int, bean: PublicData.AdvertisingData) {
                if (bean.goWhere == 1) {
                    if (UrlCheckUtils.checkUrlIsEffective(bean.externalAddress)){
                        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(UrlCheckUtils.checkUrl(bean.externalAddress))))
                        mPresenter.clickAd(bean.id)
                    }else
                        showToast("無效鏈接")

                } else {
                    try {
                        val result = Class.forName(bean.androidAdress) as Class<*>
                        val ob = result.newInstance()
                        if (ob is BaseFragment<*>) {
                            val baseFragment = ob
                            start(baseFragment)
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


    override fun finishRefresh() {
        refresh_layout.finishRefresh()
    }


    override fun noData() {
        refresh_layout.finishRefresh()
        val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
        adapter!!.emptyView = emptyView

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

    }
}