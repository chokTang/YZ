package com.sxtx.user.fragment.home

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import com.like.base.base.BaseActivity
import com.like.base.base.BaseFragment
import com.lyh.protocol.data.PublicData
import com.lyh.protocol.login.Api
import com.sxtx.user.R
import com.sxtx.user.adapter.HomeTopicAdapter
import com.sxtx.user.mvp.presenter.home.TopicPresenter
import com.sxtx.user.mvp.view.main.ITopicView
import com.sxtx.user.util.UrlCheckUtils
import kotlinx.android.synthetic.main.fragment_topic.*

/**
 *
 * 介紹: 專題
 * 作者:CHOK
 */

class HomeTopicFragment : BaseFragment<TopicPresenter>(), ITopicView {


    var adapter: HomeTopicAdapter? = null
    var list: MutableList<PublicData.SpecialData> = arrayListOf()

    override fun getResId(): Any {
        return R.layout.fragment_topic
    }

    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        val linearLayoutManager = LinearLayoutManager(activity)
        rv_topic.layoutManager = linearLayoutManager
        adapter = HomeTopicAdapter()
        rv_topic.adapter = adapter
        banner.isFocusable = false
        rv_topic.isFocusable = false
        rv_topic.isNestedScrollingEnabled = false
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
        adapter?.setOnItemClickListener { adapter, _, position ->
            val bean = adapter.data[position] as PublicData.SpecialData
            val parent = parentFragment as BaseFragment<*>
            val parent1 = parent.parentFragment as BaseFragment<*>
            parent1.start(CategoryMoreFragment.newIncetance(bean.specialId, bean.name, 1))

        }

        refresh_layout.setOnRefreshListener {
            mPresenter.getSpecialPageDataReqeust(true)
        }
        refresh_layout.setOnLoadMoreListener {
            mPresenter.getSpecialPageDataReqeust(false)
        }

        adapter?.invoke(object :HomeTopicAdapter.BannerCLick{
            override fun click(position: Int,bean:PublicData.AdvertisingData) {
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

    }

    override fun finishRefresh() {
        refresh_layout.finishRefresh()
    }

    override fun getTopicData(pageNum:Int,refresh: Boolean, response: Api.GetSpecialPageDataResponse) {
        val list = response.specialDataList
        if (refresh) {//刷新
            refresh_layout.finishRefresh()
            this.list.clear()
            this.list.addAll(list)
        } else {//加载
            refresh_layout.finishLoadMore()
            this.list.addAll(list)
            if (response.total==response.page.toLong()) {//添加完数据
                refresh_layout.finishLoadMoreWithNoMoreData()
            }
        }
        if (this.list.size == 0) {//没有数据
            adapter!!.setNewData(this.list)
            val emptyView = LayoutInflater.from(context).inflate(R.layout.empty_layout, null, false)
            adapter!!.emptyView = emptyView
        } else {//有数据
            adapter?.type = 2
            adapter!!.setNewData(this.list)
        }

    }

    override fun onLazyInitView(savedInstanceState: Bundle?) {
        super.onLazyInitView(savedInstanceState)
        refresh_layout.autoRefresh()
    }




}