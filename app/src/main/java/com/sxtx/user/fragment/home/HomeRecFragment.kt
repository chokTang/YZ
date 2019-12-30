package com.sxtx.user.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.like.base.adapter.rvhelper.DividerItemDecoration
import com.like.base.base.BaseActivity
import com.like.base.base.BaseFragment
import com.like.utilslib.screen.DensityUtil
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.activity.VideoPlayActivity
import com.sxtx.user.adapter.HomeRecAdapter
import com.sxtx.user.fragment.main.HomeFragment
import com.sxtx.user.mvp.presenter.home.RecPresenter
import com.sxtx.user.mvp.view.main.IRecView
import com.sxtx.user.util.GlideImageLoader
import com.sxtx.user.util.UrlCheckUtils
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_home_rec.*

/**
 *
 * 介紹: 最新
 * 作者:CHOK
 */

class HomeRecFragment : BaseFragment<RecPresenter>(), IRecView {

    companion object {
        const val TYPE_1 = 1
        const val TYPE_2 = 2
        const val TYPE_3 = 3
    }

    var adapter: HomeRecAdapter? = null
    var bannerData: MutableList<PublicData.AdvertisingData> = arrayListOf()
    var recommendList: MutableList<PublicData.RecommendData> = arrayListOf()
    var isLoadBanner = false

    override fun getResId(): Any {
        return R.layout.fragment_home_rec
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_rec.layoutManager = linearLayoutManager
        adapter = HomeRecAdapter()
        rv_rec.adapter = adapter
        rv_rec.addItemDecoration(DividerItemDecoration(DividerItemDecoration.VERTICAL_LIST, R.drawable.listdivider_white_10))
        banner.isFocusable = false
        rv_rec.isFocusable = false
        banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                if (ll_desc==null || bannerData.size == 0)return
                if (bannerData.size > position){
                    if (TextUtils.isEmpty(bannerData[position].describe)){
                        ll_desc.visibility = View.GONE
                    }else{
                        ll_desc.visibility = View.VISIBLE
                        tv_desc.text = bannerData[position].describe
                    }
                }
            }

        })
        banner.setOnBannerListener { position ->
            if (bannerData[position].goWhere == 1) {
                if (UrlCheckUtils.checkUrlIsEffective(bannerData[position].externalAddress)){
                    startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(UrlCheckUtils.checkUrl(bannerData[position].externalAddress))))
                    mPresenter.clickAd(bannerData[position].id)
                }else{
                    showToast("無效鏈接")
                }
            } else {
                try {
                    val result = Class.forName(bannerData[position].androidAdress) as Class<*>
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
        scroll.setOnScrollChangeListener(object : NestedScrollView.OnScrollChangeListener{
            override fun onScrollChange(v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                if (scrollY>=DensityUtil.dpTopx(20f)){
                    view_top.visibility = View.GONE
                }else{
                    view_top.visibility = View.VISIBLE
                }
            }
        })
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        refresh_layout.setOnRefreshListener {
            adapter?.list= arrayListOf()
            mPresenter.GetRecommendPageDataReqeust()
            mPresenter.getTrumpetReqeust()
        }

        refresh_layout.setEnableLoadMore(false)

        //子適配器中的item和view點擊事件
        adapter?.invoke(object :HomeRecAdapter.ClickListener{
            override fun click(firstPosion: Int, secondPosion: Int, view: View, isItemCLick: Boolean) {
                val bean = adapter!!.data[firstPosion] as PublicData.RecommendData
                if (isItemCLick) {//子適配器的item點擊
//                    startActivity(Intent(activity, DetailMoreTypeActivity::class.java))
                    if (firstPosion==0){
                        if (adapter?.list?.size!! >0){
                            startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID, adapter?.list!![secondPosion].videoId))
                        }else{
                            startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID,bean.videoDataList[secondPosion].videoId))
                        }
                    }else{
                        startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID,bean.videoDataList[secondPosion].videoId))
                    }

                } else {//子適配器的item中的view點擊
                    when (view.id) {
                        R.id.img_collection -> {
                            mPresenter.collection(view, false, bean.videoDataList[secondPosion].videoId)
                        }
                    }
                }

            }
        })

        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as PublicData.RecommendData
            when (view.id) {
                R.id.img_collection -> {
                    mPresenter.collection(view, true, bean.videoDataList[0].videoId)
                }
                R.id.tv_more -> {//更多
                    if (bean.isMore != 1) {
                        mPresenter.GetRecommendInABatchReqeust(position,bean.dataId)
                        return@setOnItemChildClickListener
                    }
                    when (bean.type) {
                        TYPE_1 -> {
                            (parentFragment as HomeFragment).toFragment(4)
                        }
                        TYPE_2 -> {
                            (parentFragment as HomeFragment).toFragment(5)
                        }
                        TYPE_3 -> {
                            (parentFragment as HomeFragment).toFragment(6)
                        }

                    }
                }

                R.id.img_pic->{
//                    childDoubleStart(VideoPlayFragment.newIncetance(bean.videoDataList[0].videoId))
                    startActivity(Intent(activity, VideoPlayActivity::class.java).putExtra(VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID,bean.videoDataList[0].videoId))

                }
            }
        }

//        banner点击
        adapter?.invoke(object :HomeRecAdapter.BannerCLick{
            override fun click(position: Int,bean:PublicData.AdvertisingData) {
                if (bannerData[position].goWhere == 1) {
                    if (UrlCheckUtils.checkUrlIsEffective(bannerData[position].externalAddress)){
                        startActivity(Intent(Intent.ACTION_VIEW,Uri.parse(UrlCheckUtils.checkUrl(bannerData[position].externalAddress))))
                        mPresenter.clickAd(bannerData[position].id)
                    }else{
                        showToast("無效鏈接")
                    }
                } else {
                    try {
                        val result = Class.forName(bannerData[position].androidAdress) as Class<*>
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

        refresh_layout.autoRefresh()
    }

    override fun getData(list: MutableList<PublicData.RecommendData>) {
        refresh_layout.finishRefresh()
        this.recommendList.clear()
        this.recommendList.addAll(list)
        if (this.recommendList.size == 0){
            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
            adapter!!.emptyView = emptyView
        }
        adapter?.setNewData(this.recommendList)
    }

    override fun finishRefresh() {
        refresh_layout.finishRefresh()
    }

    override fun getBanner(list: MutableList<PublicData.AdvertisingData>) {
        if (!isLoadBanner){
            isLoadBanner = true
            bannerData = list
            val bannerlist: MutableList<String> = arrayListOf()
            banner.setImageLoader(GlideImageLoader())
            for (i in 0 until list.size) {
                bannerlist.add(list[i].pictureAddress)
            }
            //设置图片集合
            banner.setImages(bannerlist)
            //设置指示器位置（当banner模式中有指示器时）
            banner.setIndicatorGravity(BannerConfig.CENTER)
            //banner设置方法全部调用完毕时最后调用
            banner.start()
        }
    }
    /**
     * 换一批回调
     */
    override fun refreshData(position:Int,response: Api.GetRecommendInABatchResponse) {
         adapter?.list = response.videoDataList
         adapter?.notifyItemChanged(position,response.videoDataList)
    }


    override fun onSupportVisible() {
        super.onSupportVisible()
        mPresenter.getTrumpetReqeust()
    }


    /**
     * 获取公告回调
     */
    override fun getAnnouncement(announcement: PublicData.TrumpetData) {
        var text = announcement.content
        val num = 60 - text.length
        for (i in 0 until num) {
            text = "$text  "
        }
        tv_marqueeView.text = text
        tv_marqueeView.isSelected = true
    }


    /**
     * 收藏回调
     */
    override fun collection(view: View, isFirst: Boolean, type: Int) {
        val isChoice = view.getTag(R.id.tv_new_price)
        if ((view as ImageView).id == R.id.img_collection) {
            if (isChoice is Boolean) {
                view.isSelected = !isChoice
                view.setTag(R.id.tv_new_price, !isChoice)
            }
        }

    }
}
