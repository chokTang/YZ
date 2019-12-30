package com.sxtx.user.fragment.home

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import com.like.base.adapter.rvhelper.DividerGridItemDecoration
import com.like.base.base.BaseFragment
import com.like.base.base.BaseStatusToolbarFragment
import com.lyh.protocol.data.PublicData
import com.sxtx.user.R
import com.sxtx.user.activity.VideoPlayActivity
import com.sxtx.user.activity.VideoPlayActivity.KEY_ACTIVITY_VIDEO_ID
import com.sxtx.user.adapter.HomeGcContentAdapter
import com.sxtx.user.mvp.presenter.main.CategroyMorePresenter
import com.sxtx.user.mvp.view.main.ICategoryMoreView
import kotlinx.android.synthetic.main.fragment_categroy_more.*

/**
 *
 * 介紹:專題1  三大分類 2
 * 作者:CHOK
 */
class CategoryMoreFragment : BaseStatusToolbarFragment<CategroyMorePresenter>(), ICategoryMoreView {


    var adapter: HomeGcContentAdapter? = null
    var list: MutableList<PublicData.ClassifyVideoData> = arrayListOf()
    var specialId = 0L
    var title = ""
    var type = 1
    companion object {
        const val KEY_SPECIALID = "specialId"
        const val KEY_SPECIAL_NAME = "specialIdName"
        const val KEY_TYPE= "type"//專題1  三大分類 2
        fun newIncetance(specialId: Long?,title:String?,type: Int): CategoryMoreFragment {
            val fragment = CategoryMoreFragment()
            val bundle = Bundle()
            bundle.putLong(KEY_SPECIALID, specialId!!)
            bundle.putString(KEY_SPECIAL_NAME, title!!)
            bundle.putInt(KEY_TYPE, type)
            fragment.arguments = bundle
            return fragment
        }
    }


    override fun getMainResId(): Int {
        return R.layout.fragment_categroy_more
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        if (bundle != null) {
            specialId = bundle.getLong(KEY_SPECIALID)
            title = bundle.getString(KEY_SPECIAL_NAME)!!
            type = bundle.getInt(KEY_TYPE)
        }
    }



    override fun onInitView(savedInstanceState: Bundle?) {
        super.onInitView(savedInstanceState)
        onBack()
        setToolbarTitle(title)
        setShowLineTitle(false)
        showView(BaseFragment.MAIN_VIEW)

        val gridLayoutManager = GridLayoutManager(activity, 2)
        rv_category.layoutManager = gridLayoutManager
        adapter = HomeGcContentAdapter()
        adapter?.isShowSvga = false
        rv_category.adapter = adapter
        rv_category.addItemDecoration(DividerGridItemDecoration(R.drawable.listdivider_white_16))
    }

    override fun onInitData(savedInstanceState: Bundle?) {
        super.onInitData(savedInstanceState)
//        if (type==1){//專題
//            mPresenter.getGetSpecialMoreVideoReqeust(true, specialId)
//        }else{//三大分類
//            mPresenter.getGetThreeBigMoreVideoReqeust(true, specialId)
//        }
        refresh_layout.setOnRefreshListener {
            if (type==1){//專題
                mPresenter.getGetSpecialMoreVideoReqeust(true, specialId)
            }else{//三大分類
                mPresenter.getGetThreeBigMoreVideoReqeust(true, specialId)
            }
        }

        adapter?.setOnItemChildClickListener { adapter, view, position ->
            val bean = adapter.data[position] as PublicData.ClassifyVideoData
            when(view.id){
                R.id.img_collection->{//收藏
                    mPresenter.collection(view,bean.videoId)
                }
            }
        }
        adapter?.setOnItemClickListener { adapter, view, position ->
            val bean = adapter.data[position] as PublicData.ClassifyVideoData
            startActivity(Intent(activity,VideoPlayActivity::class.java).putExtra(KEY_ACTIVITY_VIDEO_ID,bean.videoId))
        }


        refresh_layout.setOnLoadMoreListener {
            if (type==1){//專題
                mPresenter.getGetSpecialMoreVideoReqeust(false, specialId)
            }else{//三大分類
                mPresenter.getGetThreeBigMoreVideoReqeust(false, specialId)
            }
        }
    }


    override fun finishRefresh() {
        refresh_layout.finishRefresh()
    }

    override fun onLoadData() {
        super.onLoadData()
        if (type==1){//專題
            mPresenter.getGetSpecialMoreVideoReqeust(true, specialId)
        }else{//三大分類
            mPresenter.getGetThreeBigMoreVideoReqeust(true, specialId)
        }
    }


    override fun getVideoData(refresh: Boolean, list: MutableList<PublicData.ClassifyVideoData>,total: Long,page:Int) {
        if (refresh) {//刷新
            refresh_layout.finishRefresh()
            this.list.clear()
            this.list.addAll(list)
        } else {//加载
            refresh_layout.finishLoadMore()
            this.list.addAll(list)
            if (total==page.toLong()) {//添加完数据
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

    override fun collection(view: View, type: Int) {
        val isChoice=view.getTag(R.id.tv_new_price)
        if ((view as ImageView).id == R.id.img_collection){
            if (isChoice is Boolean){
                view.isSelected = !isChoice
                view.setTag(R.id.tv_new_price,!isChoice)
            }
        }
    }

}